package com.dvinov.traynote.screens.home

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.IconButton
import androidx.compose.material3.*
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.dvinov.traynote.*
import com.dvinov.traynote.db.Note
import com.dvinov.traynote.navigation.NavigationEvent
import com.dvinov.traynote.screens.note.NoteScreen
import com.dvinov.traynote.screens.note.TextField
import com.mohamedrejeb.richeditor.model.rememberRichTextState
import compose.icons.FeatherIcons
import compose.icons.feathericons.Plus
import compose.icons.feathericons.Search
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
    Box(
        modifier = Modifier.fillMaxSize().padding(top = 32.dp, start = 40.dp, end = 40.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.widthIn(max = 900.dp).fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            NoteSearchBar(state, onEvent)
            Spacer(Modifier.height(12.dp))
            NotesList(
                Modifier.fillMaxSize(),
                state.searchedList ?: listOf(),
                onEvent = onEvent
            )
        }
    }
}

@Composable
private fun NoteSearchBar(
    state: HomeState,
    onEvent: (HomeEvent) -> Unit,
) {
    val focusRequester = remember { FocusRequester() }

    Row(
        modifier = Modifier
            .clip(MaterialTheme.shapes.large)
            .background(MaterialTheme.colorScheme.secondaryContainer)
            .fillMaxWidth()
            .height(intrinsicSize = IntrinsicSize.Min),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = { onEvent(HomeEvent.OnSearchChange("")) }) {
            Icon(
                FeatherIcons.Search, "", tint = MaterialTheme.colorScheme.onSecondaryContainer
            )
        }

        TextField(
            modifier = Modifier.fillMaxWidth().focusRequester(focusRequester).padding(horizontal = 8.dp),
            text = state.query ?: "",
            onTextChange = {
                onEvent(HomeEvent.OnSearchChange(it))
            },
            placeholder = "Поиск",
            style = MaterialTheme.typography.titleMedium.copy(color = MaterialTheme.colorScheme.onSecondaryContainer),
        )

    }
}

@Composable
fun TopBar(isTopLevel: Boolean, onClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 40.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "TrayNote",
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight(700)),
            modifier = Modifier.weight(1f)
        )

        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(MaterialTheme.shapes.large)
                .clickable(onClick = onClick)
                .background(MaterialTheme.colorScheme.secondaryContainer),
            contentAlignment = Alignment.Center
        ) {
            AnimatedContent(isTopLevel){
                if (it){
                    Icon(FeatherIcons.Plus, "")
                } else {
                    Icon(FeatherIcons.X, "")
                }
            }

        }
    }
}

@Composable
private fun NotesList(
    modifier: Modifier = Modifier,
    list: List<Note>,
    onEvent: (HomeEvent) -> Unit,
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items(list) { note ->
            NoteItem(note = note) { onEvent(HomeEvent.OnNoteClick(note)) }
        }
    }
}

@Composable
fun NoteItem(note: Note, onClick: () -> Unit = {}) {
    val state = rememberRichTextState()
    state.setHtml(note.content)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.medium)
            .clickable(onClick = onClick)
            .padding(8.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(text = note.title, style = MaterialTheme.typography.titleMedium, maxLines = 1)
            Text(
                text = state.annotatedString,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
            )
            Text(
                text = note.updatedAt.dateTimeFormat(),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f)
            )
        }
    }
}