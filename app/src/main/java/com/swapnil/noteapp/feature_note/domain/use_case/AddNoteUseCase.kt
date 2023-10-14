package com.swapnil.noteapp.feature_note.domain.use_case

import com.swapnil.noteapp.feature_note.domain.model.Note
import com.swapnil.noteapp.feature_note.domain.model.NoteException
import com.swapnil.noteapp.feature_note.domain.repository.NoteRepository

class AddNoteUseCase(
    private val repository: NoteRepository
) {

    @Throws(NoteException::class)
    suspend operator fun invoke(note: Note){

        if (note.title.isBlank()) {
            throw NoteException("Title is empty")
        }
        if (note.content.isBlank()) {
            throw NoteException("Content is empty")
        }

        repository.insertNote(note)
    }

}