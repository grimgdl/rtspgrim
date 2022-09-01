package com.grimgdl.rtspgrim


import android.net.Uri
import android.os.Build
import android.os.Bundle

import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.grimgdl.rtspgrim.databinding.MainActivityLayoutBinding
import org.videolan.libvlc.LibVLC
import org.videolan.libvlc.Media
import org.videolan.libvlc.MediaPlayer


class MainActivity: AppCompatActivity() {


    private lateinit var mediaPlayer : MediaPlayer
    private lateinit var vlc: LibVLC



    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = MainActivityLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val options = mutableListOf("-vvv")
        options.add("--sout-transcode-vb=750")

        vlc = LibVLC(this, options)
        val vlcVideoLayout = binding.videoLayout
        mediaPlayer = MediaPlayer(vlc)


        mediaPlayer.vlcVout.setVideoSurface(vlcVideoLayout.holder.surface,  vlcVideoLayout.holder)


        val ivlcVout = mediaPlayer.vlcVout
        ivlcVout.setWindowSize(1280, 760)
        ivlcVout.attachViews()


        val connectionData = String.format("rtsp://%s:%s@%s:%s/cam/realmonitor?channel=1&subtype=0",
            BuildConfig.USER, BuildConfig.PASS, BuildConfig.HOST, BuildConfig.PORT)

        val media = Media(vlc, Uri.parse(connectionData))



        mediaPlayer.media = media
        media.release()

        mediaPlayer.setEventListener {

            binding.bitrate.post {
                binding.bitrate.text = it.lengthChanged.toString()
            }

        }


        binding.play.setOnClickListener{
            if (mediaPlayer.isPlaying) mediaPlayer.stop()
            else mediaPlayer.play()
        }

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