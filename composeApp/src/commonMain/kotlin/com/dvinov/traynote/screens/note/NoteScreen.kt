package com.dvinov.traynote.screens.note

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material.icons.rounded.Pin
import androidx.compose.material.icons.rounded.PushPin
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.dvinov.traynote.ObserveAsEvents
import com.dvinov.traynote.navigation.NavigationEvent
import compose.icons.FeatherIcons
import compose.icons.feathericons.ArrowLeft
import compose.icons.feathericons.Delete
import compose.icons.feathericons.Plus
import compose.icons.feathericons.Save
import compose.icons.feathericons.Trash
import com.dvinov.traynote.db.Note
import com.dvinov.traynote.screens.note.components.RichTextStyleRow
import com.mohamedrejeb.richeditor.model.rememberRichTextState
import com.mohamedrejeb.richeditor.ui.BasicRichTextEditor
import com.mohamedrejeb.richeditor.ui.material3.RichTextEditor
import com.mohamedrejeb.richeditor.ui.material3.RichTextEditorColors
import com.mohamedrejeb.richeditor.ui.material3.RichTextEditorDefaults
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class NoteScreen(val note: Note?) : Screen {
    @Composable
    override fun Content() {
        val screenModel = getScreenModel<NoteScreenModel>()
        val state by screenModel.noteState.collectAsState()
        val navigator = LocalNavigator.currentOrThrow

        ObserveAsEvents(screenModel.navigationEventFlow) {
            when (it) {
                is NavigationEvent.NavigateBack -> navigator.pop()
                is NavigationEvent.NavigateToNote -> {}
            }
        }

        LaunchedEffect(note?.id) {
            screenModel.noteInit(note)
        }


        NoteScreenContent(state) {
            screenModel.onEvent(it)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteScreenContent(state: NoteState, onEvent: (NoteScreenEvent) -> Unit) {
    val navigator = LocalNavigator.currentOrThrow
    Scaffold(topBar = {
        TopAppBar(navigationIcon = {
            IconButton(onClick = {
                navigator.pop()
            }) {
                Icon(
                    imageVector = FeatherIcons.ArrowLeft, ""
                )
            }
        }, title = {}, actions = {
            Spacer(Modifier.weight(1f))
            IconButton(onClick = {
                onEvent(NoteScreenEvent.OnNoteDelete)
            }) {
                Icon(
                    imageVector = FeatherIcons.Trash, ""
                )
            }

            IconButton(onClick = {
                onEvent(NoteScreenEvent.OnPinnedChange)
            }) {
                AnimatedContent(!state.isPinned) {
                    if (it) {
                        Icon(
                            imageVector = Icons.Rounded.FavoriteBorder, ""
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Filled.Favorite, ""
                        )
                    }
                }

            }
        })
    }, floatingActionButton = {
        FloatingActionButton(
            onClick = {
                onEvent(NoteScreenEvent.OnNoteSave)
            },
        ) {
            Icon(FeatherIcons.Save, "")
        }
    }) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            TextField(
                modifier = Modifier.padding(start = 16.dp, top = 0.dp, end = 16.dp).fillMaxWidth(),
                text = state.title,
                onTextChange = { onEvent(NoteScreenEvent.OnNoteTitleChange(it)) },
                placeholder = "Заголовок...",
                style = MaterialTheme.typography.titleLarge.copy(color = MaterialTheme.colorScheme.onBackground)
            )

            Box(
                Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
                    .height(1.3.dp)
            )

            if (state.content != null) {
                RichTextField(
                    modifier = Modifier.padding(start = 16.dp, end = 16.dp).fillMaxSize(),
                    text = state.content,
                    onTextChange = { onEvent(NoteScreenEvent.OnNoteContentChange(it)) },
                    placeholder = "Текст заметки...",
                    style = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onBackground)
                )
            }

        }
    }

}

@Composable
fun TextField(
    modifier: Modifier = Modifier,
    text: String,
    onTextChange: (String) -> Unit,
    style: TextStyle = MaterialTheme.typography.bodyMedium,
    placeholder: String = "",
    maxLines: Int = Int.MAX_VALUE,
) {
    BasicTextField(
        modifier = modifier,
        value = text,
        onValueChange = onTextChange,
        textStyle = style,
        cursorBrush = SolidColor(MaterialTheme.colorScheme.onBackground),
        decorationBox = { innerTextField ->
            Box {
                if (text.isEmpty()) {
                    Text(
                        text = placeholder,
                        style = style.copy(color = MaterialTheme.colorScheme.onBackground.copy(0.6f))
                    )
                }
                innerTextField()
            }
        },
        maxLines = maxLines,
        keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences)
    )
}

@Composable
fun RichTextField(
    modifier: Modifier = Modifier,
    text: String,
    onTextChange: (String) -> Unit,
    style: TextStyle = MaterialTheme.typography.bodyMedium,
    placeholder: String = "",
    maxLines: Int = Int.MAX_VALUE,
) {
    val state = rememberRichTextState()

    LaunchedEffect(text) {
        if (state.annotatedString.isBlank()) state.setHtml(text)
    }

    LaunchedEffect(state.annotatedString) {
        onTextChange(state.toHtml())

    }

    Column(modifier = modifier) {
        BasicRichTextEditor(
            modifier = Modifier.fillMaxWidth().weight(1f),
            state = state,
            textStyle = style,
            cursorBrush = SolidColor(MaterialTheme.colorScheme.onBackground),
            decorationBox = { innerTextField ->
                Box {
                    if (text.isEmpty()) {
                        Text(
                            text = placeholder,
                            style = style.copy(
                                color = MaterialTheme.colorScheme.onBackground.copy(
                                    0.6f
                                )
                            )
                        )
                    }
                    innerTextField()
                }
            },
        )
        RichTextStyleRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colorScheme.secondaryContainer)
                .padding(4.dp),
            state = state
        )
    }


}