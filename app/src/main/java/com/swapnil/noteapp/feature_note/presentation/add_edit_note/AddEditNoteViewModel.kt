package com.swapnil.noteapp.feature_note.presentation.add_edit_note

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.swapnil.noteapp.feature_note.domain.model.Note
import com.swapnil.noteapp.feature_note.domain.model.NoteException
import com.swapnil.noteapp.feature_note.domain.use_case.NoteUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditNoteViewModel @Inject constructor(
    private val userCases: NoteUseCases,
    safeStateHandle: SavedStateHandle
): ViewModel(){

    private val _noteTitle = mutableStateOf(NoteTextFieldState(
        hint = "Enter title..."
    ))
    val noteTitle : State<NoteTextFieldState> = _noteTitle

    private val _noteContent = mutableStateOf(NoteTextFieldState(
        hint = "Enter content..."
    ))
    val noteContent : State<NoteTextFieldState> = _noteContent

    private val _noteColor = mutableStateOf(Note.noteColors.random().toArgb())
    val noteColor : State<Int> = _noteColor

    private var currentNoteId: Int? = null

    private val _uiEvent = MutableSharedFlow<UiEvent>()
    val uiEvent = _uiEvent

    init {
        safeStateHandle.get<Int>("noteId")?.let {noteId ->
            if (noteId == -1) return@let
            viewModelScope.launch {
                userCases.getNote(noteId)?.also { note ->
                    currentNoteId = note.id
                    _noteTitle.value = noteTitle.value.copy(
                        text = note.title,
                        showHint = false
                    )
                    _noteContent.value = _noteContent.value.copy(
                        text = note.content,
                        showHint = false
                    )
                    _noteColor.value = note.color
                }
            }
        }
    }

    fun onEvent(event: AddEditNoteEvent){
        when(event){
            is AddEditNoteEvent.ChangeContentFocus -> {
                _noteContent.value = _noteContent.value.copy(
                    showHint = !event.focus.isFocused && _noteContent.value.text.isBlank()
                )
            }
            is AddEditNoteEvent.ChangeTitleFocus -> {
                _noteTitle.value = _noteTitle.value.copy(
                    showHint = !event.focus.isFocused && _noteTitle.value.text.isBlank()
                )
            }
            is AddEditNoteEvent.ColorChanged -> {
                _noteColor.value = event.color
            }
            is AddEditNoteEvent.EnteredContent -> {
                _noteContent.value = noteContent.value.copy(
                    text = event.value,
                    showHint = false
                )
            }
            is AddEditNoteEvent.EnteredTitle -> {
                _noteTitle.value = noteTitle.value.copy(
                    text = event.value,
                    showHint = false
                )
            }
            AddEditNoteEvent.SaveNote -> {
                viewModelScope.launch {
                    try {
                        userCases.addNote.invoke(
                            Note(
                                title = noteTitle.value.text,
                                content = noteContent.value.text,
                                color = noteColor.value,
                                timestamp = System.currentTimeMillis(),
                                id = currentNoteId
                            )
                        )
                        _uiEvent.emit(UiEvent.SaveNote)

                    }catch (e: NoteException){
                        _uiEvent.emit(UiEvent.ShowSnackBar(message = e.message ?: "Note didn't save"))

                    }

                }
            }
        }
    }

    sealed class UiEvent{
        data class ShowSnackBar(val message: String): UiEvent()
        object SaveNote: UiEvent()
    }
}