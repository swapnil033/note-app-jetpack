package com.swapnil.noteapp.feature_note.domain.use_case

import com.google.common.truth.Truth.assertThat
import com.swapnil.noteapp.feature_note.data.repository.FakeNoteRepositoryImpl
import com.swapnil.noteapp.feature_note.domain.model.Note
import com.swapnil.noteapp.feature_note.domain.model.NoteException
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.Test

class AddNoteUseCaseTest{

    private lateinit var addNote: AddNoteUseCase
    private lateinit var fakeNoteRepository: FakeNoteRepositoryImpl

    @Before
    fun setup(){
        fakeNoteRepository = FakeNoteRepositoryImpl()
        addNote = AddNoteUseCase(fakeNoteRepository)
    }

    @Test
    fun `Get empty title exception`(){
        val note = Note(title = "", content = "qwerty", timestamp = 123, color = 1)
        val expected: NoteException = assertThrows(NoteException::class.java) { runBlocking { addNote(note) } }
        assertThat(expected).hasMessageThat().isEqualTo("Title is empty")
    }
    @Test
    fun `Get empty content exception`(){
        val note = Note(title = "qwerty", content = "", timestamp = 123, color = 1)
        val expected: NoteException = assertThrows(NoteException::class.java) { runBlocking { addNote(note) } }
        assertThat(expected).hasMessageThat().isEqualTo("Content is empty")
    }
    @Test
    fun `Get no exception`(){
        val note = Note(title = "qwerty", content = "qwerty", timestamp = 123, color = 1, id = 2)
        runBlocking {
            addNote(note)

            fakeNoteRepository.getNotes().collect{ notes ->
                var check = false
                notes.forEach {
                    if (note.id!! == it.id!!) {
                        check = true
                    }
                }

                assertThat(check).isEqualTo(true)
            }
        }
    }

}