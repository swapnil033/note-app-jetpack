package com.swapnil.noteapp.feature_note.data.repository

import com.swapnil.noteapp.feature_note.domain.model.Note
import com.swapnil.noteapp.feature_note.domain.repository.NoteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeNoteRepositoryImpl: NoteRepository {

    private val noteList = mutableListOf<Note>()
    override fun getNotes(): Flow<List<Note>> {
        return flow { emit(noteList) }
    }

    override suspend fun getNoteById(id: Int): Note? {
        return noteList.find { it.id == id }
    }

    override suspend fun insertNote(note: Note) {
        noteList.add(note)
    }

    override suspend fun deleteNote(note: Note) {
        noteList.remove(note)
    }
}