## Lab #4

### Activities interactions and storage

### You will need to use „Android Studio” IDE in order to create notes storing application. Application should meet the following requirements:

- Variables, functions and other elements should have meaningful names (e.g. txtName vs EditText1);
- Code should follow Java coding standards;

## Special part:
- There should be three Activities in the application:
  - MainActivity – displays a list of notes created by the user (use ListView element or ListActivity). Activity should have menu with two options: Add Note and Delete Note. Add Note option should invoke AddNoteActivity and Delete Note should invoke DeleteNoteActivity.
  - AddNoteActivity – activity for adding new note. It should accept note name and note content.
  - DeleteNoteActivity – activity for deleting existing notes. This activity should display a list of existing notes (by name) and allow user to select single note for deletion.

- There should be implemented two data storing solutions (e.g. shared preferences and files). Which options to use you can choose freely (Shared preferences, files (text, json, xml), SQLite, remote etc.). User should be able to switch from one storage option to another.
- Text (Label) for GUI elements should be taken from strings resource file.
- Application should validate the entered text (if string is empty). If the string is empty, then warning should be shown by using Toast element;


***This lab effectively simulated real-world app development where requirements evolve (adding edit functionality) and multiple implementation options exist (storage types)***

