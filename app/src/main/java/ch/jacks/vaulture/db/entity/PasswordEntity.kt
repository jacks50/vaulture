package ch.jacks.vaulture.db.entity

data class PasswordEntity(
        val passwordId: Long,
        val passwordName: String,
        val passwordUsername: String,
        val passwordURL: String,
        val passwordValue: String,
        val loginId: Long
)