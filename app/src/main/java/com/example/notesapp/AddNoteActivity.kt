package com.example.notesapp

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class AddNoteActivity : AppCompatActivity() {

    // Meaningful variable names (Requirement #1)
    private lateinit var editTextNoteTitle: EditText
    private lateinit var editTextNoteContent: EditText
    private lateinit var btnSave: Button
    private lateinit var btnCancel: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_note)

        // Set activity title
        supportActionBar?.title = getString(R.string.add_note_title)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Initialize views
        initializeViews()

        // Set up button listeners
        setupButtonListeners()
    }

    /**
     * Initialize view references with meaningful names
     */
    private fun initializeViews() {
        editTextNoteTitle = findViewById(R.id.editTextNoteTitle)
        editTextNoteContent = findViewById(R.id.editTextNoteContent)
        btnSave = findViewById(R.id.btnSave)
        btnCancel = findViewById(R.id.btnCancel)
    }

    /**
     * Set up button click listeners
     */
    private fun setupButtonListeners() {
        // Save button
        btnSave.setOnClickListener {
            saveNote()
        }

        // Cancel button
        btnCancel.setOnClickListener {
            finish()
        }
    }

    /**
     * Validate and save the note
     * Requirement #4: Validate input and show Toast for empty fields
     */
    private fun saveNote() {
        val noteTitle = editTextNoteTitle.text.toString().trim()
        val noteContent = editTextNoteContent.text.toString().trim()

        // Validation - check if title is empty
        if (noteTitle.isEmpty()) {
            Toast.makeText(
                this,
                getString(R.string.error_empty_title),
                Toast.LENGTH_SHORT
            ).show()
            editTextNoteTitle.requestFocus()
            return
        }

        // Validation - check if content is empty
        if (noteContent.isEmpty()) {
            Toast.makeText(
                this,
                getString(R.string.error_empty_content),
                Toast.LENGTH_SHORT
            ).show()
            editTextNoteContent.requestFocus()
            return
        }

        // Create new note
        val note = Note(
            title = noteTitle,
            content = noteContent
        )

        // Save to current storage
        val saved = StorageManager.getStorage().saveNote(note)

        if (saved) {
            Toast.makeText(
                this,
                getString(R.string.note_saved),
                Toast.LENGTH_SHORT
            ).show()
            finish()
        } else {
            Toast.makeText(
                this,
                "Error saving note. Please try again.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    /**
     * Handle back button in action bar
     */
    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}