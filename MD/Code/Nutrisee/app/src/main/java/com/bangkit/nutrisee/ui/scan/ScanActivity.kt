package com.bangkit.nutrisee.ui.scan

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.*
import androidx.camera.core.ImageCapture
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import com.bangkit.nutrisee.databinding.ActivityScanBinding
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class ScanActivity : AppCompatActivity() {
    private lateinit var binding: ActivityScanBinding
    private var imageCapture: ImageCapture? = null
    private lateinit var outputDirectory: File

    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val allGranted = permissions.all { it.value }
        if (allGranted) startCamera()
        else {
            Toast.makeText(this, "Permissions not granted.", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private val galleryLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImageUri = result.data?.data
            binding.apply {
                capturedImage.setImageURI(selectedImageUri)
                capturedImage.tag = selectedImageUri // Simpan URI ke tag
                capturedImage.visibility = View.VISIBLE
                viewFinder.visibility = View.GONE
                fabProcess.visibility = View.VISIBLE
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        outputDirectory = getOutputDirectory()

        setupListeners()
        checkPermissionsAndStartCamera()
    }

    private fun setupListeners() {
        binding.apply {
            btnCapture.setOnClickListener { capturePhoto() }
            btnGallery.setOnClickListener { openGallery() }
            fabProcess.setOnClickListener { processImage() }
        }
    }

    private fun checkPermissionsAndStartCamera() {
        if (allPermissionsGranted()) startCamera()
        else requestPermissions()
    }

    private fun capturePhoto() {
        val imageCapture = imageCapture ?: return
        val photoFile = File(
            outputDirectory,
            SimpleDateFormat(FILENAME_FORMAT, Locale.US)
                .format(System.currentTimeMillis()) + ".jpg"
        )

        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {
                override fun onError(exc: ImageCaptureException) {
                    Toast.makeText(
                        baseContext,
                        "Photo capture failed: ${exc.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    val savedUri = Uri.fromFile(photoFile)
                    binding.apply {
                        capturedImage.setImageURI(savedUri)
                        capturedImage.tag = savedUri // Simpan URI ke tag
                        capturedImage.visibility = View.VISIBLE
                        viewFinder.visibility = View.GONE
                        fabProcess.visibility = View.VISIBLE
                    }
                }
            }
        )
    }

    private fun openGallery() {
        val intent = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )
        galleryLauncher.launch(intent)
    }

    private fun processImage() {
        // TODO: Implement image processing logic
        Toast.makeText(this, "Processing image...", Toast.LENGTH_SHORT).show()
        val imageUri = binding.capturedImage.tag as? Uri
        if (imageUri != null) {
            val intent = Intent(this, ConfirmScanActivity::class.java).apply {
                putExtra("EXTRA_IMAGE_URI", imageUri)
            }
            startActivity(intent)
        } else {
            Toast.makeText(this, "No image to process.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(binding.viewFinder.surfaceProvider)
                }

            imageCapture = ImageCapture.Builder()
                .build()

            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview, imageCapture
                )
            } catch(exc: Exception) {
                Toast.makeText(this, "Camera initialization failed", Toast.LENGTH_SHORT).show()
            }
        }, ContextCompat.getMainExecutor(this))
    }

    private fun getOutputDirectory(): File {
        val mediaDir = externalMediaDirs.firstOrNull()?.let {
            File(it, resources.getString(com.bangkit.nutrisee.R.string.app_name)).apply { mkdirs() }
        }
        return if (mediaDir != null && mediaDir.exists())
            mediaDir else filesDir
    }

    private fun requestPermissions() {
        permissionLauncher.launch(REQUIRED_PERMISSIONS)
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    companion object {
        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
        private val REQUIRED_PERMISSIONS = arrayOf(
            Manifest.permission.CAMERA,
//            Manifest.permission.READ_EXTERNAL_STORAGE,
//            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
    }
}