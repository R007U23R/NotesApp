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

    // Track if we're editing an existing note
    private var isEditMode = false
    private var editingNoteId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_note)

        // Initialize views
        initializeViews()

        // Check if we're editing an existing note
        checkForEditMode()

        // Set activity title based on mode
        supportActionBar?.title = if (isEditMode) "Edit Note" else getString(R.string.add_note_title)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

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
     * Check if we're in edit mode and populate fields if so
     */
    private fun checkForEditMode() {
        val note = intent.getSerializableExtra("note") as? Note
        if (note != null) {
            isEditMode = true
            editingNoteId = note.id
            editTextNoteTitle.setText(note.title)
            editTextNoteContent.setText(note.content)
            btnSave.text = "Update Note"
        }
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

        val storage = StorageManager.getStorage()

        if (isEditMode && editingNoteId != null) {
            // Delete old note and save updated version
            storage.deleteNote(editingNoteId!!)
            val updatedNote = Note(
                id = editingNoteId!!,
                title = noteTitle,
                content = noteContent
            )
            val saved = storage.saveNote(updatedNote)

            if (saved) {
                Toast.makeText(this, "Note updated successfully!", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Error updating note", Toast.LENGTH_SHORT).show()
            }
        } else {
            // Create new note
            val note = Note(
                title = noteTitle,
                content = noteContent
            )

            // Save to current storage
            val saved = storage.saveNote(note)

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
    }

    /**
     * Handle back button in action bar
     */
    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}