package com.swapnil.noteapp.feature_note.presentation.notes

import android.annotation.SuppressLint
import androidx.compose.animation.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Sort
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.state.ToggleableState
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.swapnil.noteapp.feature_note.domain.util.NoteOrder
import com.swapnil.noteapp.feature_note.domain.util.OrderType
import com.swapnil.noteapp.feature_note.presentation.notes.components.NoteItem
import com.swapnil.noteapp.feature_note.presentation.notes.components.OrderSection
import com.swapnil.noteapp.feature_note.presentation.util.Screen
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun NoteScreen(
    navController: NavController,
    viewModel: NoteViewModel = hiltViewModel()
) {

    val state = viewModel.state.value
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate(Screen.AddEditNote.route)
                },
                backgroundColor = MaterialTheme.colors.primary,
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "add note")
            }
        },
        scaffoldState = scaffoldState
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Your Notes",
                    style = MaterialTheme.typography.h4
                )
                IconButton(onClick = {
                    viewModel.onEvent(NoteEvent.ShowHideSortingOptions)
                }) {
                    Icon(imageVector = Icons.Default.Sort, contentDescription = "sort")
                }
            }

            AnimatedVisibility(
                visible = state.isSortingOptionsShown,
                enter = fadeIn() + slideInVertically (),
                exit = fadeOut() + slideOutVertically ()
            ) {
                OrderSection(
                    noteOrder = state.noteOrder,
                    onOrderChange = {
                        viewModel.onEvent(NoteEvent.GetNotes(it))
                    }
                )
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ){
                items(state.notes){note ->

                    NoteItem(
                        note = note,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                       navController.navigate(
                                           Screen.AddEditNote.route + "?noteId=${note.id}&noteColor=${note.color}"
                                       )
                            },
                        onDeleteClick = {
                           viewModel.onEvent(NoteEvent.DeleteNote(note))

                            scope.launch {
                                val result = scaffoldState.snackbarHostState.showSnackbar(
                                    message = "Note deleted.",
                                    actionLabel = "Undo"
                                )

                                if (result == SnackbarResult.ActionPerformed){
                                    viewModel.onEvent(NoteEvent.RestoreNote)
                                }
                            }
                        }
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }

        }
    }
}