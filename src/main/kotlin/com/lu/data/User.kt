package com.lu.data

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table

object User : Table() {
    val id : Column<Int> = integer ("id").autoIncrement()
    val name: Column<String> = varchar("name", 25)
    val age: Column<Int> = integer("age")
    val sex: Column<Int> = integer("sex")

    override val primaryKey = PrimaryKey(id)
}

data class UserItem (
    val id: Int,
    val name: String,
    val age: Int,
    val sex: Int
    )