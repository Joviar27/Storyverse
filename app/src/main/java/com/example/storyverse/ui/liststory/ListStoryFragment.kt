package com.example.storyverse.ui.liststory

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.storyverse.R
import com.example.storyverse.databinding.FragmentListStoryBinding
import com.example.storyverse.domain.entity.StoryEntity
import com.example.storyverse.utils.LoadingStateAdapter
import com.example.storyverse.utils.ResultState
import com.example.storyverse.utils.StoryAdapter
import com.example.storyverse.utils.showSystemUI

class ListStoryFragment : Fragment(), MenuProvider {

    private var _binding : FragmentListStoryBinding? = null
    private val binding get() = _binding

    private var _storyAdapter : StoryAdapter? = null
    private val storyAdapter get() = _storyAdapter

    private var _viewModel: ListStoryViewModel? = null
    private val viewModel get() = _viewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        obtainViewModel()
    }

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

//        (requireActivity() as AppCompatActivity).also {
//            showSystemUI(it)
//        }.supportActionBar?.apply {
//            setDisplayHomeAsUpEnabled(false)
//            setDisplayShowHomeEnabled(false)
//        }

        _storyAdapter = StoryAdapter(object : StoryAdapter.OnItemClicked{
            override fun onClicked(story: StoryEntity) {
                val toDetailStory = ListStoryFragmentDirections.actionListStoryFragmentToDetailStoryFragment(story)
                toDetailStory.story = story
                view.findNavController().navigate(toDetailStory)
            }
        })

        getStoryPaged()
    }

    private fun obtainViewModel() {
        val factory : ListStoryViewModelFactory = ListStoryViewModelFactory.getInstance(requireActivity())
        val viewModel : ListStoryViewModel by viewModels {
            factory
        }
        _viewModel = viewModel
    }

    private fun showLoading(isLoading : Boolean){
        binding?.progressBar?.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

//    private fun getStoryList(){
//        viewModel?.getStoryList(0)?.observe(viewLifecycleOwner){ result ->
//            when(result){
//                is ResultState.Loading -> showLoading(true)
//                is ResultState.Error ->{
//                    showLoading(false)
//                    Toast.makeText(
//                        context,
//                        result.error,
//                        Toast.LENGTH_SHORT
//                    ).show()
//                }
//                is ResultState.Success ->{
//                    showLoading(false)
//                    val stories = result.data
//                    storyAdapter?.submitList(stories)
//                }
//            }
//        }
//
//        binding?.rvStory?.apply {
//            layoutManager = LinearLayoutManager(requireActivity())
//            setHasFixedSize(true)
//            adapter = storyAdapter
//        }
//    }

    private fun getStoryPaged(){
        viewModel?.story?.observe(viewLifecycleOwner){
            storyAdapter?.submitData(lifecycle, it) //used to insert data into PagingDataAdapter
        }

        binding?.rvStory?.apply {
            layoutManager = LinearLayoutManager(requireActivity())
            setHasFixedSize(true)
            adapter = storyAdapter?.withLoadStateFooter(
                footer = LoadingStateAdapter{
                    storyAdapter?.retry()
                }
            )
            scrollToPosition(0)
        }
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.menu,menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        when(menuItem.itemId){
            R.id.logout -> {
                viewModel?.logout()?.observe(viewLifecycleOwner){ result ->
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

                            val toLogin = ListStoryFragmentDirections.actionListStoryFragmentToLoginFragment()
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
            R.id.maps->{
                val toMaps = ListStoryFragmentDirections.actionListStoryFragmentToMapStoryActivity()
                view?.findNavController()?.navigate(toMaps)
            }
        }
        return true
    }
}