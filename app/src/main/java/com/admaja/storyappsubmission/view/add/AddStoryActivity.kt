package com.admaja.storyappsubmission.view.add

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.admaja.storyappsubmission.databinding.ActivityAddStoryBinding
import java.io.File
import android.Manifest
import android.content.Intent
import android.content.Intent.ACTION_GET_CONTENT
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.location.Location
import android.net.Uri
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.admaja.storyappsubmission.R
import com.admaja.storyappsubmission.utils.createCustomTempFile
import com.admaja.storyappsubmission.utils.reduceFileImage
import com.admaja.storyappsubmission.utils.uriToFile
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import com.admaja.storyappsubmission.data.Result
import com.admaja.storyappsubmission.view.main.MainActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import okhttp3.RequestBody

class AddStoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddStoryBinding
    private var getFile: File? = null
    private lateinit var currentPhotoPath: String
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var location: Location? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        binding.apply {
            btOpenCamera.setOnClickListener {startCamera()}
            btOpenGalerry.setOnClickListener { startGallery() }
            btUploadStory.setOnClickListener { uploadStory() }
        }
    }

    private fun uploadStory() {
        if (getFile != null) {
            val file = reduceFileImage(getFile as File)
            val description = binding.etInputDescription.text.toString().toRequestBody("text/plain".toMediaType())
            val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
            val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                "photo",
                file.name,
                requestImageFile
            )

            val lat: RequestBody = location?.latitude.toString().toRequestBody("text/plain".toMediaType())
            val lon: RequestBody = location?.longitude.toString().toRequestBody("text/plain".toMediaType())

            val viewModelFactory: AddStoryViewModelFactory = AddStoryViewModelFactory.getInstance(this)
            val viewModel: AddStoryViewModel by viewModels {
                viewModelFactory
            }
            viewModel.addStory(description, imageMultipart, lat, lon).observe(this) {
                if (it != null) {
                    when(it) {
                        is Result.Loading -> {
                            binding.layoutLoadingAdd.root.visibility = View.VISIBLE
                        }
                        is Result.Success -> {
                            binding.layoutLoadingAdd.root.visibility = View.GONE
                            Toast.makeText(this, it.data.message, Toast.LENGTH_SHORT).show()
                            Intent(this, MainActivity::class.java).apply {
                                startActivity(this)
                                finish()
                            }
                        }
                        is Result.Error -> {
                            binding.layoutLoadingAdd.root.visibility = View.GONE
                            Toast.makeText(this, R.string.error_message, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        } else {
            Toast.makeText(this, R.string.image_empty, Toast.LENGTH_SHORT).show()
        }
    }

    private fun startGallery() {
        val intent = Intent()
        intent.action = ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, R.string.choose_picture.toString())
        launcherIntentGallery.launch(chooser)
    }

    private fun startCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.resolveActivity(packageManager)

        createCustomTempFile(application).also {
            val photoURI: Uri = FileProvider.getUriForFile(
                this@AddStoryActivity,
                "com.admaja.storyappsubmission",
                it
            )
            currentPhotoPath = it.absolutePath
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            launcherIntentCamera.launch(intent)

        }

    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == RESULT_OK) {
            val myFile = File(currentPhotoPath)
            getFile = myFile

            val result = BitmapFactory.decodeFile(getFile?.path)
            binding.ivAddStory.setImageBitmap(result)
        }
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg: Uri = result.data?.data as Uri
            val myFile = uriToFile(selectedImg, this@AddStoryActivity)
            getFile = myFile
            binding.ivAddStory.setImageURI(selectedImg)
        }
    }

    private val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {permission ->
        when {
            permission[Manifest.permission.ACCESS_FINE_LOCATION]?: false -> {
                getLocation()
            }
            permission[Manifest.permission.ACCESS_COARSE_LOCATION]?: false -> {
                getLocation()
            }
            else -> {
                Toast.makeText(this, R.string.permissions, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        getLocation()
    }

    private fun checkPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun getLocation() {
        if (checkPermission(Manifest.permission.ACCESS_FINE_LOCATION) && checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION)){
            fusedLocationClient.lastLocation.addOnSuccessListener { loc: Location? ->
                if (loc != null) {
                    this.location = loc
                } else {
                    Toast.makeText(this, R.string.location_not_found, Toast.LENGTH_SHORT).show()
                }
            }
        }else {
            requestPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

}