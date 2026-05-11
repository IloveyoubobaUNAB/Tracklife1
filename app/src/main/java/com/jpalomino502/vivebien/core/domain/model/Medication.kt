package com.jpalomino502.vivebien.core.domain.model

data class Medication(
    val id: Long = 0,
    val name: String,
    val dose: String,
    val frequency: String,
    val isActive: Boolean = true
)
