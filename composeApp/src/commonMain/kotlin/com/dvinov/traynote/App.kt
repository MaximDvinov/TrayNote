package com.dvinov.traynote

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.CurrentScreen
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.SlideTransition
import com.dvinov.traynote.db.Note
import com.dvinov.traynote.screens.home.HomeEvent
import com.dvinov.traynote.screens.home.HomeScreen
import com.dvinov.traynote.screens.home.TopBar
import com.dvinov.traynote.screens.note.NoteScreen
import com.dvinov.traynote.theme.AppTheme



@Composable
fun AppContent(modifier: Modifier, isCreate: Boolean, openNote: Note?) {
    var navigator: Navigator? by remember {
        mutableStateOf(null)
    }

    Scaffold(modifier = modifier, topBar = {
        Column {
            TopBar(isTopLevel = navigator?.lastItem is HomeScreen) {
                if (navigator?.lastItem is NoteScreen){
                    navigator?.pop()
                } else {
                    navigator?.push(NoteScreen(null))
                }
            }
            Spacer(
                Modifier.fillMaxWidth().height(1.dp)
                    .background(color = MaterialTheme.colorScheme.onBackground.copy(0.2f))
            )
        }

    }) { padding ->
        Column(modifier = Modifier.padding(padding).fillMaxSize()) {
            if (isCreate || openNote != null) {
                Navigator(listOf(HomeScreen(), NoteScreen(openNote))){
                    navigator = it
                    SlideTransition(it)
                }
            } else {
                Navigator(HomeScreen()){
                    navigator = it
                    SlideTransition(it)
                }
            }
        }
    }
}


