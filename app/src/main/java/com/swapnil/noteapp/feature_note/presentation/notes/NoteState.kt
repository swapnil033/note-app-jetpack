package com.swapnil.noteapp.feature_note.presentation.notes

import com.swapnil.noteapp.feature_note.domain.model.Note
import com.swapnil.noteapp.feature_note.domain.util.NoteOrder
import com.swapnil.noteapp.feature_note.domain.util.OrderType

data class NoteState(
    var notes: List<Note> = emptyList(),
    var noteOrder: NoteOrder = NoteOrder.Date(OrderType.Descending),
    var isSortingOptionsShown: Boolean = false
)
