package com.example.storyverse.ui.register

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import com.example.storyverse.R
import com.example.storyverse.databinding.FragmentRegisterBinding
import com.example.storyverse.utils.ResultState
import androidx.navigation.findNavController
import com.example.storyverse.utils.hideSystemUI

class RegisterFragment : Fragment(){

    private var _binding : FragmentRegisterBinding? = null
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentRegisterBinding.inflate(inflater,container,false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        hideSystemUI(requireActivity() as AppCompatActivity)
        playAnimation()
        setupView()
    }

    private fun setupView(){
        binding?.btnRegister?.setOnClickListener{
            val name = binding?.edRegisterName?.text.toString()
            val email = binding?.edRegisterEmail?.text.toString()
            val password = binding?.edRegisterPassword?.text.toString()
            Log.d(TAG, "In registerFragment name : $name, email : $email, pass : $password")
            registerUser(name, email, password)
        }

        binding?.tvSignIn?.setOnClickListener{
            val toSignIn = RegisterFragmentDirections.actionRegisterFragmentToLoginFragment()
            view?.findNavController()?.navigate(toSignIn)
        }
    }

    private fun obtainViewModel() : RegisterViewModel{
        val factory : RegisterViewModelFactory = RegisterViewModelFactory.getInstance(requireActivity())
        val viewModel : RegisterViewModel by viewModels {
            factory
        }
        return viewModel
    }

    private fun showLoading(isLoading : Boolean){
        binding?.progressBar?.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun playAnimation(){
        ObjectAnimator.ofFloat(binding?.ivLogo, View.TRANSLATION_X, -30f, 30f).apply{
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val nameEdt = ObjectAnimator.ofFloat(binding?.edRegisterName, View.ALPHA, 1f).setDuration(500)
        val emailEdt = ObjectAnimator.ofFloat(binding?.edRegisterEmail, View.ALPHA,1f).setDuration(500)
        val passwordEdt = ObjectAnimator.ofFloat(binding?.edRegisterPassword, View.ALPHA,1f).setDuration(500)

        val register = ObjectAnimator.ofFloat(binding?.btnRegister, View.ALPHA,1f).setDuration(500)

        val redirect = ObjectAnimator.ofFloat(binding?.tvSignIn, View.ALPHA,1f).setDuration(500)

        val together = AnimatorSet().apply {
            playTogether(nameEdt, emailEdt, passwordEdt)
        }

        AnimatorSet().apply {
            playSequentially(together,register, redirect)
            start()
        }
    }

    private fun registerUser(name: String, email: String, password: String) {
        val viewModel = obtainViewModel()
        when {
            name.isEmpty() -> binding?.edRegisterName?.error = resources.getString(R.string.name_empty)
            email.isEmpty() -> binding?.edRegisterEmail?.error = resources.getString(R.string.email_empty)
            password.isEmpty() -> binding?.edRegisterPassword?.error = resources.getString(R.string.pass_empty)
            else -> {
                viewModel.register(name, email, password).observe(viewLifecycleOwner) { result ->
                    when (result) {
                        is ResultState.Loading -> showLoading(true)
                        is ResultState.Error -> {
                            showLoading(false)
                            var error = result.error
                            when(error){
                                "HTTP 400 Bad Request" ->{
                                    error = resources.getString(R.string.check_format)
                                }
                            }
                            Toast.makeText(
                                context,
                                resources.getString(R.string.failed_to_register, error),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        is ResultState.Success -> {
                            Toast.makeText(
                                context,
                                resources.getString(R.string.register_success),
                                Toast.LENGTH_SHORT
                            ).show()
                            val toLogin = RegisterFragmentDirections.actionRegisterFragmentToLoginFragment()
                            view?.findNavController()?.navigate(toLogin)
                            showLoading(false)
                        }
                    }
                }
            }
        }
    }
}