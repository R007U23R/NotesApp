package com.example.notesapp

import android.content.Context

/**
 * Singleton manager for handling storage operations
 * Allows switching between different storage implementations
 */
object StorageManager {

    enum class StorageType {
        SHARED_PREFS,
        JSON_FILE
    }

    private var currentStorageType = StorageType.SHARED_PREFS
    private lateinit var currentStorage: StorageInterface

    fun initialize(context: Context, storageType: StorageType = StorageType.SHARED_PREFS) {
        currentStorageType = storageType
        currentStorage = when (storageType) {
            StorageType.SHARED_PREFS -> SharedPrefsStorage(context)
            StorageType.JSON_FILE -> JsonFileStorage(context)
        }
    }

    fun switchStorage(context: Context) {
        // Get all notes from current storage
        val notes = currentStorage.getAllNotes()

        // Switch to other storage type
        currentStorageType = if (currentStorageType == StorageType.SHARED_PREFS) {
            StorageType.JSON_FILE
        } else {
            StorageType.SHARED_PREFS
        }

        // Initialize new storage
        currentStorage = when (currentStorageType) {
            StorageType.SHARED_PREFS -> SharedPrefsStorage(context)
            StorageType.JSON_FILE -> JsonFileStorage(context)
        }

        // Clear new storage and migrate notes
        currentStorage.deleteAllNotes()
        notes.forEach { currentStorage.saveNote(it) }
    }

    fun getStorage(): StorageInterface {
        if (!::currentStorage.isInitialized) {
            throw IllegalStateException("StorageManager not initialized. Call initialize() first.")
        }
        return currentStorage
    }

    fun getCurrentStorageType(): String {
        return currentStorage.getStorageType()
    }
}