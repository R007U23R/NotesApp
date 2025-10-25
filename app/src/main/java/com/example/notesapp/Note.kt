package com.example.notesapp

import java.io.Serializable

/**
 * Data class representing a Note
 * Implements Serializable for easy passing between activities
 */
data class Note(
    val id: String = System.currentTimeMillis().toString(),
    val title: String,
    val content: String,
    val timestamp: Long = System.currentTimeMillis()
) : Serializable {

    /**
     * Returns formatted display string for lists
     */
    fun getDisplayTitle(): String {
        return title
    }

    /**
     * Returns a preview of the content
     */
    fun getContentPreview(maxLength: Int = 50): String {
        return if (content.length > maxLength) {
            content.substring(0, maxLength) + "..."
        } else {
            content
        }
    }
}