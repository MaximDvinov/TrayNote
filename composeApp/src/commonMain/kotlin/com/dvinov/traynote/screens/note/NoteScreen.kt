package com.dvinov.traynote.screens.note

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import cafe.adriel.voyager.core.screen.uniqueScreenKey
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.dvinov.traynote.ObserveAsEvents
import com.dvinov.traynote.navigation.NavigationEvent
import com.dvinov.traynote.db.Note
import com.dvinov.traynote.screens.note.components.RichTextStyleRow
import com.mohamedrejeb.richeditor.model.rememberRichTextState
import com.mohamedrejeb.richeditor.ui.BasicRichTextEditor
import compose.icons.FeatherIcons
import compose.icons.feathericons.Trash

class NoteScreen(val note: Note?) : Screen {
    override val key: ScreenKey
        get() = note?.id?.toString() ?: "new"

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
    Scaffold { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 32.dp, start = 40.dp, end = 40.dp, bottom = 32.dp),
            contentAlignment = Alignment.TopCenter
        ) {
            Column(
                modifier = Modifier
                    .widthIn(max = 900.dp)
                    .heightIn(max = 700.dp)
                    .fillMaxWidth()
            ) {
                Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    TextField(
                        modifier = Modifier.padding(bottom = 16.dp, start = 16.dp, end = 16.dp).weight(1f),
                        text = state.title,
                        onTextChange = { onEvent(NoteScreenEvent.OnNoteTitleChange(it)) },
                        placeholder = "Заголовок...",
                        style = MaterialTheme.typography.titleMedium.copy(
                            color = MaterialTheme.colorScheme.onBackground,
                            fontSize = 18.sp
                        )
                    )

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
                }


                if (state.content != null) {
                    RichTextField(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(MaterialTheme.shapes.large)
                            .background(MaterialTheme.colorScheme.secondaryContainer)
                            .padding(16.dp),
                        text = state.content,
                        onEvent = onEvent,
                        placeholder = "Текст заметки...",
                        style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onBackground)
                    )
                }

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
                if (text.isBlank()) {
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
    style: TextStyle = MaterialTheme.typography.bodyMedium,
    placeholder: String = "",
    onEvent: (NoteScreenEvent) -> Unit,
    maxLines: Int = Int.MAX_VALUE,
) {
    val state = rememberRichTextState()

    LaunchedEffect(text) {
        if (state.annotatedString.isEmpty() && text.isNotEmpty()) state.setHtml(text)
    }

    LaunchedEffect(state.annotatedString) {
        onEvent(NoteScreenEvent.OnNoteContentChange(state.toHtml()))
    }

    Column(modifier = modifier) {
        BasicRichTextEditor(
            modifier = Modifier.fillMaxWidth().weight(1f),
            state = state,
            textStyle = style,
            cursorBrush = SolidColor(MaterialTheme.colorScheme.onBackground),
            decorationBox = { innerTextField ->
                Box {
                    if (state.annotatedString.isEmpty()) {
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
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            RichTextStyleRow(
                modifier = Modifier
                    .weight(1f),
                state = state
            )

            Box(
                Modifier
                    .height(32.dp)
                    .clip(MaterialTheme.shapes.large).clickable {
                        onEvent(NoteScreenEvent.OnNoteSave)
                    }
                    .background(MaterialTheme.colorScheme.primaryContainer)
                    .padding(horizontal = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Сохранить",
                    style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onSecondaryContainer)
                )
            }
        }

    }


}