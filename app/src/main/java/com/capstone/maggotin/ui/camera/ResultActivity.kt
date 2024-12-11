package com.capstone.maggotin.ui.camera

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.capstone.maggotin.R
import com.capstone.maggotin.data.ImageRepository
import com.capstone.maggotin.data.remote.response.ClassifyResponse
import com.capstone.maggotin.data.remote.retrofit.ApiConfig
import com.capstone.maggotin.databinding.ActivityResultBinding
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class ResultActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResultBinding
    private lateinit var viewModel: ResultViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val apiService = ApiConfig.getModelApiService()
        val repository = ImageRepository.getInstance(apiService)
        val factory = ResultViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[ResultViewModel::class.java]

        val progressBar = findViewById<ProgressBar>(R.id.progress_bar)
        val mainContent = findViewById<View>(R.id.main_content)

        mainContent.visibility = View.GONE
        progressBar.visibility = View.VISIBLE

        val filePath = intent.getStringExtra("imageFilePath")
        if (filePath != null) {
            val file = File(filePath)
            if (file.exists()) {
                binding.resultImage.setImageURI(Uri.fromFile(file))
            } else {
                Toast.makeText(this, "File not found!", Toast.LENGTH_SHORT).show()
            }

            val filePart = createImagePart(filePath)
            viewModel.uploadImage(filePart)
        } else {
            Toast.makeText(this, "No file to upload!", Toast.LENGTH_SHORT).show()
        }

        viewModel.uploadResult.observe(this) { response ->
            handleApiResponse(response)
            progressBar.visibility = View.GONE
            mainContent.visibility = View.VISIBLE
        }

        binding.backToAnalyzeButton.setOnClickListener { backToAnalyze() }
    }

    private fun backToAnalyze() {
        super.finish()
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
    }

    private fun createImagePart(filePath: String): MultipartBody.Part {
        val file = File(filePath)
        val requestFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
        return MultipartBody.Part.createFormData("image", file.name, requestFile)
    }

    @SuppressLint("SetTextI18n", "DefaultLocale")
    private fun handleApiResponse(response: ClassifyResponse) {
        val errorMessageView = findViewById<TextView>(R.id.error_message)

        if (response.status == "true") {
            errorMessageView.visibility = View.GONE

            val phase = response.data?.phase
            val result = response.data?.result
            val confidenceScore = response.data?.confidenceScore

            val formattedConfidenceScore = String.format("%.2f", confidenceScore)
            binding.resultStage.text = "$phase ($formattedConfidenceScore% confidence)"

            binding.resultFeed.text = result.toString()

            when (result) {
                1 -> {
                    binding.resultFeed.text = getString(R.string.larva_phase_1_recommendation)
                    binding.resultDuration.text = getString(R.string.larva_phase_1_duration)
                    binding.evaluation.text = getString(R.string.larva_phase_1_evaluation)
                    binding.resultNotes.text = getString(R.string.larva_phase_1_action)
                }
                2 -> {
                    binding.resultFeed.text = getString(R.string.larva_phase_2_recommendation)
                    binding.resultDuration.text = getString(R.string.larva_phase_2_duration)
                    binding.evaluation.text = getString(R.string.larva_phase_2_evaluation)
                    binding.resultNotes.text = getString(R.string.larva_phase_2_action)
                }
                3 -> {
                    binding.resultFeed.text = getString(R.string.larva_phase_3_recommendation)
                    binding.resultDuration.text = getString(R.string.larva_phase_3_duration)
                    binding.evaluation.text = getString(R.string.larva_phase_3_evaluation)
                    binding.resultNotes.text = getString(R.string.larva_phase_3_action)
                }
                4 -> {
                    binding.resultFeed.text = getString(R.string.maggot_pre_pupa_recommendation)
                    binding.resultDuration.text = getString(R.string.maggot_pre_pupa_duration)
                    binding.evaluation.text = getString(R.string.maggot_pre_pupa_evaluation)
                    binding.resultNotes.text = getString(R.string.maggot_pre_pupa_action)
                }
                5 -> {
                    binding.resultFeed.text = getString(R.string.prepupa_recommendation)
                    binding.resultDuration.text = getString(R.string.prepupa_duration)
                    binding.evaluation.text = getString(R.string.prepupa_evaluation)
                    binding.resultNotes.text = getString(R.string.prepupa_action)
                }
                6 -> {
                    binding.resultFeed.text = getString(R.string.pupa_recommendation)
                    binding.resultDuration.text = getString(R.string.pupa_duration)
                    binding.evaluation.text = getString(R.string.pupa_evaluation)
                    binding.resultNotes.text = getString(R.string.pupa_action)
                }
                else -> {
                    binding.resultStage.text = getString(R.string.unknown_phase)
                    binding.resultDuration.text = getString(R.string.n_a)
                    binding.evaluation.text = getString(R.string.no_evaluation_available)
                    binding.resultNotes.text = getString(R.string.no_action_available)
                }
            }
        } else {
            binding.resultStage.text = getString(R.string.unknown_phase)
            binding.resultDuration.text = getString(R.string.n_a)
            binding.evaluation.text = getString(R.string.no_evaluation_available)
            binding.resultNotes.text = getString(R.string.no_action_available)

            errorMessageView.visibility = View.VISIBLE
            errorMessageView.text = response.message ?: getString(R.string.unknown_error)
        }
    }
}
