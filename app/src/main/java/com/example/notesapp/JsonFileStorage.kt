package com.example.notesapp

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File

/**
 * Storage implementation using JSON files
 * Stores notes in internal storage as JSON file
 */
class JsonFileStorage(private val context: Context) : StorageInterface {

    private val gson = Gson()
    private val fileName = "notes.json"
    private val notesFile: File = File(context.filesDir, fileName)

    init {
        // Create file if it doesn't exist
        if (!notesFile.exists()) {
            notesFile.createNewFile()
            saveAllNotes(emptyList())
        }
    }

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
            if (notesFile.exists() && notesFile.length() > 0) {
                val jsonString = notesFile.readText()
                val type = object : TypeToken<List<Note>>() {}.type
                gson.fromJson(jsonString, type)
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
            saveAllNotes(emptyList())
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    override fun getStorageType(): String {
        return "JSON File"
    }

    private fun saveAllNotes(notes: List<Note>) {
        val jsonString = gson.toJson(notes)
        notesFile.writeText(jsonString)
    }
}