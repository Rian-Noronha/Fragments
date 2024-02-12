package com.rn.jogador.repository.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.rn.jogador.model.Jogador
import com.rn.jogador.repository.sqlite.DATABASE_NAME
import com.rn.jogador.repository.sqlite.DATABASE_VERSION

@Database(entities = [Jogador::class], version = DATABASE_VERSION)
abstract class JogadorDatabase : RoomDatabase() {
    abstract fun jogadorDao(): JogadorDao

    companion object{
        private var instance: JogadorDatabase? = null
         fun getDatabase(context: Context): JogadorDatabase{
            if(instance == null){
                instance = Room.databaseBuilder(
                    context.applicationContext,
                    JogadorDatabase::class.java,
                    DATABASE_NAME
                )
                    .allowMainThreadQueries()
                    .build()
            }

            return instance as JogadorDatabase
        }

        fun destroyIntance(){
            instance = null
        }
    }
}