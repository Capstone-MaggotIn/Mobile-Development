package com.capstone.maggotin.ui.camera

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.capstone.maggotin.R
import com.capstone.maggotin.databinding.FragmentCameraBinding
import com.capstone.maggotin.utils.CameraUtils.getImageUri
import java.io.File

@Suppress("DEPRECATION")
class CameraFragment : Fragment() {

    private var _binding: FragmentCameraBinding? = null

    private val binding get() = _binding!!
    private var currentImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentCameraBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.galleryButton.setOnClickListener{ startGallery() }
        binding.cameraButton.setOnClickListener { startCamera() }
        binding.analyzeButton.setOnClickListener{ startAnalize() }
        return root
    }

    @Deprecated("Deprecated in Java")
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_camera, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    @Deprecated("Deprecated in Java")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_subscribe -> {
                Toast.makeText(context, "Sorry, we are under maintenance", Toast.LENGTH_SHORT).show()
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

    private fun startCamera() {
        currentImageUri = getImageUri(requireContext())
        launcherIntentCamera.launch(currentImageUri!!)
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess) {
            showImage()
        } else {
            currentImageUri = null
        }
    }

    private fun showImage() {
        currentImageUri?.let {
            Log.d("Image URI", "showImage: $it")
            binding.previewImageView.setImageURI(it)
        }
    }

    private fun startAnalize(){
        currentImageUri?.let { uri ->
            val imageFile = uriToFile(uri, requireContext())
            val intent = Intent(requireContext(), ResultActivity::class.java)
            intent.putExtra("imageFilePath", imageFile.absolutePath)
            startActivity(intent)
        } ?: run {
            Toast.makeText(requireContext(), "No image selected!", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun uriToFile(uri: Uri, context: Context): File {
        val contentResolver = context.contentResolver
        val tempFile = File.createTempFile("image", ".jpg", context.cacheDir)
        tempFile.outputStream().use { outputStream ->
            contentResolver.openInputStream(uri)?.use { inputStream ->
                inputStream.copyTo(outputStream)
            }
        }
        return tempFile
    }
}