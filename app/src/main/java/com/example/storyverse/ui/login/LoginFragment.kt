package com.example.storyverse.ui.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.example.storyverse.R
import com.example.storyverse.databinding.FragmentLoginBinding
import com.example.storyverse.utils.ResultState
import com.example.storyverse.utils.hideSystemUI

class LoginFragment : Fragment(){

    private var _binding : FragmentLoginBinding? = null
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentLoginBinding.inflate(inflater,container,false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        hideSystemUI(requireActivity() as AppCompatActivity)
        playAnimation()
        setupView()

    }

    private fun setupView(){
        binding?.btnSignIn?.setOnClickListener{
            val email = binding?.edLoginEmail?.text.toString()
            val password = binding?.edLoginPassword?.text.toString()
            signInUser(email, password)
        }

        binding?.tvRegister?.setOnClickListener{
            val toRegister = LoginFragmentDirections.actionLoginFragmentToRegisterFragment()
            view?.findNavController()?.navigate(toRegister)
        }
    }

    private fun obtainViewModel() : LoginViewModel {
        val factory : LoginViewModelFactory = LoginViewModelFactory.getInstance(requireActivity())
        val viewModel : LoginViewModel by viewModels {
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

        val emailEdt = ObjectAnimator.ofFloat(binding?.edLoginEmail, View.ALPHA,1f).setDuration(500)
        val passwordEdt = ObjectAnimator.ofFloat(binding?.edLoginPassword, View.ALPHA,1f).setDuration(500)

        val login = ObjectAnimator.ofFloat(binding?.btnSignIn, View.ALPHA,1f).setDuration(500)

        val redirect = ObjectAnimator.ofFloat(binding?.tvRegister, View.ALPHA,1f).setDuration(500)

        val together = AnimatorSet().apply {
            playTogether(emailEdt, passwordEdt)
        }

        AnimatorSet().apply {
            playSequentially(together,login, redirect)
            start()
        }
    }

    private fun signInUser(email : String, password : String){
        val viewModel = obtainViewModel()
        when {
            email.isEmpty() -> binding?.edLoginEmail?.error = resources.getString(R.string.email_empty)
            password.isEmpty() -> binding?.edLoginPassword?.error = resources.getString(R.string.pass_empty)
            else -> {
                viewModel.login(email, password).observe(viewLifecycleOwner) { result ->
                    when (result) {
                        is ResultState.Loading -> showLoading(true)
                        is ResultState.Error -> {
                            showLoading(false)
                            var error = result.error
                            when(error){
                                "HTTP 401 Unauthorized" ->{
                                    error = resources.getString(R.string.wrong_email_or_pass)
                                }
                                "HTTP 400 Bad Request" ->{
                                    error = resources.getString(R.string.check_format)
                                }

                            }
                            Toast.makeText(
                                context,
                                resources.getString(R.string.failed_to_login, error),
                                Toast.LENGTH_SHORT
                            ).show()
                            Log.d(TAG, result.error)
                        }
                        is ResultState.Success -> {
                            Toast.makeText(
                                context,
                                resources.getString(R.string.login_success),
                                Toast.LENGTH_SHORT
                            ).show()
                            val toListStory = LoginFragmentDirections.actionLoginFragmentToListStoryFragment()
                            view?.findNavController()?.navigate(toListStory)
                            showLoading(false)
                        }
                    }
                }
            }
        }
    }
}