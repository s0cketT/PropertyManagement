package com.example.propertymanagement.data.remote.auth

import com.example.propertymanagement.data.model.UserDto
import com.example.propertymanagement.domain.model.SellerType
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.gotrue.OtpType
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.gotrue.providers.builtin.Email
import io.github.jan.supabase.gotrue.providers.builtin.OTP
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

class AuthServiceImpl(
    private val supabase: SupabaseClient
) : AuthService {

    override suspend fun sendOtp(email: String) {
        supabase.auth.signInWith(OTP) {
            this.email = email
        }
    }

    override suspend fun verifyOtp(
        email: String,
        code: String
    ): UserDto {

        supabase.auth.verifyEmailOtp(
            type = OtpType.Email.EMAIL,
            email = email,
            token = code
        )

        val user = supabase.auth.currentUserOrNull()
            ?: throw IllegalStateException("User is null")

        return UserDto(
            id = user.id,
            email = user.email ?: ""
        )
    }

    override suspend fun logout() {
        supabase.auth.signOut()
    }

    override suspend fun signUp(
        email: String,
        password: String,
        firstName: String,
        sellerType: SellerType
    ) {
        supabase.auth.signUpWith(Email) {
            this.email = email
            this.password = password

            data = buildJsonObject {
                put("first_name", firstName)
                put("seller_type", sellerType.name)
            }
        }
    }

    override suspend fun signIn(email: String, password: String): UserDto {
        supabase.auth.signInWith(Email) {
            this.email = email
            this.password = password
        }

        val user = supabase.auth.currentUserOrNull()
            ?: throw IllegalStateException("User is null")

        return UserDto(
            id = user.id,
            email = user.email ?: ""
        )
    }

    override suspend fun getCurrentUser(): UserDto? {
        val user = supabase.auth.currentUserOrNull() ?: return null

        return UserDto(
            id = user.id,
            email = user.email ?: ""
        )
    }
}