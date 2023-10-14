package com.swapnil.noteapp.feature_note.presentation.notes

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.swapnil.noteapp.feature_note.domain.model.Note
import com.swapnil.noteapp.feature_note.domain.use_case.NoteUseCases
import com.swapnil.noteapp.feature_note.domain.util.NoteOrder
import com.swapnil.noteapp.feature_note.domain.util.OrderType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteViewModel @Inject constructor (
    private val useCases: NoteUseCases,
) : ViewModel() {

    private val _state = mutableStateOf(NoteState())
    val state : State<NoteState> = _state

    private var _noteListJob: Job? = null

    private var _recentlyDeletedNote : Note? = null

    init{
        getNotes(NoteOrder.Date(OrderType.Descending))
    }

    fun onEvent(event: NoteEvent){
        when(event){
            is NoteEvent.DeleteNote -> deleteNote(event)
            is NoteEvent.GetNotes -> getNotes(event)
            NoteEvent.RestoreNote -> restoreNote()
            NoteEvent.ShowHideSortingOptions -> showHideSortingOptions()
        }
    }

    private fun showHideSortingOptions() {
        _state.value = state.value.copy(
            isSortingOptionsShown = !state.value.isSortingOptionsShown
        )
    }

    private fun restoreNote() {
        viewModelScope.launch {
            _recentlyDeletedNote?.let {
                useCases.addNote(it)
            }
        }
    }

    private fun getNotes(event: NoteEvent.GetNotes) {
        if (state.value.noteOrder::class == event.noteOrder::class &&
            state.value.noteOrder.orderType == event.noteOrder.orderType
        ) return
        getNotes(event.noteOrder)
    }

    private fun getNotes(noteOrder: NoteOrder) {
        _noteListJob?.cancel()
        _noteListJob = useCases.getNotes(noteOrder).onEach {
            _state.value = state.value.copy(
                notes = it,
                noteOrder = noteOrder
            )
        }.launchIn(viewModelScope)
    }

    private fun deleteNote(event: NoteEvent.DeleteNote) {
        viewModelScope.launch {
            useCases.deleteNote(event.note)
            _recentlyDeletedNote = event.note
        }
    }

}