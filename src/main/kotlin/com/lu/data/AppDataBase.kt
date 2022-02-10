package com.lu.data

import com.github.javafaker.Faker
import io.ktor.server.auth.*
import kotlinx.css.Contain
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import java.net.URL
import java.util.*
import com.google.gson.Gson
import com.lu.data.UserLikeTable.userID
import kotlinx.css.i

object AppDataBase {
    fun initDataBase() {
        Database.connect(
            "jdbc:mysql://localhost:3307/Neko",
            driver = "com.mysql.cj.jdbc.Driver",
            user = "root",
            password = "pwd"
        )
        transaction {
            SchemaUtils.create(User)
            SchemaUtils.create(UserLikeTable)
        }

    }

    fun createUser() {
        val faker = Faker(Locale.TAIWAN)
        transaction {
            User.insert {
                it[name] = faker.name().name()
                it[age] = faker.number().numberBetween(2, 40)
                it[sex] = faker.number().numberBetween(0, 3)
            }
        }
    }

    fun getAllUsers(): List<UserItem> {
        return transaction {
            User.selectAll().map {
                UserItem(
                    it[User.id],
                    it[User.name],
                    it[User.age],
                    it[User.sex]
                )
            }
        }
    }

    fun searchUserSex(sreachSex: Int): List<UserItem> {
        return transaction {
            User.select { User.sex eq sreachSex }.map {
                UserItem(
                    it[User.id],
                    it[User.name],
                    it[User.age],
                    it[User.sex]
                )
            }
        }
    }

    fun getUser(userID: Int): UserItem? {
        return transaction {
            val list = User.select { User.id eq userID }.limit(1).map {
                UserItem(
                    it[User.id],
                    it[User.name],
                    it[User.age],
                    it[User.sex]
                )
            }
            if (list.isEmpty()) null else list[0]
        }
    }

    fun getImage(): String {
        val jsonText = URL("https://nekos.best/api/v1/nekos").readText()
        val item = Gson().fromJson(jsonText, NekoApi::class.java)
        return item.url
    }

    fun UserLikeImage(userID: Int, imageUrl: String) {
        transaction {
            UserLikeTable.insert {
                it[UserLikeTable.userID] = userID
                it[UserLikeTable.likeUrl] = imageUrl
            }
        }
    }

    fun getUserLikes(userID: Int): List<UserLikeItem> {
        return transaction {
            UserLikeTable.select { UserLikeTable.userID eq userID }.map {
                UserLikeItem(
                    it[UserLikeTable.id],
                    it[UserLikeTable.userID],
                    it[UserLikeTable.likeUrl]
                )
            }
        }
    }
}