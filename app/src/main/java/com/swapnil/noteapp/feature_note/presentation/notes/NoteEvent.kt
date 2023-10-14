package com.swapnil.noteapp.feature_note.presentation.notes

import com.swapnil.noteapp.feature_note.domain.model.Note
import com.swapnil.noteapp.feature_note.domain.util.NoteOrder

sealed class NoteEvent{
    data class GetNotes(val noteOrder: NoteOrder): NoteEvent()
    data class DeleteNote(val note: Note): NoteEvent()
    object RestoreNote: NoteEvent()
    object ShowHideSortingOptions: NoteEvent()
}
