package com.grimgdl.rtspgrim

import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.grimgdl.rtspgrim.databinding.MainActivityLayoutBinding
import org.videolan.libvlc.LibVLC
import org.videolan.libvlc.Media
import org.videolan.libvlc.MediaPlayer
import org.videolan.libvlc.interfaces.IVLCVout

class MainActivity: AppCompatActivity(), IVLCVout.Callback {


    private lateinit var mediaPlayer : MediaPlayer
    private lateinit var vlc: LibVLC


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = MainActivityLayoutBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


        vlc = LibVLC(this)
        val vlcVideoLayout = binding.videoLayout
        mediaPlayer = MediaPlayer(vlc)


        mediaPlayer.attachViews(vlcVideoLayout, null, false,  false)

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


}