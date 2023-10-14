package com.swapnil.noteapp.feature_note.data.data_source

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.swapnil.noteapp.feature_note.domain.model.Note
import com.swapnil.noteapp.feature_note.domain.util.Converters


@Database(
    entities = [Note::class],
    version = 1
)
abstract class NoteDatabase: RoomDatabase() {

    abstract val dao: NoteDao

    companion object{
        val DATABASE_NAME = "note_db"
    }

}