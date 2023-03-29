package com.example.my_movie_app.ui.profile

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import com.example.my_movie_app.App
import com.example.my_movie_app.R
import com.example.my_movie_app.copyInputStreamToFile
import com.example.my_movie_app.databinding.FragmentProfileBinding
import com.example.my_movie_app.login.LoginActivity
import com.example.my_movie_app.toIso
import com.google.android.material.snackbar.Snackbar
import java.io.File
import java.io.InputStream
import java.util.*

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ProfileViewModel by lazy {
        ViewModelProvider(this)[ProfileViewModel::class.java]
    }
    private var galleryPermissionResultLauncher: ActivityResultLauncher<String>? = null
    private var openPictureResultLauncher: ActivityResultLauncher<String>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        galleryPermissionResultLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission(),
            galleryPermissionResultCallback
        )
        openPictureResultLauncher = registerForActivityResult(
            ActivityResultContracts.GetContent(),
            openPictureResultCallback
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onStart() {
        super.onStart()
        binding.logOut.setOnClickListener {
            val ed: SharedPreferences.Editor =
                App.appContext.getSharedPreferences(
                    getString(R.string.app_name),
                    AppCompatActivity.MODE_PRIVATE
                )
                    .edit()
            ed.remove(getString(R.string.token))
            ed.apply()
            startActivity(Intent(requireActivity(), LoginActivity::class.java))
            requireActivity().finish()
        }

        binding.imageProfile.setOnClickListener {
            if (shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)) {

            } else {
                galleryPermissionResultLauncher?.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        }

        viewModel.onGetData().observe(viewLifecycleOwner) {
            if (it != null) {
                binding.nameProfile.text =
                    it.firstName + " " + it.name + " " + (it.lastName?.first() ?: "")
                Glide
                    .with(requireContext())
                    .load(
                        GlideUrl(
                            "http://n931333e.beget.tech/api/v3/img/" + it.imageUrl,
                            LazyHeaders.Builder()
                                .addHeader("User-Agent", "Mozilla/5.0")
                                .build()
                        )
                    )
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .centerCrop()
                    .error(R.drawable.logo)
                    .into(binding.imageProfile)
            }
        }
        viewModel.onGetErrorMessage().observe(viewLifecycleOwner) {
            Snackbar.make(binding.root, it, Snackbar.LENGTH_SHORT).show()
        }
        viewModel.onLoadData()
    }

    override fun onDestroy() {
        galleryPermissionResultLauncher?.unregister()
        openPictureResultLauncher?.unregister()
        super.onDestroy()
    }

    private val galleryPermissionResultCallback = ActivityResultCallback<Boolean> {
        if (it) {
            openPictureResultLauncher?.launch("*/*")
        }
    }

    private val openPictureResultCallback = ActivityResultCallback<Uri?> {
        if (it != null) {
            val openIS = requireContext().contentResolver.openInputStream(it)
                ?: return@ActivityResultCallback
            val realPath = writeStreamToDisk(
                context = requireContext(),
                inputStream = openIS,
                mimeType = MimeTypeMap.getSingleton()
                    .getExtensionFromMimeType(requireContext().contentResolver.getType(it)) ?: ""
            )
            viewModel.changeImageProfile(realPath.absolutePath)
        }
    }

    private fun writeStreamToDisk(
        context: Context,
        inputStream: InputStream,
        mimeType: String
    ): File {
        val name = "${Date().toIso().replace(":", "-")}-${System.currentTimeMillis()}.$mimeType"
        val dir = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)
        val pathBuilder = StringBuilder()
        pathBuilder.append("$dir/$name")
        val futureStudioIconFile = File(pathBuilder.toString())
        if (!futureStudioIconFile.exists()) {
            futureStudioIconFile.createNewFile()
        }
        futureStudioIconFile.copyInputStreamToFile(inputStream)
        return futureStudioIconFile
    }
}