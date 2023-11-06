package com.dvinov.traynote.di

import com.dvinov.traynote.db.NoteDao
import com.dvinov.traynote.db.NoteDaoImpl
import com.dvinov.traynote.db.createDatabase
import org.koin.dsl.module

val appModule = module {
    single { createDatabase() }
    single<NoteDao> { NoteDaoImpl(get()) }
}