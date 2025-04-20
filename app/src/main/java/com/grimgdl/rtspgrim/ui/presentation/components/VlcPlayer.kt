package com.grimgdl.rtspgrim.ui.presentation.components

import android.text.Layout
import android.view.SurfaceHolder
import android.view.SurfaceView
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.viewinterop.AndroidView
import org.videolan.libvlc.Media
import org.videolan.libvlc.MediaPlayer

@Composable
fun VlcPlayerView(player: MediaPlayer, media: Media,modifier: Modifier = Modifier) {

    var bitrate by rememberSaveable { mutableIntStateOf(0) }


    Box{
        AndroidView(
            factory = { context ->
                SurfaceView(context).apply {
                    holder.addCallback(object : SurfaceHolder.Callback {
                        override fun surfaceCreated(holder: SurfaceHolder) {
                            player.vlcVout.setVideoSurface(holder.surface, holder)
                            player.vlcVout.attachViews()
                            player.aspectRatio = "16:9"
                            player.videoScale = MediaPlayer.ScaleType.SURFACE_BEST_FIT
                            player.vlcVout.setWindowSize(1280, 720)
                            player.media = media
                            media.release()
                            player.play()
                        }
                        override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {}
                        override fun surfaceDestroyed(holder: SurfaceHolder) {
                            player.vlcVout.detachViews()
                        }
                    })
                }

            },
            modifier = modifier.fillMaxWidth()
                .aspectRatio(16f/9f)
        )

        Text(text = "${bitrate}KB/s", color = Color.White, modifier = Modifier.align(alignment = Alignment.BottomEnd) )
    }



    player.setEventListener { event ->
        if (event is MediaPlayer.Event) {
            val bite = player.media?.stats?.demuxBitrate ?: 0f
            bitrate = (bite * 1000).toInt()
        }

    }



}