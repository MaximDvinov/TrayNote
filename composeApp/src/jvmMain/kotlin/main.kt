import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.window.WindowDraggableArea
import androidx.compose.material3.FloatingActionButtonDefaults.smallShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.*
import com.dvinov.traynote.AppContent
import com.dvinov.traynote.db.Note
import com.dvinov.traynote.di.appModule
import com.dvinov.traynote.repositories.NoteRepository
import com.dvinov.traynote.theme.AppTheme
import compose.icons.FeatherIcons
import compose.icons.feathericons.FilePlus
import compose.icons.feathericons.Minus
import compose.icons.feathericons.Square
import compose.icons.feathericons.X
import kotlinx.coroutines.flow.map
import org.koin.core.context.startKoin
import java.awt.Dimension

val koin = startKoin {
    modules(appModule)
}

fun main() = application {
    var isVisible by remember { mutableStateOf(true) }
    var isCreate by remember { mutableStateOf(false) }
    var openNote by remember { mutableStateOf<Note?>(null) }
    val noteRepository = koin.koin.get<NoteRepository>()
    val notes by noteRepository.getAllNotes().map { it.filter { it.isPinned == true } }
        .collectAsState(initial = emptyList())
    val windowState = rememberWindowState(size = DpSize(1000.dp, 800.dp))

    Window(
        onCloseRequest = {
            isVisible = false;
            isCreate = false
            openNote = null
        },
        state = windowState,
        icon = rememberVectorPainter(FeatherIcons.FilePlus),
        visible = isVisible,
        transparent = true,
        undecorated = true,
        title = "TrayNote",
    ) {
        window.minimumSize = Dimension(600, 400)
        window.isTransparent = true

        AppTheme {
            Column(
                modifier = Modifier
                    .clip(if (windowState.placement == WindowPlacement.Maximized) RectangleShape else smallShape)
                    .border(
                        width = (0.5).dp,
                        color = MaterialTheme.colorScheme.secondaryContainer,
                        shape = if (windowState.placement == WindowPlacement.Maximized) RectangleShape else smallShape
                    )
                    .background(color = MaterialTheme.colorScheme.secondaryContainer)
            ) {
                window.isResizable = windowState.placement != WindowPlacement.Maximized
                if (windowState.placement != WindowPlacement.Maximized) {
                    WindowDraggableArea {
                        AppBar(windowState)
                    }
                } else {
                    AppBar(windowState)
                }
                AppContent(modifier = Modifier.weight(1f), isCreate, openNote)

            }
        }

    }

    if (!isVisible) {
        Tray(
            icon = rememberVectorPainter(FeatherIcons.FilePlus),
            tooltip = "TrayNote",
            onAction = { isVisible = true },
            menu = {
                notes.forEach {
                    Item(it.title, onClick = {
                        openNote = it
                        isVisible = true
                    })
                }

                Item("Создать", onClick = {
                    isCreate = true
                    isVisible = true
                })

                Item("Закрыть", onClick = {
                    exitApplication()
                })
            }
        )
    }


}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ApplicationScope.AppBar(windowState: WindowState) {
    var isActive by remember {
        mutableStateOf(false)
    }

    Row(
        modifier = Modifier.fillMaxWidth().background(color = MaterialTheme.colorScheme.secondaryContainer.copy(0.5f))
            .padding(top = 8.dp, start = 8.dp, end = 8.dp, bottom = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp, Alignment.End),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = FeatherIcons.FilePlus,
            modifier = Modifier.size(18.dp),
            contentDescription = "icon",
            tint = MaterialTheme.colorScheme.onSecondary
        )

        Text(
            "Меню",
            modifier = Modifier
                .clip(MaterialTheme.shapes.small)
                .background(
                    color = if (isActive) MaterialTheme.colorScheme.secondary.copy(0.3f) else Color.Transparent
                )
                .clickable{

                }
                .onPointerEvent(PointerEventType.Enter) { isActive = true }
                .onPointerEvent(PointerEventType.Exit) { isActive = false }
                .padding(vertical = 4.dp, horizontal = 10.dp),
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.weight(1f))

        WindowControlButton(
            background = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f),
            iconColor = MaterialTheme.colorScheme.onSecondary,
            icon = FeatherIcons.Minus
        ) {
            windowState.isMinimized = !windowState.isMinimized
        }

        WindowControlButton(
            background = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f),
            iconColor = MaterialTheme.colorScheme.onSecondary,
            icon = FeatherIcons.Square
        ) {
            windowState.placement = if (windowState.placement == WindowPlacement.Maximized) {
                WindowPlacement.Floating
            } else {
                WindowPlacement.Maximized
            }
        }

        WindowControlButton(
            icon = FeatherIcons.X,
            background = MaterialTheme.colorScheme.errorContainer,
            iconColor = MaterialTheme.colorScheme.error,
            onClick = ::exitApplication
        )
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun WindowControlButton(
    background: Color = MaterialTheme.colorScheme.primary,
    contentPadding: PaddingValues = PaddingValues(4.dp),
    shape: RoundedCornerShape = RoundedCornerShape(4.dp),
    icon: ImageVector,
    iconColor: Color = MaterialTheme.colorScheme.primary,
    contentDescription: String = "",
    onClick: () -> Unit,
) {
    var isActive by remember { mutableStateOf(false) }
    val colorBackground by animateColorAsState(
        if (isActive) background else Color.Transparent
    )
    Box(
        modifier = Modifier.size(24.dp)
            .clip(shape)
            .background(colorBackground)
            .onPointerEvent(PointerEventType.Enter) { isActive = true }
            .onPointerEvent(PointerEventType.Exit) { isActive = false }
            .clickable(onClick = onClick)
            .padding(contentPadding),
    ) {
        Icon(
            icon,
            contentDescription = contentDescription,
            tint = iconColor
        )
    }
}
