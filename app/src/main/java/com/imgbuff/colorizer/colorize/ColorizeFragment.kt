package com.imgbuff.colorizer.colorize

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.imgbuff.colorizer.R
import com.imgbuff.colorizer.bindImage
import com.imgbuff.colorizer.databinding.FragmentColorizeBinding

const val PICK_PHOTO = 1

class ColorizeFragment: Fragment() {

    private val viewModel: ColorizeViewModel by lazy {
        ViewModelProvider(this).get(ColorizeViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding: FragmentColorizeBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_colorize,
            container,
            false
        )
        // Allows Data Binding to Observe LiveData with the lifecycle of this Fragment
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        // set on click listener(s)
        binding.imageSelectionButton.setOnClickListener { startPhotoPickIntent() }

        // observe live data
        viewModel.colorizedImage.observe(viewLifecycleOwner, Observer {
            if (it != null)
                startPhotoViewIntent(it.url)
        })

        return binding.root
    }

    private fun startPhotoViewIntent(uri: String) {
        val intent = Intent()
        intent.action = Intent.ACTION_VIEW
        intent.setDataAndType(Uri.parse(uri), "image/*")
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }

    private fun startPhotoPickIntent() {
        val photoPickerIntent = Intent(Intent.ACTION_GET_CONTENT)
        photoPickerIntent.type = "image/*"
        startActivityForResult(Intent.createChooser(photoPickerIntent, "Select Photo"), PICK_PHOTO)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_PHOTO && resultCode == Activity.RESULT_OK) {
            viewModel.colorize(context, data?.data)
        }
    }


    private fun Uri.toBitmap(): Bitmap {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val source: ImageDecoder.Source = ImageDecoder.createSource(
                requireActivity().contentResolver,
                this
            )
            ImageDecoder.decodeBitmap(source)
        } else {
            MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, this)
        }
    }

}