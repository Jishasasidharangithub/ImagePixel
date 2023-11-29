package com.image.imagepixel

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.image.imagepixel.databinding.ActivityMainBinding
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class MainActivity : AppCompatActivity(){

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        val selectImageButton: Button = binding.selectImageGalleryButton

        selectImageButton.setOnClickListener {
            openGallery()
        }

    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            val selectedImageUri: Uri = data.data ?: return
            println("--------------------------->url${selectedImageUri}")

            val options = BitmapFactory.Options()
            options.inJustDecodeBounds = true

            // Decode image bounds without loading into memory
            BitmapFactory.decodeStream(
                contentResolver.openInputStream(selectedImageUri),
                null,
                options
            )

            // Check the image dimensions and DPI
            if (options.outWidth > 50 || options.outHeight > 50 || options.inDensity < 72) {
                // Image does not meet the requirements
                showToast("Image does not meet the requirements")
                return
            }
            val originalBitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedImageUri)
            binding?.imageView?.setImageBitmap(originalBitmap)

            saveBitmap(originalBitmap, "sample_image.png")
        }
    }
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun saveBitmap(bitmap: Bitmap, fileName: String) {
        val folder = Environment.getExternalStorageDirectory()
        val file = File(folder, fileName)

        try {
            val stream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
            stream.flush()
            stream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    companion object {
        private const val PICK_IMAGE_REQUEST = 1
    }
}