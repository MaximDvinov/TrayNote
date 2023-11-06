import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.window.Tray
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.dvinov.traynote.App
import com.dvinov.traynote.di.appModule
import com.dvinov.traynote.screens.note.NoteScreen
import compose.icons.FeatherIcons
import compose.icons.feathericons.FilePlus
import org.koin.core.context.startKoin

val koin = startKoin {
    modules(appModule)
}

fun main() = application {
    var isVisible by remember { mutableStateOf(true) }
    var isCreate by remember { mutableStateOf(false) }

    Window(
        onCloseRequest = {
            isVisible = false;

        },
        visible = isVisible,
        title = "TrayNote",
    ) {
        App(isCreate)
    }

    if (!isVisible) {
        Tray(
            icon = rememberVectorPainter(FeatherIcons.FilePlus),
            tooltip = "TrayNote",
            onAction = { isVisible = true },
            menu = {
                Item("Создать", onClick = {
                    isVisible = true
                    isCreate = true
                })
            },
        )
    }
}