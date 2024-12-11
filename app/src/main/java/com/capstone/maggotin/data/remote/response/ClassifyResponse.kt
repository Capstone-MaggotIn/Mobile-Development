package com.capstone.maggotin.data.remote.response

data class ClassifyResponse(
	val data: Data? = null,
	val message: String? = null,
	val status: String? = null
)

data class Data(
	val phase: String? = null,
	val result: Int? = null,
	val confidenceScore: Any? = null,
	val createdAt: String? = null,
	val id: String? = null
)

