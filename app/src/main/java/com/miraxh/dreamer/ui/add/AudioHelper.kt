package com.miraxh.dreamer.ui.add

import android.content.Context
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.net.Uri
import android.util.Log
import android.view.View
import android.widget.Button
import com.google.android.material.snackbar.Snackbar
import com.miraxh.dreamer.R
import java.io.IOException
import java.util.*

class AudioHelper(var view: View, var context: Context?) {

    //mediaplayer
    private var mediaPlayer: MediaPlayer? = null

    //mediarecorder
    private lateinit var titleRecording: String
    private lateinit var uriFile: String
    private var mediaRecorder: MediaRecorder? = null
    private var state: Int = 0

    private var managementBtn: Button

    init {
        this.context = context
        managementBtn = view.findViewById(R.id.recording_btn)
        managementAudio()
    }

    private fun managementAudio() {
        setAudio()
        //listener sul bottone per registrare
        managementBtn.setOnClickListener {
            //implementare controllo permessi
            when(state){
                0 ->{
                    startRecording()
                    state++
                    managementBtn.text = "Stop"
                }
                1 ->{
                    stopRecording()
                    state++
                    managementBtn.text = "Play"
                }
                2 ->{
                    play(null)
                    state++
                    managementBtn.text = "Stop"
                }
                3 ->{
                    stop()
                    state = 2
                    managementBtn.text = "Play"
                }
            }
        }
    }

    private fun setAudio() {
        //conferisco all'audio un indirizzo univoco dato dalla data di oggi
        val cal = Calendar.getInstance()
        titleRecording = cal.time.toString()

        //trimming della string
        titleRecording = titleRecording.replace(' ', '_').toLowerCase()
        titleRecording = titleRecording.replace(':', '_').toLowerCase()
        titleRecording = titleRecording.replace('+', '_').toLowerCase()

        uriFile = context?.getExternalFilesDir(null)?.absolutePath + "/$titleRecording.mp3"
        mediaRecorder = MediaRecorder()

        mediaRecorder?.setAudioSource(MediaRecorder.AudioSource.MIC)
        mediaRecorder?.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
        mediaRecorder?.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
        mediaRecorder?.setOutputFile(uriFile)
    }

    private fun startRecording() {
        try {
            mediaRecorder?.prepare()
            mediaRecorder?.start()
            Snackbar.make(
                view,
                "Recording started!",
                Snackbar.LENGTH_SHORT
            ).show()
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun stopRecording() {
        mediaRecorder?.stop()
        mediaRecorder?.release()
        Snackbar.make(
            view,
            "Recording stopped",
            Snackbar.LENGTH_SHORT
        ).show()
    }


    fun play(uri: String?) {
        if (uri != null) {
            uriFile = uri
        }

        if (mediaPlayer == null) {
            Log.i("test_audio",uriFile)
            mediaPlayer = MediaPlayer.create(context, Uri.parse(uriFile))
        }
        mediaPlayer?.start()
        mediaPlayer?.setOnCompletionListener {
            managementBtn.text = "Play"
            state = 2
        }
    }

    fun pause() {
        if (mediaPlayer != null) {
            mediaPlayer?.pause()
        }
    }

    fun stop() {
        stopPlayer()
    }

    private fun stopPlayer() {
        if (mediaPlayer != null) {
            mediaPlayer?.release()
            mediaPlayer = null
        }
    }

    private fun getUriAudioFile(): String {
        return uriFile
    }

    //metodi di pausa e resume sono disponibili per 24 up
}