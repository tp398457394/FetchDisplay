package com.fetch.fetchdisplay.di

import com.fetch.fetchdisplay.data.ItemsApi
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val appModule = module {
    single<Retrofit> { provideRetrofit() }
    single<ItemsApi> { provideItemsApi(get()) }
}

fun provideRetrofit(): Retrofit = Retrofit.Builder()
    .baseUrl("https://fetch-hiring.s3.amazonaws.com/")
    .addConverterFactory(GsonConverterFactory.create())
    .build()

fun provideItemsApi(retrofit: Retrofit): ItemsApi = retrofit.create(ItemsApi::class.java)