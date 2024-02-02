package com.rn.jogador.repository.sqlite

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.rn.jogador.model.Jogador
import com.rn.jogador.repository.JogadorRepository

class SQLiteRepository(ctx: Context): JogadorRepository {
    private val helper: JogadorSqlHelper = JogadorSqlHelper(ctx)


    private fun inserir(jogador: Jogador){
        val db = helper.writableDatabase
        val cv = ContentValues().apply {
            put(COLUMN_NOME, jogador.nome)
            put(COLUMN_POSICAO, jogador.posicao)
            put(COLUMN_AVALIACAO, jogador.rating)
        }

        val id = db.insert(TABLE_JOGADOR, null, cv)
        if(id != -1L){
            jogador.id = id
        }

        db.close()
    }

    private fun atualizar(jogador: Jogador){
        val db = helper.writableDatabase
        val cv = ContentValues().apply {
            put(COLUMN_ID, jogador.id)
            put(COLUMN_NOME, jogador.nome)
            put(COLUMN_POSICAO, jogador.posicao)
            put(COLUMN_AVALIACAO, jogador.rating)
        }

        db.insertWithOnConflict(
            TABLE_JOGADOR,
            null,
            cv,
            SQLiteDatabase.CONFLICT_REPLACE)
        db.close()
    }


    override fun salvarJogador(jogador: Jogador) {
        if(jogador.id == 0L){
            inserir(jogador)
        }else{
            atualizar(jogador)
        }
    }

    override fun removerJogador(vararg jogadores: Jogador) {
        val db = helper.writableDatabase
        for(jogador in jogadores){
            db.delete(
                TABLE_JOGADOR,
                "$COLUMN_ID = ?",
                arrayOf(jogador.id.toString())
            )
        }

        db.close()
    }

    override fun jogadorPorId(id: Long, callback: (Jogador?) -> Unit) {
        val sql = "SELECT * FROM $TABLE_JOGADOR WHERE $COLUMN_ID = ?"
        val db = helper.readableDatabase
        val cursor = db.rawQuery(sql, arrayOf(id.toString()))
        val jogador = if(cursor.moveToNext())
            jogadorFromCursor(cursor) else null
        callback(jogador)
    }

    override fun buscar(termo: String, callback: (List<Jogador>) -> Unit) {
        var sql = "SELECT * FROM $TABLE_JOGADOR"
        var args:Array<String>? = null
        if(termo.isNotEmpty()){
            sql += " WHERE $COLUMN_NOME LIKE ?"
            args = arrayOf("%$termo%")
        }
        
        sql += " ORDER BY $COLUMN_NOME"
        val db = helper.readableDatabase
        val cursor = db.rawQuery(sql, args)
        val jogadores = ArrayList<Jogador>()
        while(cursor.moveToNext()){
            val jogador = jogadorFromCursor(cursor)
            jogadores.add(jogador)
        }

        cursor.close()
        db.close()
        callback(jogadores)
    }

    private fun jogadorFromCursor(cursor: Cursor): Jogador{
        val id = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_ID))
        val nome = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NOME))
        val posicao = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_POSICAO))
        val avaliacao = cursor.getFloat(cursor.getColumnIndexOrThrow(COLUMN_AVALIACAO))

        return Jogador(id, nome, posicao, avaliacao)
    }

}