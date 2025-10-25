package com.example.notesapp

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class DeleteNoteActivity : AppCompatActivity() {

    // Meaningful variable names (Requirement #1)
    private lateinit var listViewDeleteNotes: ListView
    private lateinit var txtNoNotes: TextView
    private lateinit var btnDelete: Button
    private lateinit var btnCancelDelete: Button
    private lateinit var notesAdapter: ArrayAdapter<String>

    private var notesList = mutableListOf<Note>()
    private var selectedPosition = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_delete_note)

        // Set activity title
        supportActionBar?.title = getString(R.string.delete_note_title)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Initialize views
        initializeViews()

        // Set up ListView
        setupListView()

        // Set up button listeners
        setupButtonListeners()

        // Load notes
        loadNotes()
    }

    /**
     * Initialize view references with meaningful names
     */
    private fun initializeViews() {
        listViewDeleteNotes = findViewById(R.id.listViewDeleteNotes)
        txtNoNotes = findViewById(R.id.txtNoNotes)
        btnDelete = findViewById(R.id.btnDelete)
        btnCancelDelete = findViewById(R.id.btnCancelDelete)
    }

    /**
     * Set up ListView with adapter for single choice mode
     */
    private fun setupListView() {
        notesAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_single_choice,
            mutableListOf<String>()
        )
        listViewDeleteNotes.adapter = notesAdapter
        listViewDeleteNotes.choiceMode = ListView.CHOICE_MODE_SINGLE

        // Set item click listener to track selection
        listViewDeleteNotes.setOnItemClickListener { _, _, position, _ ->
            selectedPosition = position
        }
    }

    /**
     * Set up button click listeners
     */
    private fun setupButtonListeners() {
        // Delete button
        btnDelete.setOnClickListener {
            deleteSelectedNote()
        }

        // Cancel button
        btnCancelDelete.setOnClickListener {
            finish()
        }
    }

    /**
     * Load notes from current storage
     */
    private fun loadNotes() {
        notesList.clear()
        notesList.addAll(StorageManager.getStorage().getAllNotes())

        // Update adapter
        notesAdapter.clear()

        if (notesList.isEmpty()) {
            // Show empty state
            listViewDeleteNotes.visibility = View.GONE
            txtNoNotes.visibility = View.VISIBLE
            btnDelete.isEnabled = false
        } else {
            // Show notes list
            listViewDeleteNotes.visibility = View.VISIBLE
            txtNoNotes.visibility = View.GONE
            btnDelete.isEnabled = true

            // Add note titles to adapter
            notesList.forEach { note ->
                notesAdapter.add(note.title)
            }
        }

        notesAdapter.notifyDataSetChanged()
    }

    /**
     * Delete the selected note
     * Requirement #4: Validate selection and show Toast
     */
    private fun deleteSelectedNote() {
        // Validation - check if a note is selected
        if (selectedPosition < 0 || selectedPosition >= notesList.size) {
            Toast.makeText(
                this,
                getString(R.string.error_no_selection),
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        // Get selected note
        val selectedNote = notesList[selectedPosition]

        // Delete from storage
        val deleted = StorageManager.getStorage().deleteNote(selectedNote.id)

        if (deleted) {
            Toast.makeText(
                this,
                getString(R.string.note_deleted),
                Toast.LENGTH_SHORT
            ).show()

            // Reset selection
            selectedPosition = -1

            // Reload notes
            loadNotes()

            // If no more notes, finish activity
            if (notesList.isEmpty()) {
                finish()
            }
        } else {
            Toast.makeText(
                this,
                "Error deleting note. Please try again.",
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