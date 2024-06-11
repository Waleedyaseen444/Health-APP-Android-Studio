package com.project.sfmd_project


import android.media.MediaRecorder
import android.net.Uri
import java.io.File
import java.util.*

class AudioRecorder {

    private var recorder: MediaRecorder? = null
    private var outputFile: File? = null
    var isRecording = false
        private set

    fun startRecording() {
        if (!isRecording) {
            recorder = MediaRecorder()
            outputFile = File.createTempFile("audio", ".3gp")
            recorder?.apply {
                setAudioSource(MediaRecorder.AudioSource.MIC)
                setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
                setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
                setOutputFile(outputFile?.absolutePath)
                prepare()
                start()
            }
            isRecording = true
        }
    }

    fun stopRecording(callback: (Uri?) -> Unit) {
        if (isRecording) {
            recorder?.apply {
                stop()
                release()
            }
            isRecording = false
            callback(Uri.fromFile(outputFile))
        } else {
            callback(null)
        }
    }

    fun cancelRecording() {
        if (isRecording) {
            recorder?.apply {
                stop()
                release()
            }
            outputFile?.delete()
            isRecording = false
        }
    }
}
