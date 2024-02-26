package com.dvinov.traynote.screens.note.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material.icons.filled.ColorLens
import androidx.compose.material.icons.outlined.FormatBold
import androidx.compose.material.icons.outlined.FormatItalic
import androidx.compose.material.icons.outlined.FormatListBulleted
import androidx.compose.material.icons.outlined.FormatListNumbered
import androidx.compose.material.icons.outlined.FormatSize
import androidx.compose.material.icons.outlined.FormatStrikethrough
import androidx.compose.material.icons.outlined.FormatUnderlined
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.PopupProperties
import com.mohamedrejeb.richeditor.model.RichTextState
import com.seiko.imageloader.util.Logger.Companion.None
import java.awt.color.ColorSpace

val fontSizes = listOf(10.sp, 12.sp, 14.sp, 16.sp, 18.sp, 20.sp, 24.sp)

@Composable
fun RichTextStyleButton(
    onClick: () -> Unit,
    icon: ImageVector,
    tint: Color? = null,
    isSelected: Boolean = false,
) {
    val contentColor by animateColorAsState(
        if (isSelected) {
            MaterialTheme.colorScheme.onSecondary
        } else {
            MaterialTheme.colorScheme.onBackground
        }
    )

    val iconBackgroundColor by animateColorAsState(
        if (isSelected) {
            MaterialTheme.colorScheme.secondary
        } else {
            Color.Transparent
        }
    )

    IconButton(
        modifier = Modifier.size(40.dp).focusProperties { canFocus = false },
        onClick = onClick,
        colors = IconButtonDefaults.iconButtonColors(
            contentColor = contentColor,
        ),
    ) {
        Icon(
            icon,
            contentDescription = icon.name,
            tint = tint ?: LocalContentColor.current,
            modifier = Modifier.background(
                color = iconBackgroundColor, shape = CircleShape
            ).padding(6.dp)
        )
    }
}


@Composable
fun RichTextStyleRow(
    modifier: Modifier = Modifier,
    state: RichTextState,
) {
    val colors = listOf(
        MaterialTheme.colorScheme.onBackground,
        Color.Black,
        Color.White.copy(alpha = 0.4f),
        Color(0xFFfc4635),
        Color(0xFFfcde35),
        Color(0xFF32a84e),
        MaterialTheme.colorScheme.primary
    )

    LazyRow(
        verticalAlignment = Alignment.CenterVertically, modifier = modifier
    ) {
        item {
            RichTextStyleButton(
                onClick = {
                    state.toggleSpanStyle(
                        SpanStyle(
                            fontWeight = FontWeight.Bold
                        )
                    )
                },
                isSelected = state.currentSpanStyle.fontWeight == FontWeight.Bold,
                icon = Icons.Outlined.FormatBold
            )
        }

        item {
            RichTextStyleButton(
                onClick = {
                    state.toggleSpanStyle(
                        SpanStyle(
                            fontStyle = FontStyle.Italic
                        )
                    )
                },
                isSelected = state.currentSpanStyle.fontStyle == FontStyle.Italic,
                icon = Icons.Outlined.FormatItalic
            )
        }

        item {
            RichTextStyleButton(
                onClick = {
                    state.toggleSpanStyle(
                        SpanStyle(
                            textDecoration = TextDecoration.Underline
                        )
                    )
                },
                isSelected = state.currentSpanStyle.textDecoration?.contains(TextDecoration.Underline) == true,
                icon = Icons.Outlined.FormatUnderlined
            )
        }

        item {
            RichTextStyleButton(
                onClick = {
                    state.toggleSpanStyle(
                        SpanStyle(
                            textDecoration = TextDecoration.LineThrough
                        )
                    )
                },
                isSelected = state.currentSpanStyle.textDecoration?.contains(TextDecoration.LineThrough) == true,
                icon = Icons.Outlined.FormatStrikethrough
            )
        }

        item {
            Box {
                println("${state.currentSpanStyle.color}")
                var expanded by remember { mutableStateOf(false) }
                RichTextStyleButton(
                    onClick = {
                        expanded = !expanded
                    },
                    isSelected = false,
                    icon = Icons.Filled.ColorLens,
                    tint = if (!state.currentSpanStyle.color.colorSpace.isSrgb) Color.White else state.currentSpanStyle.color
                )
                DropdownMenu(
                    modifier = Modifier.clip(RoundedCornerShape(16.dp)),
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    colors.forEach {
                        RichTextStyleButton(
                            onClick = {
                                state.toggleSpanStyle(
                                    SpanStyle(
                                        color = it
                                    )
                                )
                            },
                            isSelected = state.currentSpanStyle.color == it,
                            icon = Icons.Filled.Circle,
                            tint = it
                        )
                    }
                }
            }

        }


        item {
            Box(
                Modifier.height(24.dp).width(1.dp).background(Color(0xFF393B3D))
            )
        }

        item {
            RichTextStyleButton(
                onClick = {
                    state.toggleUnorderedList()
                },
                isSelected = state.isUnorderedList,
                icon = Icons.Outlined.FormatListBulleted,
            )
        }

        item {
            RichTextStyleButton(
                onClick = {
                    state.toggleOrderedList()
                },
                isSelected = state.isOrderedList,
                icon = Icons.Outlined.FormatListNumbered,
            )
        }


        item {
            Box(
                Modifier.height(24.dp).width(1.dp).background(Color(0xFF393B3D))
            )
        }

        item {
            Box {
                var expanded by remember { mutableStateOf(false) }
                val value = state.currentSpanStyle.fontSize.value.toInt()
                Text(modifier = Modifier.width(66.dp).focusProperties { canFocus = false }
                    .clip(RoundedCornerShape(8.dp)).clickable {
                        expanded = !expanded
                    }.padding(10.dp),
                    text = if (value == 0) "16sp" else (value.toString() + "sp"),
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = MaterialTheme.colorScheme.onBackground,
                        fontSize = 18.sp,
                        textAlign = TextAlign.Center
                    ),
                    textAlign = TextAlign.Center
                )
                DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                    fontSizes.forEach {
                        DropdownMenuItem(text = { Text(it.value.toInt().toString() + "sp") },
                            onClick = {
                                state.toggleSpanStyle(
                                    SpanStyle(
                                        fontSize = it
                                    )
                                )
                            })
                    }
                }
            }
        }

    }
}