package com.example.notesapp

/**
 * Interface defining storage operations for notes
 * Allows switching between different storage implementations
 */
interface StorageInterface {
    fun saveNote(note: Note): Boolean
    fun getAllNotes(): List<Note>
    fun deleteNote(noteId: String): Boolean
    fun deleteAllNotes(): Boolean
    fun getStorageType(): String
}