package com.example.storyverse.ui.splashscreen

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.example.storyverse.databinding.FragmentSplashScreenBinding
import com.example.storyverse.utils.ResultState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class SplashScreenFragment : Fragment() {

    private var _binding : FragmentSplashScreenBinding? = null
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSplashScreenBinding.inflate(inflater,container,false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (requireActivity() as AppCompatActivity).supportActionBar?.hide()
        //checkAuthState()
    }

    private fun obtainViewModel() : SplashViewModel {
        val factory : SplashViewModelFactory = SplashViewModelFactory.getInstance(requireActivity())
        val viewModel : SplashViewModel by viewModels {
            factory
        }
        return viewModel
    }

//    private fun checkAuthState(){
//        val viewModel = obtainViewModel()
//        viewModel.checkAuthState().observe(viewLifecycleOwner){ result->
//            when(result){
//                is ResultState.Loading ->{
//                    lifecycleScope.launch {
//                        delay(3000)
//                    }
//                }
//                is ResultState.Error -> {
//                    val toLogin = SplashScreenFragmentDirections.actionSplashScreenFragmentToLoginFragment()
//                    view?.findNavController()?.navigate(toLogin)
//                    Log.e(TAG, "check auth state : ${result.error}")
//                }
//                is ResultState.Success ->{
//                    if(result.data?.state!!){
//                        val toListStory = SplashScreenFragmentDirections.actionSplashScreenFragmentToListStoryFragment()
//                        view?.findNavController()?.navigate(toListStory)
//                    }
//                    else{
//                        val toLogin = SplashScreenFragmentDirections.actionSplashScreenFragmentToLoginFragment()
//                        view?.findNavController()?.navigate(toLogin)
//                    }
//                }
//            }
//        }
//    }
}