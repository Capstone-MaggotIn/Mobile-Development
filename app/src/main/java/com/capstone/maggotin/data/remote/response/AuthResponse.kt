package com.capstone.maggotin.data.remote.response

data class RegisterRequest(
	val name: String? = null,
	val email: String? = null,
	val password: String? = null,
	val confirmPassword: String? = null
)

data class RegisterResponse(
	val error: Boolean? = null,
	val message: String? = null
)

data class LoginRequest(
	val email: String? = null,
	val password: String? = null
)

data class LoginResponse(
	val loginResult: LoginResult? = null,
	val error: Boolean? = null,
	val message: String? = null
)

data class LoginResult(
	val name: String? = null,
	val userId: String? = null,
	val token: String? = null
)