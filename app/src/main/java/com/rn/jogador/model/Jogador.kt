package com.rn.jogador.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.rn.jogador.repository.sqlite.COLUMN_ID
import com.rn.jogador.repository.sqlite.TABLE_JOGADOR

@Entity(tableName = TABLE_JOGADOR)
data class Jogador(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = COLUMN_ID)
    var id:Long = 0,
    var name:String = "",
    var positon:String="",
    var rating:Float = 0.0F
){
    override fun toString(): String = name
}
