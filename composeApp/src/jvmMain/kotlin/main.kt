import androidx.compose.runtime.*
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.window.Tray
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.dvinov.traynote.App
import com.dvinov.traynote.db.Note
import com.dvinov.traynote.di.appModule
import com.dvinov.traynote.repositories.NoteRepository
import compose.icons.FeatherIcons
import compose.icons.feathericons.FilePlus
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

    val state = rememberWindowState()

    Window(
        onCloseRequest = {
            isVisible = false;
            isCreate = false
            openNote = null
        },
        visible = isVisible,
        title = "TrayNote",
    ) {
        window.minimumSize = Dimension(600, 400)

        App(isCreate, openNote)
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