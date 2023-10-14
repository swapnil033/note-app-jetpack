package com.swapnil.noteapp.di

import android.app.Application
import androidx.room.Room
import androidx.room.RoomDatabase
import com.swapnil.noteapp.feature_note.data.data_source.NoteDatabase
import com.swapnil.noteapp.feature_note.data.repository.NoteRepositoryImpl
import com.swapnil.noteapp.feature_note.domain.repository.NoteRepository
import com.swapnil.noteapp.feature_note.domain.use_case.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideNoteDatabase(
        app: Application
    ): NoteDatabase {
        return Room.databaseBuilder(
            app,
            NoteDatabase::class.java,
            NoteDatabase.DATABASE_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun provideRepository(
        db: NoteDatabase
    ): NoteRepository{
        return NoteRepositoryImpl(db.dao)
    }

    @Provides
    @Singleton
    fun noteUseCases(
        repository: NoteRepository
    ): NoteUseCases{
        return NoteUseCases(
            getNotes = GetNotesUseCase(repository),
            deleteNote = DeleteNoteUseCase(repository),
            addNote = AddNoteUseCase(repository),
            getNote = GetNoteUseCase(repository)
        )
    }

}