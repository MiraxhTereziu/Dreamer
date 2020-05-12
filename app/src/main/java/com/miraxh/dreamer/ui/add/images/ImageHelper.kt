package com.miraxh.dreamer.ui.add.images

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import android.webkit.MimeTypeMap
import androidx.fragment.app.Fragment
import com.miraxh.dreamer.data.dream.Dream
import com.miraxh.dreamer.util.FOLDER_IMAGE
import java.io.File


class ImageHelper(var context: Context, var fragment: Fragment) {

    var toRtnFileName: String = "null"

    init {
        pickImageFromGallery()
    }

    private fun pickImageFromGallery() {
        //Intent to pick image
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        fragment.startActivityForResult(intent, 1000)
    }
    //handle result of picked image
    fun Fragment.onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && requestCode == 1000) {
            /*Glide.with(context)
                .load("${data?.data}")
                .into(image)*/

            val uriSourceFile = data?.data

            val cr = context?.contentResolver
            val mime = MimeTypeMap.getSingleton()
            val type = mime.getExtensionFromMimeType(cr?.getType(uriSourceFile!!))
            val sourceFile = File(getRealPathFromURI(uriSourceFile))
            val fileName = "${Dream.createUniqueName()}.$type"
            val destFile = File(Dream.getBasePath(requireContext(), FOLDER_IMAGE),fileName)
            sourceFile.copyTo(destFile,true)
            toRtnFileName = fileName
        }
    }

    private fun getRealPathFromURI(contentUri: Uri?): String? {
        val proj = arrayOf(MediaStore.Audio.Media.DATA)
        val cursor: Cursor? = context?.contentResolver?.query(contentUri!!, proj, null, null, null)
        val column_index: Int? = cursor?.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)
        cursor?.moveToFirst()
        return cursor?.getString(column_index!!)
    }

    fun getFileName(): String {
        return toRtnFileName
    }
}