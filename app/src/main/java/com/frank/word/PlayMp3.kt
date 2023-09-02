package com.frank.word

import android.media.AudioAttributes
import android.media.MediaPlayer
import android.net.Uri
import java.util.Timer
import java.util.TimerTask

var thisTimer: Timer = Timer()
var thisTask: TimerTask? = null

fun playMp3(result: Uri, index: Int) {
    if (isFirstTime) {
        mediaPlayer = MediaPlayer().apply {
            setAudioAttributes(
                AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .build()
            )

            if (thisTask == null) {
                thisTask = object : TimerTask() {
                    override fun run() {
                        try {
                            if (titleString.isNotEmpty()) mainActivity.title = titleString
                            titleString = ""
                            doTask()
                        } catch (_: Exception) {
                        }
                    }
                }
                thisTimer.scheduleAtFixedRate(thisTask, 100, 100)
            }
        }
    } else {
        mediaPlayer.reset()
    }
    isFirstTime = true
    mediaPlayer.setDataSource(mainActivity, result)
    mediaPlayer.prepare()
    mediaPlayer.start()
    pathAndName = result.path ?: ""
    readTextFile(index)
}
