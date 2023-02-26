package com.example.planner.models

import com.google.gson.annotations.SerializedName

data class RegistrationRequest(
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String,
    @SerializedName("first_name") val firstName: String,
    @SerializedName("name") val name: String
)
