package com.example.storyverse.ui.liststory

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.findNavController
import androidx.navigation.fragment.FragmentNavigator
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.storyverse.R
import com.example.storyverse.databinding.FragmentListStoryBinding
import com.example.storyverse.domain.entity.StoryEntity
import com.example.storyverse.utils.ResultState
import com.example.storyverse.utils.StoryAdapter
import com.example.storyverse.utils.showSystemUI

class ListStoryFragment : Fragment(), MenuProvider {

    private var _binding : FragmentListStoryBinding? = null
    private val binding get() = _binding

    private var _storyAdapter : StoryAdapter? = null
    private val storyAdapter get() = _storyAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentListStoryBinding.inflate(inflater,container,false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)

        (requireActivity() as AppCompatActivity).also {
            showSystemUI(it)
        }.supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(false)
            setDisplayShowHomeEnabled(false)
        }

        _storyAdapter = StoryAdapter(object : StoryAdapter.OnItemClicked{
            override fun onClicked(story: StoryEntity) {
                val toDetailStory = ListStoryFragmentDirections.actionListStoryFragmentToDetailStoryFragment(story)
                toDetailStory.story = story
                view.findNavController().navigate(toDetailStory)
            }
        })

        getStoryList()
    }

    private fun obtainViewModel() : ListStoryViewModel {
        val factory : ListStoryViewModelFactory = ListStoryViewModelFactory.getInstance(requireActivity())
        val viewModel : ListStoryViewModel by viewModels {
            factory
        }
        return viewModel
    }

    private fun showLoading(isLoading : Boolean){
        binding?.progressBar?.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun getStoryList(){
        val viewModel = obtainViewModel()

        viewModel.getStoryList().observe(viewLifecycleOwner){ result ->
            when(result){
                is ResultState.Loading -> showLoading(true)
                is ResultState.Error ->{
                    showLoading(false)
                    Toast.makeText(
                        context,
                        result.error,
                        Toast.LENGTH_SHORT
                    ).show()
                }
                is ResultState.Success ->{
                    showLoading(false)
                    val data = result.data
                    storyAdapter?.submitList(data)
                }
            }
        }

        binding?.rvStory?.apply {
            layoutManager = LinearLayoutManager(requireActivity())
            setHasFixedSize(true)
            adapter = storyAdapter
        }
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.menu,menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        val viewModel = obtainViewModel()

        when(menuItem.itemId){
            R.id.logout -> {
                viewModel.logout().observe(viewLifecycleOwner){ result ->
                    when(result){
                        is ResultState.Loading -> showLoading(true)
                        is ResultState.Error ->{
                            showLoading(false)
                            Toast.makeText(
                                context,
                                result.error,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        is ResultState.Success -> {
                            showLoading(false)
                            Toast.makeText(
                                context,
                                resources.getString(R.string.logout_success),
                                Toast.LENGTH_SHORT
                            ).show()

                            val toLogin = ListStoryFragmentDirections.actionListStoryFragmentToSplashScreenFragment()
                            view?.findNavController()?.navigate(toLogin)
                        }
                    }
                }
            }
            R.id.addStory ->{
                val toAdd = ListStoryFragmentDirections.actionListStoryFragmentToAddStoryFragment()
                view?.findNavController()?.navigate(toAdd)
            }
            R.id.language ->{
                startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
            }
        }
        return true
    }
}