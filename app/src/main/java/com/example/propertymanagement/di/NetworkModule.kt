package com.example.propertymanagement.di

import com.example.propertymanagement.data.common.Constants.ANON_KEY
import com.example.propertymanagement.data.common.Constants.BASE_URL_SUPABASE
import com.example.propertymanagement.data.remote.INbrbApi
import com.example.propertymanagement.data.remote.IOverpassApi
import com.example.propertymanagement.data.remote.ISupabaseApi
import com.example.propertymanagement.data.remote.SupabaseRestAuthInterceptor
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.gotrue.Auth
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.storage.Storage
import okhttp3.OkHttpClient
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val networkModule = module {

    single {
        createSupabaseClient(
            supabaseUrl = BASE_URL_SUPABASE,
            supabaseKey = ANON_KEY
        ) {
            install(Auth)
            install(Storage)
            install(Postgrest)
        }
    }

    single(named("nbrb")) {
        Retrofit.Builder()
            .baseUrl("https://api.nbrb.by/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    single<INbrbApi> {
        get<Retrofit>(named("nbrb")).create(INbrbApi::class.java)
    }

    single {
        OkHttpClient.Builder()
            .addInterceptor(SupabaseRestAuthInterceptor(get()))
            .build()
    }

    single(named("supabase")) {
        Retrofit.Builder()
            .baseUrl("$BASE_URL_SUPABASE/rest/v1/")
            .client(get())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    single<ISupabaseApi> {
        get<Retrofit>(named("supabase")).create(ISupabaseApi::class.java)
    }

    single(named("overpass")) {
        val client = OkHttpClient.Builder()
            .addInterceptor { chain ->
                chain.proceed(
                    chain.request().newBuilder()
                        .header(
                            "User-Agent",
                            "PropertyManagement/1.0 (Android; school POI via Overpass)",
                        )
                        .build(),
                )
            }
            .build()
        Retrofit.Builder()
            .baseUrl("https://overpass-api.de/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    single<IOverpassApi> {
        get<Retrofit>(named("overpass")).create(IOverpassApi::class.java)
    }
}
