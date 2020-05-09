package com.miraxh.dreamer.ui.add

import android.content.Context
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.net.Uri
import android.os.Environment
import android.os.SystemClock
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Chronometer
import android.widget.ImageButton
import android.widget.SeekBar
import com.google.android.material.snackbar.Snackbar
import com.miraxh.dreamer.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.IOException
import java.util.*

class AudioHelper(var view: View, var context: Context?, var uri: String?) {

    //mediaplayer
    private var mediaPlayer: MediaPlayer? = null


    //mediarecorder
    lateinit var titleRecording: String
    private var mediaRecorder: MediaRecorder? = null
    private lateinit var managementBtn: Button
    private val folderName = "audio_files"
    private var currentPosition: Int = 0
    private lateinit var seekBar: SeekBar
    private lateinit var chronometer: Chronometer
    private lateinit var deleteBtn: ImageButton
    var state: Int = 0

    init {
        //inizializzo i componenti
        setComponents()
        managementAudio()
    }

    private fun setComponents() {
        managementBtn = view.findViewById(R.id.recording_btn)
        chronometer = view.findViewById(R.id.audio_chronometer)
        deleteBtn = view.findViewById(R.id.delete_audio_btn)
        seekBar = view.findViewById(R.id.audio_seekbar)
    }

    private fun managementAudio() {
        setAudio()
        //listener sul bottone per registrare
        managementBtn.setOnClickListener {
            //implementare controllo permessi
            when (state) {
                0 -> {
                    setAudio()
                    startRecording()
                    state++
                    managementBtn.text = "Stop"
                }
                1 -> {
                    stopRecording()
                    state++
                    managementBtn.text = "Play"
                }
                2 -> {
                    play()
                    state++
                    managementBtn.text = "Pause"
                }
                3 -> {
                    pause()
                    state = 2
                    managementBtn.text = "Play"
                }
            }
        }
        deleteBtn.setOnClickListener {
            deleteAudioFile()
        }
    }

    private fun setAudio() {
        //conferisco all'audio un indirizzo univoco dato dalla data di oggi
        createUniqueName()

        val myDirectory =
            File(context?.getExternalFilesDir(null)?.absolutePath, folderName)
        if (!myDirectory.exists()) {
            myDirectory.mkdirs()
        }

        uri = context?.getExternalFilesDir(null)?.absolutePath + "/$folderName/$titleRecording.mp3"
        mediaRecorder = MediaRecorder()

        mediaRecorder?.setAudioSource(MediaRecorder.AudioSource.MIC)
        mediaRecorder?.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
        mediaRecorder?.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
        mediaRecorder?.setOutputFile(uri)
    }

    private fun createUniqueName() {
        val cal = Calendar.getInstance()
        titleRecording = cal.time.toString()
        //trimming della string
        titleRecording = titleRecording.replace(' ', '_').toLowerCase()
        titleRecording = titleRecording.replace(':', '_').toLowerCase()
        titleRecording = titleRecording.replace('+', '_').toLowerCase()
    }

    private fun startRecording() {
        try {
            resetChronometer()
            mediaRecorder?.prepare()
            mediaRecorder?.start()
            chronometer.start()
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
        chronometer.stop()
        mediaRecorder?.release()
        Snackbar.make(
            view,
            "Recording stopped",
            Snackbar.LENGTH_SHORT
        ).show()

        deleteBtn.visibility = View.VISIBLE
        seekBar.visibility = View.VISIBLE
        seekBar.progress = 0
    }

    fun play() {
        if (mediaPlayer == null) {
            Log.i("test_audio", uri)
            mediaPlayer = MediaPlayer.create(context, Uri.parse(uri))
        }

        seekBar.max
        seekBar.progress = currentPosition
        seekBar.max = mediaPlayer?.duration ?: 0
        mediaPlayer?.start()
        changeSeekbar()

        mediaPlayer?.setOnCompletionListener {
            managementBtn.text = "Play"
            state = 2
        }
    }

    private fun changeSeekbar() {
        seekBar.progress = mediaPlayer?.currentPosition ?: 0
        if (mediaPlayer?.isPlaying ?: false) {
            CoroutineScope(Dispatchers.IO).launch {
                changeSeekbar()
            }
        }
    }

    fun pause() {
        if (mediaPlayer != null) {
            mediaPlayer?.pause()
            currentPosition = mediaPlayer?.getCurrentPosition() ?: 0
        }
    }

    private fun stop() {
        mediaPlayer?.stop()
    }

    private fun relesaePlayer() {
        if (mediaPlayer != null) {
            mediaPlayer?.stop()
            mediaPlayer?.release()

            mediaPlayer = null
        }
    }

    fun getUriAudioFile(): String? {
        return uri
    }

    private fun resetChronometer() {
        chronometer.setBase(SystemClock.elapsedRealtime())
    }

    fun deleteAudioFile() {
        val audioFile = File(uri)
        relesaePlayer()
        deleteBtn.visibility = View.INVISIBLE
        seekBar.visibility = View.INVISIBLE
        currentPosition = 0
        seekBar.progress = currentPosition
        resetChronometer()
        managementBtn.text = "Start"
        state = 0
    }
}