package com.example.propertymanagement.di

import com.example.propertymanagement.data.remote.INbrbApi
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val networkModule = module {

    single<Retrofit> {

        Retrofit.Builder()
            .baseUrl("https://api.nbrb.by/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    single<INbrbApi> { get<Retrofit>().create(INbrbApi::class.java) }
}