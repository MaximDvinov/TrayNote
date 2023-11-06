package com.dvinov.traynote

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.navigator.Navigator
import com.dvinov.traynote.home.HomeScreen
import com.dvinov.traynote.theme.AppTheme

@Composable
internal fun App() = AppTheme {
    AppTheme {
        AppContent()
    }
}

@Composable
fun AppContent() {
    Column(modifier = Modifier.fillMaxSize()) {
        Navigator(HomeScreen())
    }
}
