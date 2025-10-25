package com.example.notesapp

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    // Meaningful variable names (Requirement #1)
    private lateinit var listViewNotes: ListView
    private lateinit var txtEmptyState: TextView
    private lateinit var txtStorageType: TextView
    private lateinit var fabAddNote: FloatingActionButton
    private lateinit var notesAdapter: ArrayAdapter<String>
    private var notesList = mutableListOf<Note>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize StorageManager
        StorageManager.initialize(this)

        // Initialize views
        initializeViews()

        // Set up ListView
        setupListView()

        // Load notes
        loadNotes()
    }

    override fun onResume() {
        super.onResume()
        // Reload notes when returning to this activity
        loadNotes()
    }

    /**
     * Initialize view references with meaningful names
     */
    private fun initializeViews() {
        listViewNotes = findViewById(R.id.listViewNotes)
        txtEmptyState = findViewById(R.id.txtEmptyState)
        txtStorageType = findViewById(R.id.txtStorageType)

        // Initialize FAB and set click listener
        fabAddNote = findViewById(R.id.fabAddNote)
        fabAddNote.setOnClickListener {
            val intent = Intent(this, AddNoteActivity::class.java)
            startActivity(intent)
        }

        // Update storage type display
        updateStorageTypeDisplay()
    }

    /**
     * Set up ListView with adapter
     */
    private fun setupListView() {
        notesAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_2,
            android.R.id.text1,
            mutableListOf<String>()
        )
        listViewNotes.adapter = notesAdapter

        // Set click listener for viewing notes
        listViewNotes.setOnItemClickListener { _, _, position, _ ->
            if (position < notesList.size) {
                val note = notesList[position]
                showNoteDetails(note)
            }
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
            listViewNotes.visibility = View.GONE
            txtEmptyState.visibility = View.VISIBLE
        } else {
            // Show notes list
            listViewNotes.visibility = View.VISIBLE
            txtEmptyState.visibility = View.GONE

            // Add notes to adapter with formatted display
            notesList.forEach { note ->
                val displayText = "${note.title}\n${note.getContentPreview()}"
                notesAdapter.add(displayText)
            }
        }

        notesAdapter.notifyDataSetChanged()
    }

    /**
     * Show note details in a Toast (or you could create a detail activity)
     */
    private fun showNoteDetails(note: Note) {
        val message = "${note.title}\n\n${note.content}"
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    /**
     * Update storage type display
     */
    private fun updateStorageTypeDisplay() {
        val storageType = StorageManager.getCurrentStorageType()
        txtStorageType.text = getString(R.string.current_storage, storageType)
    }

    /**
     * Create options menu
     */
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    /**
     * Handle menu item selection
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_add_note -> {
                // Launch AddNoteActivity
                val intent = Intent(this, AddNoteActivity::class.java)
                startActivity(intent)
                true
            }
            R.id.action_delete_note -> {
                // Launch DeleteNoteActivity
                if (notesList.isEmpty()) {
                    Toast.makeText(
                        this,
                        getString(R.string.no_notes_to_delete),
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    val intent = Intent(this, DeleteNoteActivity::class.java)
                    startActivity(intent)
                }
                true
            }
            R.id.action_switch_storage -> {
                // Switch storage type
                switchStorage()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    /**
     * Switch between storage implementations
     */
    private fun switchStorage() {
        StorageManager.switchStorage(this)
        updateStorageTypeDisplay()
        loadNotes()

        val newStorageType = StorageManager.getCurrentStorageType()
        Toast.makeText(
            this,
            getString(R.string.storage_switched, newStorageType),
            Toast.LENGTH_SHORT
        ).show()
    }
}