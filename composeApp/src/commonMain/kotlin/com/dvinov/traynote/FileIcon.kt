package com.dvinov.traynote

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.runtime.Composable
import androidx.compose.foundation.Image
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.group
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp


private var _FileIcon: ImageVector? = null

public val FileIcon: ImageVector
    get() {
        if (_FileIcon != null) {
            return _FileIcon!!
        }
        _FileIcon = ImageVector.Builder(
            name = "FileIcon",
            defaultWidth = 48.dp,
            defaultHeight = 48.dp,
            viewportWidth = 48f,
            viewportHeight = 48f
        ).apply {
            path(
                fill = SolidColor(Color(0xFF293038)),
                fillAlpha = 1.0f,
                stroke = null,
                strokeAlpha = 1.0f,
                strokeLineWidth = 1.0f,
                strokeLineCap = StrokeCap.Butt,
                strokeLineJoin = StrokeJoin.Miter,
                strokeLineMiter = 1.0f,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(8f, 0f)
                horizontalLineTo(40f)
                arcTo(8f, 8f, 0f, isMoreThanHalf = false, isPositiveArc = true, 48f, 8f)
                verticalLineTo(40f)
                arcTo(8f, 8f, 0f, isMoreThanHalf = false, isPositiveArc = true, 40f, 48f)
                horizontalLineTo(8f)
                arcTo(8f, 8f, 0f, isMoreThanHalf = false, isPositiveArc = true, 0f, 40f)
                verticalLineTo(8f)
                arcTo(8f, 8f, 0f, isMoreThanHalf = false, isPositiveArc = true, 8f, 0f)
                close()
            }
            group {
                path(
                    fill = SolidColor(Color(0xFFFFFFFF)),
                    fillAlpha = 1.0f,
                    stroke = null,
                    strokeAlpha = 1.0f,
                    strokeLineWidth = 1.0f,
                    strokeLineCap = StrokeCap.Butt,
                    strokeLineJoin = StrokeJoin.Miter,
                    strokeLineMiter = 1.0f,
                    pathFillType = PathFillType.EvenOdd
                ) {
                    moveTo(32.0306f, 19.7194f)
                    lineTo(26.7806f, 14.4694f)
                    curveTo(26.6399f, 14.3288f, 26.449f, 14.2498f, 26.25f, 14.25f)
                    horizontalLineTo(17.25f)
                    curveTo(16.4216f, 14.25f, 15.75f, 14.9216f, 15.75f, 15.75f)
                    verticalLineTo(32.25f)
                    curveTo(15.75f, 33.0784f, 16.4216f, 33.75f, 17.25f, 33.75f)
                    horizontalLineTo(30.75f)
                    curveTo(31.5784f, 33.75f, 32.25f, 33.0784f, 32.25f, 32.25f)
                    verticalLineTo(20.25f)
                    curveTo(32.2502f, 20.051f, 32.1712f, 19.8601f, 32.0306f, 19.7194f)
                    verticalLineTo(19.7194f)
                    close()
                    moveTo(27f, 16.8103f)
                    lineTo(29.6897f, 19.5f)
                    horizontalLineTo(27f)
                    verticalLineTo(16.8103f)
                    close()
                    moveTo(30.75f, 32.25f)
                    horizontalLineTo(17.25f)
                    verticalLineTo(15.75f)
                    horizontalLineTo(25.5f)
                    verticalLineTo(20.25f)
                    curveTo(25.5f, 20.6642f, 25.8358f, 21f, 26.25f, 21f)
                    horizontalLineTo(30.75f)
                    verticalLineTo(32.25f)
                    verticalLineTo(32.25f)
                    close()
                }
            }
        }.build()
        return _FileIcon!!
    }

