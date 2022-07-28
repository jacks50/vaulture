package ch.jacks.vaulture.db.entity

import kotlinx.serialization.Serializable
import java.util.*

// TODO : Remove unused fields (passwordId, loginId)
@Serializable
data class PasswordEntity(
    val passwordId: Long,
    val passwordName: String,
    val passwordUsername: String,
    val passwordURL: String,
    val passwordValue: String,
    val loginId: Long,
    val passwordUID: String = UUID.randomUUID().toString(),
)