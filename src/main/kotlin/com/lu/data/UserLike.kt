package com.lu.data

import io.ktor.server.engine.*
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table

object  UserLikeTable :Table() {
    val id : Column<Int> = integer("id").autoIncrement()
    val userID : Column<Int> = integer("userID")
    val likeUrl : Column<String> = varchar("likeUrl", 10)

    override val primaryKey : PrimaryKey = PrimaryKey(id)
}
data class UserLikeItem (
    val id: Int,
    val userID: Int,
    val likeUrl: String
        )