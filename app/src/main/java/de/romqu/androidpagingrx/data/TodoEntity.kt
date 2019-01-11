package de.romqu.androidpagingrx.data

data class TodoEntity(
    val id: TodoId,
    val text: String
)

inline class TodoId(val id: Long)