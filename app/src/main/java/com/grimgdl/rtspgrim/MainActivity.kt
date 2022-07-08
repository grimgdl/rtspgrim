package com.grimgdl.rtspgrim

import android.graphics.Insets
import android.graphics.Point
import android.graphics.Rect
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.util.Size
import android.view.WindowInsets
import android.view.WindowManager
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.grimgdl.rtspgrim.databinding.MainActivityLayoutBinding
import org.videolan.libvlc.LibVLC
import org.videolan.libvlc.Media
import org.videolan.libvlc.MediaPlayer
import org.videolan.libvlc.interfaces.IVLCVout

class MainActivity: AppCompatActivity(), IVLCVout.Callback {


    private lateinit var mediaPlayer : MediaPlayer
    private lateinit var vlc: LibVLC


    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = MainActivityLayoutBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


        vlc = LibVLC(this)
        val vlcVideoLayout = binding.videoLayout
        mediaPlayer = MediaPlayer(vlc)

        mediaPlayer.vlcVout.setVideoSurface(vlcVideoLayout.holder.surface,  vlcVideoLayout.holder)
        //mediaPlayer.vlcVout.attachViews()


        val ivlcVout = mediaPlayer.vlcVout
        ivlcVout.setWindowSize(1280, 760)
        ivlcVout.attachViews();




        val connectionData = String.format("rtsp://%s:%s@%s:%s/cam/realmonitor?channel=1&subtype=0",
            BuildConfig.USER, BuildConfig.PASS, BuildConfig.HOST, BuildConfig.PORT)

        val media = Media(vlc, Uri.parse(connectionData))

        mediaPlayer.media = media
        media.release()


        mediaPlayer.play()

    }

    override fun onSurfacesCreated(vlcVout: IVLCVout?) {
        TODO("Not yet implemented")
    }

    override fun onSurfacesDestroyed(vlcVout: IVLCVout?) {
        TODO("Not yet implemented")
    }


    override fun onDestroy() {
        super.onDestroy()

        mediaPlayer.release()
        vlc.release()

    }


    override fun onStop() {
        super.onStop()

        mediaPlayer.stop()
        mediaPlayer.detachViews()

    }

    fun WindowManager.currentWindowMetricsPointCompat(): Point {
        return if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
            val windowInsets = currentWindowMetrics.windowInsets
            var insets: Insets = windowInsets.getInsets(WindowInsets.Type.navigationBars())
            windowInsets.displayCutout?.run {
                insets = Insets.max(insets, Insets.of(safeInsetLeft, safeInsetTop, safeInsetRight, safeInsetBottom))
            }
            val insetsWidth = insets.right + insets.left
            val insetsHeight = insets.top + insets.bottom
            Point(currentWindowMetrics.bounds.width() - insetsWidth, currentWindowMetrics.bounds.height() - insetsHeight)
        }else{
            Point().apply {
                defaultDisplay.getSize(this)
            }
        }
    }


}