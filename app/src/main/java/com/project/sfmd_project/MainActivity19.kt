package com.project.sfmd_project

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.content.pm.PackageManager

import android.view.SurfaceView
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.project.sfmd_project.databinding.ActivityMain19Binding
import io.agora.rtc2.ChannelMediaOptions
import io.agora.rtc2.Constants
import io.agora.rtc2.IRtcEngineEventHandler
import io.agora.rtc2.RtcEngine
import io.agora.rtc2.RtcEngineConfig
import io.agora.rtc2.video.VideoCanvas



class MainActivity19 : AppCompatActivity() {
    private val appId = "2fb35ceaf35844b59fb003e66bec3cc9"
    private val channelName = "SMDA2"
    private var token: String? = "007eJxTYPh72ObfnXtHlzjq/12g1HRlY2P4vxjPycxv1Vlvfq9vvFypwGCUlmRsmpyamGZsamFikmRqmZZkYGCcamaWlJpsnJxs6brrZ2pDICMDj6EbKyMDBIL4rAzBvi6ORgwMAIVqIhQ="
    private val uid = 0
    private var isJoined = false
    private var agoraEngine: RtcEngine? = null
    private val PERMISSION_REQ_ID = 22
    private val REQUESTED_PERMISSIONS = arrayOf(
        android.Manifest.permission.RECORD_AUDIO
    )

    private lateinit var binding: ActivityMain19Binding

    private val mRtcEventHandler: IRtcEngineEventHandler = object : IRtcEngineEventHandler() {
        override fun onJoinChannelSuccess(channel: String, uid: Int, elapsed: Int) {
            isJoined = true
            showMessage("Joined Channel $channel")
        }

        override fun onUserOffline(uid: Int, reason: Int) {
            showMessage("Remote user offline $uid $reason")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMain19Binding.inflate(layoutInflater)
        setContentView(binding.root)

        if (!checkSelfPermission()) {
            ActivityCompat.requestPermissions(this, REQUESTED_PERMISSIONS, PERMISSION_REQ_ID);
        }
        setupAudioSDKEngine()
    }

    override fun onDestroy() {
        super.onDestroy()
        agoraEngine?.leaveChannel()
        RtcEngine.destroy()
        agoraEngine = null
    }

    private fun checkSelfPermission(): Boolean {
        return ContextCompat.checkSelfPermission(this, REQUESTED_PERMISSIONS[0]) == PackageManager.PERMISSION_GRANTED
    }

    fun showMessage(message: String?) {
        runOnUiThread {
            Toast.makeText(
                applicationContext,
                message,
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun setupAudioSDKEngine() {
        try {
            val config = RtcEngineConfig()
            config.mContext = baseContext
            config.mAppId = appId
            config.mEventHandler = mRtcEventHandler
            agoraEngine = RtcEngine.create(config)
            agoraEngine?.enableAudio()
        } catch (e: Exception) {
            showMessage(e.toString())
        }
    }

    fun joinChannel(view: View) {
        if (checkSelfPermission()) {
            agoraEngine?.joinChannel(token, channelName, null, uid)
        } else {
            Toast.makeText(applicationContext, "Permissions for audio recording not granted", Toast.LENGTH_SHORT).show()
        }
    }

    fun leaveChannel(view: View) {
        if (!isJoined) {
            showMessage("Join a channel first")
        } else {
            agoraEngine?.leaveChannel()
            showMessage("You left the channel")
            isJoined = false
        }
    }
}