package com.jpalomino502.vivebien.core.domain.model

data class User(
    val id: String,
    val name: String,
    val email: String = "",
    val phone: String = "",
    val birthDate: String = ""
) {
    val initials: String
        get() = name.split(" ").mapNotNull { it.firstOrNull()?.toString() }.take(2).joinToString("")
}
