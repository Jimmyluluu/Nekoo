package com.lu.plugins

import com.lu.data.AppDataBase
import io.ktor.server.routing.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.html.*
import io.ktor.server.response.*
import kotlinx.css.h1
import kotlinx.css.img
import kotlinx.html.*

fun Application.configureRouting() {

    routing {
        get("/") {
            call.respondHtml {
                bootstrapPage("iOS Club") {
                    ol {
                        AppDataBase.getAllUsers().forEach {
                            li {
                                a(href = "/${it.id}") {
                                    +it.name
                                }
                            }
                        }
                    }
                }
            }
        }
        get("/{userID}") {
            val userID = call.parameters["userID"]?.toIntOrNull()
            if (userID == null) {
                call.respond(HttpStatusCode.NotFound)
            } else {
                var user = AppDataBase.getUser(userID)
                if (user == null) {
                    call.respond(HttpStatusCode.NotFound)
                } else {
                    val randomImage = AppDataBase.getImage()
                    call.respondHtml {
                        bootstrapPage(user.name) {
                            h1 { +"Hi ${user.name}, ID is ${user.age}" }
                            div(classes = "row") {
                                a(classes = "btn btn-danger col", href = "/${user.id}") {
                                    +"NO"
                                }
                                a(
                                    classes = "btn btn-primary col",
                                    href = "/like/${user.id}/${randomImage.split("/").last()}"
                                ) {
                                    +"YES"
                                }
                            }
                            img(classes = "img-fluid", src = randomImage)
                        }
                    }
                }
            }
        }
        get("/like/{userID}/{imageID}") {
            val userID  = call.parameters["userID"]?.toIntOrNull()
            val imageID = call.parameters["imageID"]
            if (userID == null || imageID == null) {
                call.respond(HttpStatusCode.NotFound)
            }else {
                AppDataBase.UserLikeImage(userID, imageID)
                call.respondRedirect("/$userID")
            }
        }
        get("/likeList/{userID}") {
            val userID = call.parameters["userID"]?.toIntOrNull()
            if (userID == null) {
                call.respond(HttpStatusCode.NotFound)
                return@get // 提早離開
            }
            val userLikes = AppDataBase.getUserLikes(userID)
            call.respondHtml {
                bootstrapPage("User Like") {
                    ol {
                        userLikes.forEach {
                            li {
                                a (href = "https://nekos.best/api/v1/nekos/${it.likeUrl}") {
                                    +it.likeUrl
                                }
                            }
                        }
                    }
                }
            }
        }

        post("/create_user") {
            AppDataBase.createUser()
            call.respondText("sucessful")
        }
    }
    routing {
        route("/api") {
            get("users") {
                call.respond(HttpStatusCode.OK, AppDataBase.getAllUsers())
            }
            get("users/sex/{sex}") {
                val searchSex: Int? = call.parameters["sex"]?.toIntOrNull()
                if (searchSex == null) {
                    call.respond(HttpStatusCode.NotFound)
                } else {
                    call.respond(HttpStatusCode.OK, AppDataBase.searchUserSex(searchSex))
                }
            }
        }
    }
}

fun HTML.bootstrapPage(PageTitle: String, content: FlowContent.() -> Unit) {
    head {
        meta(charset = "utf-8")
        meta(name = "viewport", content = "width=device-width, initial-scale=1")
        title {
            +PageTitle
        }
        link(
            href = "https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css",
            rel = LinkHeader.Rel.Stylesheet
        )


    }
    body {
        div(classes = "container") {
            content()
        }
        script("https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js") {}
    }
}

