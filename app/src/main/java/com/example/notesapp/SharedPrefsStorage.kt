package com.example.notesapp

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * Storage implementation using SharedPreferences
 * Stores notes as JSON strings in SharedPreferences
 */
class SharedPrefsStorage(private val context: Context) : StorageInterface {

    private val sharedPrefs: SharedPreferences =
        context.getSharedPreferences("NotesAppPrefs", Context.MODE_PRIVATE)
    private val gson = Gson()
    private val notesKey = "notes_list"

    override fun saveNote(note: Note): Boolean {
        return try {
            val notes = getAllNotes().toMutableList()
            notes.add(note)
            saveAllNotes(notes)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    override fun getAllNotes(): List<Note> {
        return try {
            val notesJson = sharedPrefs.getString(notesKey, null)
            if (notesJson != null) {
                val type = object : TypeToken<List<Note>>() {}.type
                gson.fromJson(notesJson, type)
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    override fun deleteNote(noteId: String): Boolean {
        return try {
            val notes = getAllNotes().toMutableList()
            val removed = notes.removeAll { it.id == noteId }
            if (removed) {
                saveAllNotes(notes)
            }
            removed
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    override fun deleteAllNotes(): Boolean {
        return try {
            sharedPrefs.edit().remove(notesKey).apply()
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    override fun getStorageType(): String {
        return "SharedPreferences"
    }

    private fun saveAllNotes(notes: List<Note>) {
        val notesJson = gson.toJson(notes)
        sharedPrefs.edit().putString(notesKey, notesJson).apply()
    }
}