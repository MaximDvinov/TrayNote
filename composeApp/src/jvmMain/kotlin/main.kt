import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.window.Tray
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.dvinov.traynote.App
import compose.icons.FeatherIcons
import compose.icons.feathericons.FilePlus

fun main() = application {
    var isVisible by remember { mutableStateOf(true) }
    var isCreate by remember { mutableStateOf(false) }

    Window(
        onCloseRequest = { isVisible = false },
        visible = isVisible,
        title = "Counter",
    ) {
        App()
    }

    if (!isVisible) {
        Tray(
            icon = rememberVectorPainter(FeatherIcons.FilePlus),
            tooltip = "TrayNote",
            onAction = { isVisible = true },
            menu = {
                Item("Создать", onClick = {
                    isVisible = true
                })
            },
        )
    }
}