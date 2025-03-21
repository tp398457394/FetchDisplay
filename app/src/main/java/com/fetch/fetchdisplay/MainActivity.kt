package com.fetch.fetchdisplay

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.fetch.fetchdisplay.domain.entity.Item
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
                            title = { Text("Fetch Display") },
                            colors = TopAppBarDefaults.topAppBarColors().copy(
                                containerColor = MaterialTheme.colorScheme.primaryContainer,
                                titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        )
                    }
                ) { innerPadding ->
                    when {
                        uiState.isLoading -> {
                            Column(modifier = Modifier.padding(innerPadding)) {
                                Text(text = "Loading...", modifier = Modifier.padding(16.dp))
                            }
                        }

                        uiState.hasError -> {
                            Column(modifier = Modifier.padding(innerPadding)) {
                                Text(text = "Encountered error...", modifier = Modifier.padding(16.dp))
                            }
                        }

                        else -> FetchDisplayList(innerPadding, uiState)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun FetchDisplayList(
    innerPadding: PaddingValues,
    uiState: UiState
) {
    val keys = uiState.groupedItems.keys
    val checkStates = remember {
        mutableStateMapOf<Int, Boolean>().apply {
            keys.forEach { put(it, false) }
        }
    }
    val items: List<Item> by remember {
        derivedStateOf {
            uiState.groupedItems
                .filterKeys { checkStates[it] == true }
                .values
                .flatten()
        }
    }

    LazyColumn(
        modifier = Modifier.padding(innerPadding)
    ) {
        stickyHeader {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .background(MaterialTheme.colorScheme.surfaceContainer)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, top = 8.dp, bottom = 8.dp)
                        .horizontalScroll(rememberScrollState()),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(text = "Filter listId: ")
                    keys.forEachIndexed { _, listId ->
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = listId.toString())
                            Checkbox(
                                checked = checkStates[listId] ?: false,
                                onCheckedChange = { isChecked ->
                                    checkStates[listId] = isChecked
                                }
                            )
                        }
                    }
                }
                HorizontalDivider(thickness = 2.dp)
            }
        }
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
                ) {
                    Text(
                        text = "List ID: ${curItem.listId}",
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = "Name: ${curItem.name}",
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = "ID: ${curItem.id}",
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}