package net.pableras.sichefbeta.model

import java.io.Serializable

data class User(
    var id: String = "",
    var nick: String = "",
    var email: String = "",
    var passwd: String = "" ):Serializable

data class Receta(
    var id: String = "",
    var uid: String = "",
    var title: String = "",
    var comensales: String = "",
    var ingredientes: String = "",
    var preparacion: String = "",
    var observaciones: String = "",
    var imagens: String = "",
    var cid: ArrayList<String> = ArrayList()):Serializable

data class Comentario(
    var id: String = "",
    var nick: String = "",
    var rid: String = "",
    var content: String ):Serializable

//clases extra
data class RecetaAux(
    var id: String = "",
    var user: User = User(),
    var title: String = "",
    var comensales: String = "",
    var ingredientes: String = "",
    var preparacion: String = "",
    var observaciones: String = "",
    var imagens: String = "",
    var comentarios: ArrayList<Comentario> = ArrayList()
):Serializable

data class listComent(
    var comentarios: ArrayList<Comentario> = ArrayList()
):Serializable

