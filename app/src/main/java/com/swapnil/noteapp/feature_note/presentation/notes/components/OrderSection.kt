package com.swapnil.noteapp.feature_note.presentation.notes.components

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.swapnil.noteapp.feature_note.domain.util.NoteOrder
import com.swapnil.noteapp.feature_note.domain.util.OrderType

@Composable
fun OrderSection(
    noteOrder: NoteOrder,
    modifier: Modifier = Modifier,
    onOrderChange: (NoteOrder) -> Unit
) {

    Column(
        modifier = modifier,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            DefaultRadioButton(
                title = "Title",
                selected = noteOrder is NoteOrder.Title,
                onSelected = {
                    onOrderChange(NoteOrder.Title(noteOrder.orderType))
                }
            )
            Spacer(modifier = Modifier.width(5.dp))
            DefaultRadioButton(
                title = "Date",
                selected = noteOrder is NoteOrder.Date,
                onSelected = {
                    onOrderChange(NoteOrder.Date(noteOrder.orderType))
                }
            )
            Spacer(modifier = Modifier.width(5.dp))
            DefaultRadioButton(
                title = "Color",
                selected = noteOrder is NoteOrder.Color,
                onSelected = {
                    onOrderChange(NoteOrder.Color(noteOrder.orderType))
                }
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            DefaultRadioButton(
                title = "Ascending",
                selected = noteOrder.orderType is OrderType.Ascending,
                onSelected = {
                    onOrderChange(noteOrder.copy(OrderType.Ascending))
                }
            )
            Spacer(modifier = Modifier.width(5.dp))
            DefaultRadioButton(
                title = "Descending",
                selected = noteOrder.orderType is OrderType.Descending,
                onSelected = {
                    onOrderChange(noteOrder.copy(OrderType.Descending))
                }
            )
        }
    }

}