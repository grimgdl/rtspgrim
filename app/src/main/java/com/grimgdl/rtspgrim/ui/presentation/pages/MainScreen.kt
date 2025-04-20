package com.grimgdl.rtspgrim.ui.presentation.pages

import androidx.compose.material3.Badge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.net.toUri
import com.grimgdl.rtspgrim.BuildConfig
import com.grimgdl.rtspgrim.ui.presentation.components.VlcPlayerView
import org.videolan.libvlc.LibVLC
import org.videolan.libvlc.Media
import org.videolan.libvlc.MediaPlayer


@Composable
fun MainScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current

    val libVlc = remember{ LibVLC(context, listOf(
        "--no-osd",                        // No mostrar overlays (títulos, etc.)
        "--no-video-title-show",          // Quitar título de video al inicio
        "--network-caching=150",          // Caché en red (milisegundos)
        "--live-caching=100",             // Caché para streaming en vivo (milisegundos)
        "--file-caching=150",             // Por si se cae a fallback file stream
        "--clock-jitter=0",               // Eliminar jitter buffer (mejora latencia)
        "--clock-synchro=0",              // No sincroniza con reloj local (menos delay)
        "--avcodec-hw=any",               // Habilita decodificación por hardware
        "--codec=avcodec",
        "--rtsp-user=${BuildConfig.USER}",
        "--rtsp-pwd=${BuildConfig.PASS}",
        "--rtsp-tcp",
        "-vvv"
    ).toList()) }
    val mediaPlayer = remember{ MediaPlayer(libVlc) }

    val connectionData = "rtsp://${BuildConfig.HOST}:${BuildConfig.PORT}/cam/realmonitor?channel=1&subtype=0"
    val media = Media(libVlc, connectionData.toUri()).apply {
        addOption(":network-caching=200")
        addOption(":live-caching=150")
        addOption(":file-caching=200")
    }

    VlcPlayerView(mediaPlayer, media)

    LaunchedEffect(Unit) {

    }

}