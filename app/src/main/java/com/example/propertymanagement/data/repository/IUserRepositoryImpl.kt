package com.example.propertymanagement.data.repository

import com.example.propertymanagement.data.mapper.toDomain
import com.example.propertymanagement.data.model.CheckEmailRequest
import com.example.propertymanagement.data.model.SellerTypeIdRow
import com.example.propertymanagement.data.model.UpdateAvatarPayload
import com.example.propertymanagement.data.model.UpdateUserProfilePayload
import com.example.propertymanagement.data.model.UserProfileDto
import com.example.propertymanagement.domain.model.SellerType
import com.example.propertymanagement.data.remote.ISupabaseApi
import com.example.propertymanagement.domain.model.UserProfile
import com.example.propertymanagement.domain.repository.IUserRepository
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns


class IUserRepositoryImpl(
    private val supabaseApi: ISupabaseApi,
    private val supabase: SupabaseClient
) : IUserRepository {

    override suspend fun isUserExists(email: String): Boolean {
        val result = supabaseApi.checkEmailExists(CheckEmailRequest(email))
        return result.firstOrNull()?.email_exists ?: false
    }

    override suspend fun getUserProfile(userId: String): UserProfile {
        val result = supabase
            .from("users")
            .select(
                Columns.raw("id, name, email, phone, avatar_url, seller_types(name)")
            ) {
                filter { eq("id", userId) }
            }
            .decodeSingle<UserProfileDto>()

        return result.toDomain()
    }

    override suspend fun updateAvatar(url: String?) {
        val userId = supabase.auth.currentUserOrNull()?.id
            ?: throw IllegalStateException("User not authorized")

        supabase
            .from("users")
            .update(UpdateAvatarPayload(avatarUrl = url)) {
                filter { eq("id", userId) }
            }
    }

    override suspend fun getCurrentAvatarUrl(): String? {
        val userId = supabase.auth.currentUserOrNull()?.id
            ?: throw IllegalStateException("User not authorized")

        return supabase
            .from("users")
            .select(Columns.raw("avatar_url")) {
                filter { eq("id", userId) }
            }
            .decodeSingle<Map<String, String?>>()
            .get("avatar_url")
    }

    override suspend fun updateUserProfile(
        name: String,
        phone: String,
        sellerType: SellerType
    ) {
        val userId = supabase.auth.currentUserOrNull()?.id
            ?: throw IllegalStateException("User not authorized")

        val typeId = supabase
            .from("seller_types")
            .select(Columns.raw("id")) {
                filter { eq("name", sellerType.name) }
            }
            .decodeSingle<SellerTypeIdRow>()
            .id

        supabase
            .from("users")
            .update(
                UpdateUserProfilePayload(
                    name = name,
                    phone = phone,
                    sellerTypeId = typeId
                )
            ) {
                filter { eq("id", userId) }
            }
    }
}