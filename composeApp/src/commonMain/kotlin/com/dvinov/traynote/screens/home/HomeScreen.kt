package com.dvinov.traynote.screens.home

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.IconButton
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.dvinov.traynote.ObserveAsEvents
import com.dvinov.traynote.dateTimeFormat
import com.dvinov.traynote.db.Note
import com.dvinov.traynote.navigation.NavigationEvent
import com.dvinov.traynote.screens.note.NoteScreen
import com.dvinov.traynote.screens.note.NoteScreenEvent
import com.dvinov.traynote.screens.note.TextField
import compose.icons.FeatherIcons
import compose.icons.feathericons.ArrowLeft
import compose.icons.feathericons.Plus
import compose.icons.feathericons.Search
import compose.icons.feathericons.Trash
import compose.icons.feathericons.X

class HomeScreen : Screen {
    @Composable
    override fun Content() {
        val screenModel = getScreenModel<HomeScreenModel>()
        val state by screenModel.state.collectAsState()
        val navigator = LocalNavigator.currentOrThrow

        ObserveAsEvents(screenModel.navigationEventFlow) {
            when (it) {
                NavigationEvent.NavigateBack -> {}
                is NavigationEvent.NavigateToNote -> {
                    navigator.push(NoteScreen(it.note))
                }
            }
        }

        HomeScreenContent(state) {
            screenModel.onEvent(it)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenContent(state: HomeState, onEvent: (HomeEvent) -> Unit) {
    Scaffold(modifier = Modifier, topBar = {
        SearchBar(
            modifier = Modifier.padding(16.dp).clip(MaterialTheme.shapes.extraLarge),
            query = state.query ?: "",
            windowInsets = WindowInsets(16.dp),
            onQueryChange = { onEvent(HomeEvent.OnSearchChange(it)) },
            active = state.query != null,
            onSearch = {},
            onActiveChange = { onEvent(HomeEvent.OnSearchChange("")) },
            leadingIcon = {
                IconButton(onClick = {}) {
                    Icon(FeatherIcons.Search, "")
                }
            },
            trailingIcon = {
                IconButton(onClick = { onEvent(HomeEvent.OnSearchChange(null)) }) {
                    Icon(FeatherIcons.X, "")
                }
            }
        ) {
            LazyVerticalGrid(
                modifier = Modifier.fillMaxSize(),
                columns = GridCells.Adaptive(minSize = 250.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(16.dp),
            ) {
                items(state.searchedList ?: listOf()) { note ->
                    NoteItem(note = note) { onEvent(HomeEvent.OnNoteClick(note)) }
                }
            }
        }
    }, floatingActionButton = {
        FloatingActionButton(
            onClick = {
                onEvent(HomeEvent.CreateNewNote)
            },
            modifier = Modifier,
        ) {
            Icon(FeatherIcons.Plus, "")
        }
    }) { padding ->
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 250.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(
                top = padding.calculateTopPadding(),
                start = 16.dp,
                end = 16.dp,
                bottom = padding.calculateBottomPadding()
            ),
        ) {
            items(state.list) { note ->
                NoteItem(note = note) { onEvent(HomeEvent.OnNoteClick(note)) }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun NoteTopBar(
    state: HomeState,
    onEvent: (HomeEvent) -> Unit,
) {
    TopAppBar(title = {
        AnimatedContent(state.query == null) {
            if (it) {
                Text(
                    "Заметки",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
            } else {

                TextField(
                    modifier = Modifier.padding(start = 16.dp),
                    text = state.query ?: "",
                    onTextChange = {
                        onEvent(HomeEvent.OnSearchChange(it))
                    },
                    style = MaterialTheme.typography.titleLarge.copy(
                        color = MaterialTheme.colorScheme.onBackground
                    ),
                    placeholder = "Поиск",
                )

            }
        }
    }, actions = {
        Spacer(Modifier.weight(1f))
        IconButton(onClick = {
            if (state.query != null) {
                onEvent(HomeEvent.OnSearchChange(null))
            } else {
                onEvent(HomeEvent.OnSearchChange(""))
            }

        }) {
            AnimatedContent(state.query == null) {
                if (it) {
                    Icon(
                        imageVector = FeatherIcons.Search, ""
                    )
                } else {
                    Column {
                        Icon(
                            imageVector = FeatherIcons.X, ""
                        )
                    }

                }
            }

        }
    })
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteItem(note: Note, onClick: () -> Unit = {}) {
    Card(onClick = onClick) {
        Column(
            modifier = Modifier.padding(8.dp), verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            Text(text = note.title, style = MaterialTheme.typography.titleMedium)
            Text(text = note.content, style = MaterialTheme.typography.bodyMedium)
            Text(
                text = note.updatedAt.dateTimeFormat(), style = MaterialTheme.typography.labelMedium
            )
        }

    }
}
