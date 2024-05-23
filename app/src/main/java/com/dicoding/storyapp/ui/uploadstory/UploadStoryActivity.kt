package com.dicoding.storyapp.ui.uploadstory

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import com.dicoding.storyapp.R
import com.dicoding.storyapp.databinding.ActivityUploadStoryBinding
import com.dicoding.storyapp.ui.auth.AuthenticationViewModel
import com.dicoding.storyapp.ui.auth.AuthenticationViewModelFactory
import com.dicoding.storyapp.ui.uploadstory.CameraActivity.Companion.CAMERAX_RESULT
import com.dicoding.storyapp.ui.utils.reduceFileImage
import com.dicoding.storyapp.ui.utils.uriToFile
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody

class UploadStoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUploadStoryBinding
    private lateinit var token: String
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var currentImageUri: Uri? = null
    private var currentLat: Float? = null
    private var currentLon: Float? = null
    private val authenticationViewModel by viewModels<AuthenticationViewModel> {
        AuthenticationViewModelFactory.getInstance(context = this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadStoryBinding.inflate(layoutInflater)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        supportActionBar?.title = "Upload Story"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if (!allPermissionsGranted()) {
            requestPermissionLauncher.launch(REQUIRED_PERMISSION)
        }

        authenticationViewModel.getSession().observe(this) {
            token = it.token
        }

        binding.galleryButton.setOnClickListener { startGallery() }
        binding.cameraButton.setOnClickListener { startCameraX() }
        binding.buttonAdd.setOnClickListener { uploadStory() }
        binding.checkboxLocation.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                getMyLocation()
            } else {
                currentLat = null
                currentLon = null
            }
        }
        setContentView(binding.root)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            currentImageUri = uri
            showImage()
        } else {
            Log.d("Photo Picker", "No media selected")
        }
    }

    private fun showImage() {
        currentImageUri?.let {
            Log.d("Image URI", "showImage: $it")
            binding.previewImageView.setImageURI(it)
        }
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                Toast.makeText(this, "Permission request granted", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "Permission request denied", Toast.LENGTH_LONG).show()
            }
        }

    private val requestPermissionLauncherLocation =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                getMyLocation()
            }
        }

    private fun startCameraX() {
        val intent = Intent(this, CameraActivity::class.java)
        launcherIntentCameraX.launch(intent)
    }

    private val launcherIntentCameraX = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == CAMERAX_RESULT) {
            currentImageUri = it.data?.getStringExtra(CameraActivity.EXTRA_CAMERAX_IMAGE)?.toUri()
            showImage()
        }
    }

    private fun uploadStory() {
        currentImageUri?.let { uri ->
            val imageFile = uriToFile(uri, this).reduceFileImage()
            showLoading(true)
            val requestBody = binding.edAddDescription.text.toString().toRequestBody("text/plain".toMediaType())
            val latRequestBody = currentLat?.toString()?.toRequestBody("text/plain".toMediaType())
            val lonRequestBody = currentLon?.toString()?.toRequestBody("text/plain".toMediaType())
            val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
            val multipartBody = MultipartBody.Part.createFormData(
                "photo",
                imageFile.name,
                requestImageFile
            )
            val uploadStoryViewModel by viewModels<UploadStoryViewModel>(){
                UploadViewModelFactory(token)
            }

            uploadStoryViewModel.isLoading.observe(this) {
                showLoading(it)
            }

            uploadStoryViewModel.uploadStory(description = requestBody, image = multipartBody, lat = latRequestBody, lon = lonRequestBody)

            uploadStoryViewModel.createStoryResponse.observe(this) {
                if (!it.error) {
                    showToast("Story upload successful")
                    finish()
                }
                else {
                    showToast("Story upload failed")
                }
            }

        } ?: showToast(getString(R.string.empty_image_warning))
    }

    private fun getMyLocation() {
        if (ContextCompat.checkSelfPermission(
                this.applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                if (location != null) {
                    currentLat = location.latitude.toFloat()
                    currentLon = location.longitude.toFloat()
                } else {
                    Toast.makeText(
                        this,
                        "Location is not found. Try Again",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        } else {
            requestPermissionLauncherLocation.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun allPermissionsGranted() =
        ContextCompat.checkSelfPermission(
            this,
            REQUIRED_PERMISSION
        ) == PackageManager.PERMISSION_GRANTED

    companion object {
        private const val REQUIRED_PERMISSION = Manifest.permission.CAMERA
    }
}