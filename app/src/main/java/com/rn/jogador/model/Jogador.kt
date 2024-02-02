package com.rn.jogador.model

data class Jogador(
    var id:Long = 0,
    var nome:String = "",
    var posicao:String="",
    var rating:Float = 0.0F
){
    override fun toString(): String = nome
}
