package com.swapnil.noteapp.feature_note.presentation.util

sealed class Screen(val route: String){
    object NoteScreen: Screen("note_screen")
    object AddEditNote: Screen("add_edit_note")
}
