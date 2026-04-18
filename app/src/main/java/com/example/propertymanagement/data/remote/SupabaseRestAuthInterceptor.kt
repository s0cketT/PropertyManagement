package com.example.propertymanagement.data.remote

import com.example.propertymanagement.data.common.Constants.ANON_KEY
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.gotrue.auth
import okhttp3.Interceptor
import okhttp3.Response

/**
 * PostgREST должен получать JWT пользователя в `Authorization`, иначе `auth.uid()` в RPC null
 * (например `get_my_properties_with_favorite` вернёт пустой список).
 */
class SupabaseRestAuthInterceptor(
    private val supabase: SupabaseClient
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val bearer = supabase.auth.currentSessionOrNull()?.accessToken ?: ANON_KEY
        val original = chain.request()
        val builder = original.newBuilder()
            .header("apikey", ANON_KEY)
            .header("Authorization", "Bearer $bearer")
        if (original.header("Prefer") == null) {
            builder.header("Prefer", "return=representation")
        }
        return chain.proceed(builder.build())
    }
}
