package com.adrino.getin.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.zIndex
import coil.compose.AsyncImage
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.adrino.getin.R
import com.adrino.getin.data.model.Event

@Composable
fun EventWallpaperHeader(
    event: Event,
    modifier: Modifier = Modifier,
    height: Dp
) {
    var composableHeight by remember { mutableFloatStateOf(0f) }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(height)
            .zIndex(0f)
            .onSizeChanged { size ->
                composableHeight = size.height.toFloat()
            }
    ) {
        SubcomposeAsyncImage (
            model = ImageRequest.Builder(LocalContext.current)
                .data(event.eventWallpaper)
                .crossfade(true)
                .build(),
            contentDescription = event.eventName,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize(),
            error = {
                Image(
                    painter = painterResource(R.color.black),
                    contentDescription = stringResource(R.string.error),
                    contentScale = ContentScale.Crop
                )
            },
            loading = {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(shimmerBrush())
                )
            }
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            MaterialTheme.colorScheme.background.copy(0.3f),
                            MaterialTheme.colorScheme.background
                        ),
                        startY = 0f,
                        endY = 900f
                    )
                )
        )
    }
}

