package com.swapnil.noteapp.feature_note.presentation.add_edit_note

data class NoteTextFieldState(
    var text: String = "",
    var hint: String = "",
    var showHint: Boolean = true
)
