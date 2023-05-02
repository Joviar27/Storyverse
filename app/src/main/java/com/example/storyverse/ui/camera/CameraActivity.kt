package com.example.storyverse.ui.camera

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Surface
import android.view.WindowManager
import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.example.storyverse.ui.addstory.AddStoryFragment
import com.example.storyverse.databinding.ActivityCameraBinding
import com.example.storyverse.utils.createFile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CameraActivity : AppCompatActivity() {

    private var _binding: ActivityCameraBinding? = null
    private val binding get() = _binding

    private var imageCapture : ImageCapture? = null

    private var cameraSelector : CameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

    private var rotation : Int = Surface.ROTATION_0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityCameraBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        supportActionBar?.hide()

        binding?.ivCapture?.setOnClickListener {
            takePhoto(this)
        }

        binding?.ivSwitch?.setOnClickListener {
            cameraSelector = if(cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA){
                CameraSelector.DEFAULT_FRONT_CAMERA
            }
            else{
                CameraSelector.DEFAULT_BACK_CAMERA
            }
            startCamera()
        }
    }

    public override fun onResume() {
        super.onResume()
        startCamera()
    }

    private fun takePhoto(context: Context) {
        val imageCapture = imageCapture ?: return
        val photoFile = createFile(application)

        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        @Suppress("DEPRECATION")
        rotation = windowManager.defaultDisplay.rotation

        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback{
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    val intent = Intent().apply {
                        putExtra("picture", photoFile)
                        putExtra("isBackCamera", cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA)
                        putExtra("rotation", rotation)
                    }
                    setResult(AddStoryFragment.CAMERA_X_RESULT, intent)
                    finish()
                }

                override fun onError(exception: ImageCaptureException) {
                    Toast.makeText(
                        this@CameraActivity,
                        "Gagal mengambil gambar.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        )
    }

    private fun startCamera() = lifecycleScope.launch {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this@CameraActivity)

        val cameraProvider : ProcessCameraProvider =
            withContext(Dispatchers.IO) {
                cameraProviderFuture.get()
            }

        val preview = Preview.Builder().build().also {
            it.setSurfaceProvider(binding?.viewFinder?.surfaceProvider)
        }

        imageCapture = ImageCapture.Builder().build()

        try{
            cameraProvider.unbindAll()

            cameraProvider.bindToLifecycle(
                this@CameraActivity,
                cameraSelector,
                preview,
                imageCapture
            )
        }
        catch (e : Exception){
            Toast.makeText(
                this@CameraActivity,
                "Gagal memunculkan kamera.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}