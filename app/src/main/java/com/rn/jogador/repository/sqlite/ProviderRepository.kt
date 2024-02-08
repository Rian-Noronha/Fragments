package com.rn.jogador.repository.sqlite

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.net.Uri
import com.rn.jogador.model.Jogador
import com.rn.jogador.repository.JogadorRepository

class ProviderRepository(val ctx: Context) : JogadorRepository {
    override fun salvarJogador(jogador: Jogador) {
        val uri = ctx.contentResolver.insert(
            JogadorProvider.CONTENT_URI,
            getValues(jogador))
        val id = uri?.lastPathSegment?.toLong() ?: -1
        if (id != -1L) {
            jogador.id = id
        }
    }

    private fun getValues(jogador: Jogador): ContentValues {
        val cv = ContentValues()
        if (jogador.id > 0) {
            cv.put(COLUMN_ID, jogador.id)
        }
        cv.put(COLUMN_NOME, jogador.nome)
        cv.put(COLUMN_POSICAO, jogador.posicao)
        cv.put(COLUMN_AVALIACAO, jogador.rating)
        return cv
    }

    override fun removerJogador(vararg jogadores: Jogador) {
       jogadores.forEach { jogador ->
           val uri = Uri.withAppendedPath(
               JogadorProvider.CONTENT_URI, jogador.id.toString())
           ctx.contentResolver.delete(uri, null, null)
       }
    }

    override fun jogadorPorId(id: Long, callback: (Jogador?) -> Unit) {
        val cursor = ctx.contentResolver.query(
            Uri.withAppendedPath(JogadorProvider.CONTENT_URI, id.toString()),
            null, null, null, null)
        var jogador: Jogador? = null
        if (cursor?.moveToNext() == true) {
            jogador = jogadorFromCursor(cursor)
        }
        cursor?.close()
        callback(jogador)
    }

    override fun buscar(termo: String, callback: (List<Jogador>) -> Unit) {
        var where: String? = null
        var whereArgs: Array<String>? = null
        if (termo.isNotEmpty()) {
            where = "$COLUMN_NOME LIKE ?"
            whereArgs = arrayOf("%$termo%")
        }
        val cursor = ctx.contentResolver.query(
            JogadorProvider.CONTENT_URI,
            null,
            where,
            whereArgs,
            COLUMN_NOME)
        val jogadores = mutableListOf<Jogador>()
        while (cursor?.moveToNext() == true) {
            jogadores.add(jogadorFromCursor(cursor))
        }
        cursor?.close()
        callback(jogadores)
    }


    private fun jogadorFromCursor(cursor: Cursor): Jogador {
        val id = cursor.getLong(
            cursor.getColumnIndexOrThrow(COLUMN_ID))
        val nome = cursor.getString(
            cursor.getColumnIndexOrThrow(COLUMN_NOME))
        val posicao = cursor.getString(
            cursor.getColumnIndexOrThrow(COLUMN_POSICAO))
        val avaliacao = cursor.getFloat(
            cursor.getColumnIndexOrThrow(COLUMN_AVALIACAO))
        return  Jogador(id, nome, posicao, avaliacao)
    }
}