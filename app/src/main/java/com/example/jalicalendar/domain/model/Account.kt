package com.example.jalicalendar.domain.model

/** A signed-in account (currently Google). [id] is the OIDC `sub` — stable across email changes. */
data class Account(
    val id: String,
    val email: String,
    val displayName: String? = null,
    val photoUrl: String? = null,
    val provider: String = "google",
    val scopes: String? = null,
    val addedAtMillis: Long = 0L,
)
