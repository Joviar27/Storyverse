package com.example.storyverse.ui.addstory

import android.Manifest
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.MenuHost
import androidx.fragment.app.Fragment
import androidx.core.view.MenuProvider
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.findNavController
import com.example.storyverse.R
import com.example.storyverse.databinding.FragmentAddStoryBinding
import com.example.storyverse.ui.camera.CameraActivity
import com.example.storyverse.utils.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class AddStoryFragment : Fragment(), MenuProvider {

    private var _binding : FragmentAddStoryBinding? = null
    private val binding get() = _binding

    private var file : File? = null

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (!isGranted) {
            Toast.makeText(
                requireActivity(),
                resources.getString(R.string.permission_not_granted),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private val launcherIntentCameraX = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){
        if(it.resultCode == CAMERA_X_RESULT){
            val myFile = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                it.data?.getSerializableExtra("picture", File::class.java)
            } else {
                @Suppress("DEPRECATION")
                it.data?.getSerializableExtra("picture")
            } as File

            file = myFile

            val isBackCamera = it.data?.getBooleanExtra("isBackCamera", true) as Boolean

            val rotation = it.data?.getIntExtra("rotation", 0) as Int

            rotateFileForCamera(
                myFile,
                isBackCamera,
                rotation
            )

            val compressedFile = reduceFileImageFast(myFile)
            binding?.ivPreviewPhoto?.setImageBitmap(BitmapFactory.decodeFile(compressedFile.path))
        }
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){
        if (it.resultCode == AppCompatActivity.RESULT_OK){
            val selectedImg : Uri = it.data?.data as Uri

            val myFile = uriToFile(selectedImg, requireActivity())
            rotateFileForGallery(myFile)
            val compressed = reduceFileImageFast(myFile)

            file = compressed

            binding?.ivPreviewPhoto?.setImageURI(selectedImg)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAddStoryBinding.inflate(inflater,container,false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (savedInstanceState != null) {
            val byteArray = savedInstanceState.getByteArray("myFile")
            if (byteArray != null) {
                file = byteArrayToFile(requireActivity(), byteArray)
                val myFile = file as File
                binding?.ivPreviewPhoto?.setImageBitmap(BitmapFactory.decodeFile(myFile.path))
            }
        }

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)

        (requireActivity() as AppCompatActivity).supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24)
        }

        if(!allPermissionGranted()){
            requestPermission()
        }

        playAnimation()
        setButtonEnabled()

        binding?.edAddDescription?.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                //nothing
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                setButtonEnabled()
            }

            override fun afterTextChanged(s: Editable?) {
                //nothing
            }

        })

        binding?.buttonCamera?.setOnClickListener {
            startCameraX()
            setButtonEnabled()
        }
        binding?.buttonGallery?.setOnClickListener {
            startGallery()
            setButtonEnabled()
        }
        binding?.buttonAdd?.setOnClickListener {
            showLoading(true)
            disableButton()
            uploadImage()
        }
    }

    private fun requestPermission() {
        requestPermissionLauncher.launch(Manifest.permission.CAMERA)
    }

    private fun allPermissionGranted() = REQUIRED_PERMISSION.all {
        ContextCompat.checkSelfPermission(requireContext(), it) == PackageManager.PERMISSION_GRANTED
    }

    private fun startCameraX() {
        val intent = Intent(requireActivity(), CameraActivity::class.java)
        launcherIntentCameraX.launch(intent)
    }

    private fun startGallery() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"

        val chooserIntent = Intent.createChooser(intent, resources.getString(R.string.choose_picture))
        launcherIntentGallery.launch(chooserIntent)
    }

    private fun uploadImage() {
        val viewModel = obtainViewModel()
        val description = binding?.edAddDescription?.text.toString()

        if(file != null){
            val myFile = file as File
            val compressedFile = reduceFileImage(myFile)

            val descriptionBody = description.toRequestBody("text/plain".toMediaType())
            val requestImageFile = compressedFile.asRequestBody("image/jpeg".toMediaTypeOrNull())

            val imageMultipart : MultipartBody.Part = MultipartBody.Part.createFormData(
                "photo",
                myFile.name,
                requestImageFile
            )

            viewModel.addStory(imageMultipart, descriptionBody).observe(viewLifecycleOwner){ result ->
                when(result){
                    is ResultState.Loading -> {
                        showLoading(true)
                    }
                    is ResultState.Error ->{
                        showLoading(false)
                        Toast.makeText(
                            requireActivity(),
                            result.error,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    is ResultState.Success ->{
                        showLoading(false)
                        Toast.makeText(
                            requireActivity(),
                            resources.getString(R.string.add_story_success),
                            Toast.LENGTH_SHORT
                        ).show()
                        val back = AddStoryFragmentDirections.actionAddStoryFragmentToListStoryFragment2()
                        view?.findNavController()?.navigate(back)
                    }
                }
            }
        }
    }

    private fun obtainViewModel() : AddStoryViewModel {
        val factory : AddStoryViewModelFactory = AddStoryViewModelFactory.getInstance(requireActivity())
        val viewModel : AddStoryViewModel by viewModels {
            factory
        }
        return viewModel
    }

    private fun showLoading(isLoading : Boolean){
        binding?.progressBar?.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun setButtonEnabled(){
        val description = binding?.edAddDescription?.text.toString()
        binding?.buttonAdd?.isEnabled = description.isNotEmpty() && file != null
    }

    private fun disableButton(){
        binding?.buttonAdd?.text = resources.getString(R.string.submit)
        binding?.buttonAdd?.isEnabled = false
    }

    private fun playAnimation(){

        val btnCamera = ObjectAnimator.ofFloat(binding?.buttonCamera, View.ALPHA, 1f).setDuration(500)
        val btnGallery = ObjectAnimator.ofFloat(binding?.buttonGallery, View.ALPHA,1f).setDuration(500)

        val desc = ObjectAnimator.ofFloat(binding?.edAddDescription, View.ALPHA,1f).setDuration(500)

        val btnAdd = ObjectAnimator.ofFloat(binding?.buttonAdd, View.ALPHA,1f).setDuration(500)

        val together = AnimatorSet().apply {
            playTogether(btnCamera, btnGallery)
        }

        AnimatorSet().apply {
            playSequentially(together,desc,btnAdd)
            start()
        }
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.menu_blank, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        when(menuItem.itemId){
            android.R.id.home ->{
                view?.findNavController()?.navigateUp()
            }
        }
        return true
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if(file != null){
            outState.putByteArray("myFile", fileToByteArray(file as File))
        }
    }

    companion object{
        const val CAMERA_X_RESULT = 200
        private val REQUIRED_PERMISSION = arrayOf(Manifest.permission.CAMERA)
    }

}