package com.example.storyverse.ui.detail

import android.os.Bundle
import android.transition.TransitionInflater
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.storyverse.R
import com.example.storyverse.databinding.FragmentDetailStoryBinding
import com.example.storyverse.domain.entity.StoryEntity

class DetailStoryFragment : Fragment(), MenuProvider {

    private var _binding : FragmentDetailStoryBinding? = null
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentDetailStoryBinding.inflate(inflater,container,false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)

        (requireActivity() as AppCompatActivity).supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24)
        }

        val story = DetailStoryFragmentArgs.fromBundle(arguments as Bundle).story
        setupView(story)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        (requireActivity() as AppCompatActivity).supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(false)
            setDisplayShowHomeEnabled(false)
            setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24)
        }
    }

    private fun setupView(story : StoryEntity){
        binding?.tvDetailName?.text = story.name
        binding?.tvDetailDescription?.text = story.description

        Glide.with(requireActivity())
            .load(story.photoUri)
            .placeholder(R.drawable.ic_baseline_image_24)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(binding!!.ivDetailPhoto)
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
}