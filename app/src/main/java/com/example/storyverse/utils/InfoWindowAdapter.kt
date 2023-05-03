package com.example.storyverse.utils

import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.storyverse.R
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker

class InfoWindowAdapter(private val context: Context, private val markerStoryMap: HashMap<Marker, String>) : GoogleMap.InfoWindowAdapter {

    val view: View = LayoutInflater.from(context).inflate(R.layout.custom_marker_snippet, null)

    private val markerImage: ImageView = view.findViewById(R.id.iv_marker_photo)
    private val markerTitle: TextView = view.findViewById(R.id.tv_marker_name)
    private val markerDesc: TextView = view.findViewById(R.id.tv_marker_description)

    override fun getInfoWindow(marker: Marker): View? {
        return null
    }

    override fun getInfoContents(marker: Marker): View {

        Glide.with(context)
            .load(markerStoryMap[marker].toString())
            .transform(CenterCrop(), RoundedCorners(10))
            .placeholder(R.drawable.ic_baseline_image_24)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(markerImage)

        markerTitle.text = marker.title
        markerDesc.text = marker.snippet

        return view
    }
}