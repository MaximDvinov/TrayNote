package com.dvinov.traynote.screens.home

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.IconButton
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DockedSearchBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarColors
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.Dp
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
import com.mohamedrejeb.richeditor.model.rememberRichTextState
import com.mohamedrejeb.richeditor.ui.material.RichText
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

@Composable
fun HomeScreenContent(state: HomeState, onEvent: (HomeEvent) -> Unit) {
    Scaffold(modifier = Modifier, topBar = {
        NoteSearchBar(state, onEvent)
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
        NotesList(
            Modifier.fillMaxSize(), PaddingValues(
                top = padding.calculateTopPadding(),
                bottom = padding.calculateTopPadding(),
                start = 16.dp,
                end = 16.dp
            ), state.searchedList ?: listOf(), onEvent = onEvent
        )
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun NoteSearchBar(
    state: HomeState,
    onEvent: (HomeEvent) -> Unit,
) {
    val focusRequester = remember { FocusRequester() }

    Row(
        modifier = Modifier
            .padding(16.dp)
            .clip(MaterialTheme.shapes.large)
            .background(MaterialTheme.colorScheme.secondaryContainer)
            .padding()
            .height(intrinsicSize = IntrinsicSize.Min),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AnimatedVisibility(visible = state.query != null) {
            TextField(
                modifier = Modifier.focusRequester(focusRequester)
                    .padding(horizontal = 16.dp),
                text = state.query ?: "",
                onTextChange = {
                    onEvent(HomeEvent.OnSearchChange(it))
                },
                placeholder = "Поиск",
                style = MaterialTheme.typography.titleLarge.copy(color = MaterialTheme.colorScheme.onSecondaryContainer),
            )
            LaunchedEffect(state.query != null) {
                focusRequester.requestFocus()
            }
        }

        AnimatedContent(state.query == null) {
            if (it) {
                IconButton(onClick = { onEvent(HomeEvent.OnSearchChange("")) }) {
                    Icon(
                        FeatherIcons.Search,
                        "",
                        tint = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                }
            } else {
                IconButton(onClick = { onEvent(HomeEvent.OnSearchChange(null)) }) {
                    Icon(FeatherIcons.X, "", tint = MaterialTheme.colorScheme.onSecondaryContainer)
                }
            }
        }
    }
}

@Composable
private fun NotesList(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    list: List<Note>,
    onEvent: (HomeEvent) -> Unit,
) {
    LazyVerticalGrid(
        modifier = modifier,
        columns = GridCells.FixedSize(350.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = contentPadding,
    ) {
        items(list) { note ->
            NoteItem(note = note) { onEvent(HomeEvent.OnNoteClick(note)) }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteItem(note: Note, onClick: () -> Unit = {}) {
    val state = rememberRichTextState()
    state.setHtml(note.content)
    Card(onClick = onClick) {
        Column(
            modifier = Modifier.padding(8.dp), verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
//            Text(text = note.title, style = MaterialTheme.typography.titleMedium)
            Text(
                text = state.annotatedString,
                style = MaterialTheme.typography.bodyLarge,
            )
            Text(
                text = note.updatedAt.dateTimeFormat(), style = MaterialTheme.typography.labelMedium
            )
        }

    }
}
