package de.romqu.androidpagingrx.data

class TodoRepo {

    fun getAll() =

        LongRange(0, 20)
            .map {
                TodoEntity(
                    id = TodoId(it),
                    text = it.toString()
                )
            }


}