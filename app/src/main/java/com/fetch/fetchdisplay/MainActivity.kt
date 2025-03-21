package com.fetch.fetchdisplay

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.fetch.fetchdisplay.ui.state.UiState
import com.fetch.fetchdisplay.ui.theme.FetchDisplayTheme
import com.fetch.fetchdisplay.viewmodel.ItemsViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.getViewModel

@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FetchDisplayTheme {
                val scope = rememberCoroutineScope()
                val vm = getViewModel<ItemsViewModel>()
                val uiState by vm.uiState.collectAsStateWithLifecycle()

                LaunchedEffect(Unit) {
                    scope.launch { vm.getItems() }
                }

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        TopAppBar(
                            title = { Text("Fetch Display") }
                        )
                    }
                ) { innerPadding ->
                    when {
                        uiState.isLoading -> {
                            Text("Loading...")
                        }

                        uiState.hasError -> {
                            Text("Encountered error...")
                        }

                        else -> FetchDisplayList(innerPadding, uiState)
                    }
                }
            }
        }
    }

    @Composable
    private fun FetchDisplayList(
        innerPadding: PaddingValues,
        uiState: UiState
    ) {
        LazyColumn(
            modifier = Modifier.padding(innerPadding)
        ) {
            val items = uiState.items
            items(count = items.size, key = { items[it].id }) { idx ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    val curItem = items[idx]
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(text = curItem.listId.toString())
                        Text(text = curItem.name)
                        Text(text = curItem.id.toString())
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    FetchDisplayTheme {
        Greeting("Android")
    }
}