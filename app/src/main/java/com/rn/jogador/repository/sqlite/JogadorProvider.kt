package com.rn.jogador.repository.sqlite

import android.content.ContentProvider
import android.content.ContentResolver
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteQueryBuilder
import android.net.Uri

class JogadorProvider : ContentProvider() {

    private lateinit var helper: JogadorSqlHelper
    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        val uriType = sUriMatcher.match(uri)
        val sqlDB = helper.writableDatabase
        val rowsDeleted = when (uriType) {
            TYPE_JOGADOR_DIR ->
                sqlDB.delete(
                    TABLE_JOGADOR, selection, selectionArgs)
            TYPE_JOGADOR_ITEM -> {
                val id = uri.lastPathSegment
                sqlDB.delete(
                    TABLE_JOGADOR, "$COLUMN_ID = ?", arrayOf(id))
            }
            else -> throw IllegalArgumentException(
                "URI não suportada: $uri")
        }
        context?.contentResolver?.notifyChange(uri, null)
        return rowsDeleted
    }

    override fun getType(uri: Uri): String? {
        val uriType = sUriMatcher.match(uri)
        return when (uriType) {
            TYPE_JOGADOR_DIR ->
                return "${ContentResolver.CURSOR_DIR_BASE_TYPE}/com.rn.jogador"
            TYPE_JOGADOR_ITEM ->
                return "${ContentResolver.CURSOR_ITEM_BASE_TYPE}/com.rn.jogador"
            else -> null
        }
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        val uriType = sUriMatcher.match(uri)
        val sqlDB = helper.writableDatabase
        val id: Long
        when (uriType) {
            TYPE_JOGADOR_DIR ->
                id = sqlDB.insertWithOnConflict(
                    TABLE_JOGADOR,
                    null,
                    values,
                    SQLiteDatabase.CONFLICT_REPLACE)
            else ->
                throw IllegalArgumentException("URI não suportada: $uri")
        }
        context?.contentResolver?.notifyChange(uri, null)
        return Uri.withAppendedPath(CONTENT_URI, id.toString())
    }

    override fun onCreate(): Boolean {
       helper = JogadorSqlHelper(context)
        return true
    }

    override fun query(
        uri: Uri, projection: Array<String>?, selection: String?,
        selectionArgs: Array<String>?, sortOrder: String?
    ): Cursor? {
        val uriType = sUriMatcher.match(uri)
        val db = helper.writableDatabase
        val queryBuilder = SQLiteQueryBuilder()
        queryBuilder.tables = TABLE_JOGADOR
        val cursor: Cursor
        when (uriType) {
            TYPE_JOGADOR_DIR ->
                cursor = queryBuilder.query(
                    db, projection, selection,
                    selectionArgs, null, null, sortOrder)
            TYPE_JOGADOR_ITEM -> {
                queryBuilder.appendWhere("$COLUMN_ID = ?")
                cursor = queryBuilder.query(db, projection, selection,
                    arrayOf(uri.lastPathSegment),
                    null, null, null)
            }
            else -> throw IllegalArgumentException("URI não suportada: $uri")
        }
        cursor.setNotificationUri(context?.contentResolver, uri)
        return cursor
    }

    override fun update(
        uri: Uri, values: ContentValues?, selection: String?,
        selectionArgs: Array<String>?
    ): Int {
        val uriType = sUriMatcher.match(uri)
        val sqlDB = helper.writableDatabase
        val rowsAffected = when (uriType) {
            TYPE_JOGADOR_DIR ->
                sqlDB.update(
                    TABLE_JOGADOR,
                    values,
                    selection,
                    selectionArgs)
            TYPE_JOGADOR_ITEM -> {
                val id = uri.lastPathSegment
                sqlDB.update(
                    TABLE_JOGADOR,
                    values,
                    "$COLUMN_ID= ?",
                    arrayOf(id))
            }
            else -> throw IllegalArgumentException(
                "URI não suportada: $uri")
        }
        context?.contentResolver?.notifyChange(uri, null)
        return rowsAffected
    }


    companion object {
        private const val AUTHORITY = "com.rn.jogador"
        private const val PATH = "jogadores"
        private const val TYPE_JOGADOR_DIR = 1
        private const val TYPE_JOGADOR_ITEM = 2
        val CONTENT_URI: Uri = Uri.parse("content://$AUTHORITY/$PATH")
        private val sUriMatcher: UriMatcher = UriMatcher(UriMatcher.NO_MATCH)
        init {
            sUriMatcher.addURI(AUTHORITY, PATH, TYPE_JOGADOR_DIR)
            sUriMatcher.addURI(AUTHORITY, "$PATH/#", TYPE_JOGADOR_ITEM)
        }
    }

}