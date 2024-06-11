package com.project.sfmd_project

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.media.MediaPlayer
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.WindowManager
import android.widget.EditText
import android.widget.ImageButton
import android.widget.MediaController
import android.widget.TextView
import android.widget.Toast
import android.widget.VideoView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.ByteArrayOutputStream
import java.util.*
import com.project.sfmd_project.R
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class Chat : AppCompatActivity() {

    private var isSendingMessage = false

    private lateinit var messageRecyclerView: RecyclerView
    private lateinit var messageAdapter: MessageAdapter
    private lateinit var editTextMessage: EditText
    private lateinit var sendButton: ImageButton
    private lateinit var fileButton: ImageButton
    private lateinit var imageUpload: ImageButton
    private lateinit var voicemessage: ImageButton
    private lateinit var camerabutton: ImageButton
    private lateinit var callbutton: ImageButton
    private lateinit var videocallbutton: ImageButton

    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var messagesReference: DatabaseReference
    private lateinit var storageRef: StorageReference

    private val IMAGE_PICK_CODE = 1000
    private val FILE_PICK_CODE = 1001
    private val CAMERA_REQUEST_CODE = 1002
    private val VIDEO_REQUEST_CODE = 1003
    private val messagesList = mutableListOf<Message>()
    private var selectedMessagePosition: Int = -1

    private lateinit var audioRecorder: AudioRecorder

    private val offlineMessages = mutableListOf<Message>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val layoutParams = WindowManager.LayoutParams().apply {
            flags = WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        }
        window.attributes = layoutParams

        setContentView(R.layout.activity_chat)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        messagesReference = database.reference.child("messages")
        storageRef = FirebaseStorage.getInstance().reference

        val doctorName = intent.getStringExtra("doctorName")
        val doctorImageUrl = intent.getStringExtra("doctorImageUrl")


        audioRecorder = AudioRecorder()

        // Initialize views
        val mentorNameTextView = findViewById<TextView>(R.id.textView26)

        messageRecyclerView = findViewById(R.id.messageRecyclerView)
        editTextMessage = findViewById(R.id.editTextTextMultiLine3)
        sendButton = findViewById(R.id.imageButton20)
        fileButton = findViewById(R.id.imageButton21)
        imageUpload = findViewById(R.id.imageButton19)
       // voicemessage = findViewById(R.id.imageButton18)
        camerabutton = findViewById(R.id.imageButton13)
        callbutton = findViewById(R.id.imageButton16)
        videocallbutton = findViewById(R.id.imageButton17)

        val doctorNameTextView = findViewById<TextView>(R.id.textView26)
        doctorNameTextView.text = doctorName

        // Load image using Picasso library into doctorImageView
        val doctorImageView = findViewById<CircleImageView>(R.id.circleImageView)
        if (doctorImageUrl != null && doctorImageUrl.isNotEmpty()) {
            Picasso.get().load(doctorImageUrl).placeholder(R.drawable.doctor).into(doctorImageView)
        } else {
            // If image URL is empty or null, you can set a default placeholder image
            doctorImageView.setImageResource(R.drawable.doctor)
        }

        videocallbutton.setOnClickListener {
            val intent = Intent(this, MainActivity20::class.java)
            startActivity(intent)
        }

        callbutton.setOnClickListener {
            val intent = Intent(this, MainActivity19::class.java)
            startActivity(intent)
        }

        val mentorName = intent.getStringExtra("MENTOR_NAME")
        mentorName?.let {
            mentorNameTextView.text = it
            messagesReference = database.reference.child("messages_$it") // Use mentor's name in the database path
        }

        // Setup RecyclerView and Adapter
        messageAdapter = MessageAdapter(messagesList, object : MessageAdapter.OnMessageClickListener {
            override fun onMessageClick(position: Int) {
                selectedMessagePosition = position
                val message = messagesList[position]
                if (message.audioUrl != null) {
                    // Play audio
                    playAudioFromFirebase(message.audioUrl)
                } else {
                    // Handle other message types
                    showEditDialog(message)
                }
            }

            override fun onMessageLongClick(position: Int) {
                selectedMessagePosition = position
                showDeleteConfirmationDialog()
            }
        })

        messageRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@Chat)
            adapter = messageAdapter
            addItemDecoration(ItemOffsetDecoration(16)) // Adding item decoration with 16dp offset
        }


        // Send message button click listener
        sendButton.setOnClickListener {
            val messageText = editTextMessage.text.toString().trim()
            if (messageText.isNotEmpty() && !isSendingMessage) {
                isSendingMessage = true
                sendMessage(messageText)
                val myFirebaseMessagingService = MyFirebaseMessagingService()
                myFirebaseMessagingService.generateNotification(this,"Doctoroid", " Message sent successfully " )


            } else if (messageText.isEmpty()) {
                Toast.makeText(this, "Message cannot be empty", Toast.LENGTH_SHORT).show()
            }
        }

        // File button click listener
        fileButton.setOnClickListener {
            pickFileFromDevice()
        }

        // Image upload button click listener
        imageUpload.setOnClickListener {
            pickImageFromGallery()
        }

        // Camera button click listener
        camerabutton.setOnClickListener {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                openCamera()
            } else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.CAMERA),
                    CAMERA_REQUEST_CODE
                )
            }
        }

        // Voice message button click listener
       /* voicemessage.setOnClickListener {
            if (audioRecorder.isRecording) {
                audioRecorder.stopRecording { audioUri ->
                    if (audioUri != null) {
                        uploadAudioToFirebase(audioUri)
                    } else {
                        // Handle audio recording failure
                        Toast.makeText(this, "Failed to record audio", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                audioRecorder.startRecording()
                Toast.makeText(this, "Recording started", Toast.LENGTH_SHORT).show()
            }
        }*/

        val connectivityManager = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            connectivityManager.registerDefaultNetworkCallback(object : ConnectivityManager.NetworkCallback() {
                override fun onAvailable(network: Network) {
                    super.onAvailable(network)
                    if (!isSendingMessage && offlineMessages.isNotEmpty()) {
                        isSendingMessage = true
                        syncOfflineMessages()
                    }
                }
            })
        }



        // Listen for new messages
        messagesReference.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val message = snapshot.getValue(Message::class.java)
                if (message != null) {
                    messagesList.add(message)
                    messageAdapter.notifyItemInserted(messagesList.size - 1)
                    scrollToBottom()
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                val updatedMessage = snapshot.getValue(Message::class.java)
                if (updatedMessage != null) {
                    val existingMessage = messagesList.find { it.userId == updatedMessage.userId }
                    if (existingMessage != null) {
                        val index = messagesList.indexOf(existingMessage)
                        messagesList[index] = updatedMessage
                        messageAdapter.notifyItemChanged(index)
                    }
                }
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                val removedMessage = snapshot.getValue(Message::class.java)
                if (removedMessage != null) {
                    val existingMessage = messagesList.find { it.userId == removedMessage.userId }
                    if (existingMessage != null) {
                        val index = messagesList.indexOf(existingMessage)
                        messagesList.removeAt(index)
                        messageAdapter.notifyItemRemoved(index)
                    }
                }
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}

            override fun onCancelled(error: DatabaseError) {}
        })

        // Check for internet connectivity and sync offline messages
        if (isNetworkAvailable()) {
            syncOfflineMessages()
        } else {
            Toast.makeText(this, "No internet connection. Offline mode.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun scrollToBottom() {
        messageRecyclerView.scrollToPosition(messageAdapter.itemCount - 1)
    }

    private fun sendMessage(messageText: String) {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            val message = Message(userId, messageText, System.currentTimeMillis(), null, null, null)
            messagesReference.push().setValue(message)
                .addOnSuccessListener {
                    isSendingMessage = false
                    editTextMessage.text.clear()
                }
                .addOnFailureListener {
                    isSendingMessage = false
                    // Handle message sending failure
                    Toast.makeText(this, "Failed to send message", Toast.LENGTH_SHORT).show()
                    // Add message to offline list
                    offlineMessages.add(message)
                }
        }
    }



    private fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }

    private fun pickFileFromDevice() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "*/*"
        startActivityForResult(intent, FILE_PICK_CODE)
    }

    private fun openCamera() {
        val options = arrayOf("Take Photo", "Record Video")
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Choose an option")
        builder.setItems(options) { _, which ->
            when (which) {
                0 -> {
                    val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE)
                }
                1 -> {
                    val takeVideoIntent = Intent(MediaStore.ACTION_VIDEO_CAPTURE)
                    startActivityForResult(takeVideoIntent, VIDEO_REQUEST_CODE)
                }
            }
        }
        builder.show()
    }

    private fun takePhoto() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, CAMERA_REQUEST_CODE)
    }

    private fun recordVideo() {
        val intent = Intent(MediaStore.ACTION_VIDEO_CAPTURE)
        startActivityForResult(intent, VIDEO_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                IMAGE_PICK_CODE -> {
                    // Handle image pick
                    data?.data?.let { imageUri ->
                        uploadImageToFirebase(imageUri)
                    }
                }
                FILE_PICK_CODE -> {
                    // Handle file pick
                    data?.data?.let { fileUri ->
                        uploadFileToFirebase(fileUri)
                    }
                }
                CAMERA_REQUEST_CODE -> {
                    // Handle image capture
                    val imageBitmap = data?.extras?.get("data") as? Bitmap
                    imageBitmap?.let {
                        val imageUri = getImageUriFromBitmap(it)
                        uploadImageToFirebase(imageUri)
                    }
                }
                VIDEO_REQUEST_CODE -> {
                    // Handle video capture
                    data?.data?.let { videoUri ->
                        uploadVideoToFirebase(videoUri)
                    }
                }
            }
        }
    }
    private fun getImageUriFromBitmap(bitmap: Bitmap): Uri {
        val bytes = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(
            contentResolver,
            bitmap,
            "Image Title",
            null
        )
        return Uri.parse(path)
    }

    private fun uploadImageToFirebase(imageUri: Uri) {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            val imageRef = storageRef.child("images/${UUID.randomUUID()}")
            val uploadTask = imageRef.putFile(imageUri)
            uploadTask.continueWithTask { task ->
                if (!task.isSuccessful) {
                    task.exception?.let {
                        throw it
                    }
                }
                imageRef.downloadUrl
            }.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val downloadUri = task.result
                    val message = Message(
                        userId,
                        "",
                        System.currentTimeMillis(),
                        null,
                        downloadUri.toString(),
                        null
                    )
                    messagesReference.push().setValue(message)
                } else {
                    // Handle unsuccessful upload
                    Toast.makeText(this, "Failed to upload image", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun uploadFileToFirebase(fileUri: Uri) {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            val fileRef = storageRef.child("files/${UUID.randomUUID()}")
            val uploadTask = fileRef.putFile(fileUri)
            uploadTask.continueWithTask { task ->
                if (!task.isSuccessful) {
                    task.exception?.let {
                        throw it
                    }
                }
                fileRef.downloadUrl
            }.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val downloadUri = task.result
                    val message = Message(
                        userId,
                        "",
                        System.currentTimeMillis(),
                        null,
                        null,
                        downloadUri.toString()
                    )
                    messagesReference.push().setValue(message)
                } else {
                    // Handle unsuccessful upload
                    Toast.makeText(this, "Failed to upload file", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun uploadVideoToFirebase(videoUri: Uri) {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            val videoRef = storageRef.child("videos/${UUID.randomUUID()}")
            val uploadTask = videoRef.putFile(videoUri)
            uploadTask.continueWithTask { task ->
                if (!task.isSuccessful) {
                    task.exception?.let {
                        throw it
                    }
                }
                videoRef.downloadUrl
            }.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val downloadUri = task.result
                    val message = Message(
                        userId,
                        "",
                        System.currentTimeMillis(),
                        null,
                        null,
                        downloadUri.toString()
                    )
                    messagesReference.push().setValue(message)
                } else {
                    // Handle unsuccessful upload
                    Toast.makeText(this, "Failed to upload video", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


    private fun uploadAudioToFirebase(audioUri: Uri) {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            val audioRef = storageRef.child("audio/${UUID.randomUUID()}")
            val uploadTask = audioRef.putFile(audioUri)
            uploadTask.continueWithTask { task ->
                if (!task.isSuccessful) {
                    task.exception?.let {
                        throw it
                    }
                }
                audioRef.downloadUrl
            }.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val downloadUri = task.result
                    val message = Message(
                        userId,
                        "",
                        System.currentTimeMillis(),
                        downloadUri.toString(),
                        null,
                        null
                    )
                    messagesReference.push().setValue(message)
                    Log.d("AudioUpload", "Audio uploaded successfully: $downloadUri")
                } else {
                    // Handle unsuccessful upload
                    Toast.makeText(this, "Failed to upload audio", Toast.LENGTH_SHORT).show()
                    Log.e("AudioUpload", "Failed to upload audio")
                }
            }
        }
    }

    private fun playAudioFromFirebase(audioUrl: String) {
        Log.d("AudioPlayback", "Starting audio playback for: $audioUrl")

        val mediaPlayer = MediaPlayer()
        mediaPlayer.setDataSource(audioUrl)

        mediaPlayer.setOnPreparedListener { mp ->
            Log.d("AudioPlayback", "Audio prepared. Starting playback.")
            mp.start()
        }

        mediaPlayer.setOnCompletionListener { mp ->
            Log.d("AudioPlayback", "Audio playback completed.")
            mp.release()
        }

        mediaPlayer.setOnErrorListener { mp, what, extra ->
            Log.e("AudioPlayback", "MediaPlayer error: what=$what, extra=$extra")
            false
        }

        try {
            mediaPlayer.prepareAsync()
        } catch (e: Exception) {
            Log.e("AudioPlayback", "Failed to prepare MediaPlayer: ${e.message}")
            e.printStackTrace()
        }
    }

    private fun playVideoFromFirebase(videoUrl: String) {
        Log.d("VideoPlayback", "Starting video playback for: $videoUrl")

        val videoUri = Uri.parse(videoUrl)
        val mediaController = MediaController(this)
        mediaController.setAnchorView(messageRecyclerView)

        val videoView = VideoView(this)
        videoView.setMediaController(mediaController)
        videoView.setVideoURI(videoUri)
        videoView.requestFocus()

        val dialog = AlertDialog.Builder(this)
            .setView(videoView)
            .setPositiveButton("Close") { dialog, _ ->
                dialog.dismiss()
            }
            .create()

        dialog.show()

        videoView.setOnPreparedListener { mp ->
            Log.d("VideoPlayback", "Video prepared. Starting playback.")
            mp.start()
        }

        videoView.setOnCompletionListener { mp ->
            Log.d("VideoPlayback", "Video playback completed.")
            mp.release()
        }

        videoView.setOnErrorListener { _, what, extra ->
            Log.e("VideoPlayback", "MediaPlayer error: what=$what, extra=$extra")
            false
        }
    }


    private fun showEditDialog(message: Message) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Edit Message")

        val input = EditText(this)
        input.setText(message.messageText)
        builder.setView(input)

        builder.setPositiveButton("Save") { dialog, which ->
            val editedMessage = input.text.toString().trim()
            if (editedMessage.isNotEmpty()) {
                updateMessageInFirebase(message, editedMessage)
            } else {
                Toast.makeText(this, "Message cannot be empty", Toast.LENGTH_SHORT).show()
            }
        }
        builder.setNegativeButton("Cancel") { dialog, which -> dialog.cancel() }

        builder.show()
    }

    private fun showDeleteConfirmationDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Delete Message")
            .setMessage("Are you sure you want to delete this message?")
            .setPositiveButton("Delete") { dialog, which ->
                val message = messagesList[selectedMessagePosition]
                deleteMessageFromFirebase(message)
                selectedMessagePosition = -1
            }
            .setNegativeButton("Cancel") { dialog, which ->
                selectedMessagePosition = -1
                dialog.cancel()
            }
            .show()
    }

    private fun updateMessageInFirebase(oldMessage: Message, newMessageText: String) {
        val messageRef = messagesReference.child(oldMessage.userId ?: "")
        val updatedMessage = oldMessage.copy(messageText = newMessageText)
        messageRef.setValue(updatedMessage)
            .addOnSuccessListener {
                // Update local list and notify adapter
                val index = messagesList.indexOf(oldMessage)
                if (index != -1) {
                    messagesList[index] = updatedMessage
                    messageAdapter.notifyItemChanged(index)
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to update message", Toast.LENGTH_SHORT).show()
            }
    }

    private fun deleteMessageFromFirebase(message: Message) {
        val messageRef = messagesReference.child(message.userId ?: "")
        messageRef.removeValue()
            .addOnSuccessListener {
                // Remove from local list and notify adapter
                val index = messagesList.indexOf(message)
                if (index != -1) {
                    messagesList.removeAt(index)
                    messageAdapter.notifyItemRemoved(index)
                } else {
                    // Message not found in the list
                    // Handle this case if needed
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to delete message", Toast.LENGTH_SHORT).show()
            }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            CAMERA_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openCamera()
                } else {
                    Toast.makeText(this, "Camera permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        audioRecorder.cancelRecording()
    }

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val networkCapabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            return networkCapabilities != null &&
                    (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR))
        } else {
            val networkInfo = connectivityManager.activeNetworkInfo
            return networkInfo != null && networkInfo.isConnected
        }
    }

    private fun syncOfflineMessages() {
        if (offlineMessages.isNotEmpty()) {
            val message = offlineMessages.removeAt(0)
            sendMessage(message.messageText)
        }
    }
}
