package com.dvinov.traynote.di

import com.dvinov.traynote.db.NoteDao
import com.dvinov.traynote.db.NoteDaoImpl
import com.dvinov.traynote.db.createDatabase
import com.dvinov.traynote.screens.home.HomeScreenModel
import com.dvinov.traynote.repositories.NoteRepository
import com.dvinov.traynote.repositories.NoteRepositoryImpl
import com.dvinov.traynote.screens.note.NoteScreenModel
import org.koin.dsl.module

val appModule = module {
    single { createDatabase() }
    single<NoteDao> { NoteDaoImpl(get()) }
    single<NoteRepository> { NoteRepositoryImpl(get()) }

    factory { HomeScreenModel(get()) }
    factory { NoteScreenModel(get()) }
}