package com.rn.jogador.repository.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.rn.jogador.model.Jogador
import com.rn.jogador.repository.sqlite.COLUMN_ID
import com.rn.jogador.repository.sqlite.COLUMN_NOME
import com.rn.jogador.repository.sqlite.TABLE_JOGADOR

@Dao
interface JogadorDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun inserir(jogador:Jogador): Long

    @Update
    fun atualizar(jogador: Jogador): Int

    @Delete
    fun deletar(vararg jogadores: Jogador): Int

    @Query("SELECT * FROM $TABLE_JOGADOR WHERE $COLUMN_ID = :id")
    fun jogadorPorId(id: Long): LiveData<Jogador>

    @Query("""SELECT * FROM $TABLE_JOGADOR WHERE $COLUMN_NOME LIKE :query ORDER BY $COLUMN_NOME""")
    fun pesquisar(query: String) : LiveData<List<Jogador>>

}