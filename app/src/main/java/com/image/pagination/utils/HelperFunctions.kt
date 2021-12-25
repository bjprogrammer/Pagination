package com.image.pagination.utils

import android.content.Context
import java.io.IOException
import java.nio.charset.StandardCharsets

object HelperFunctions {

    fun getImage(context: Context, imageName: String?): Int {
        return context.resources.getIdentifier(imageName, "drawable", context.packageName)
    }

    //Converting json file (stored in assets folder) in string format
    fun fetchData(context: Context, fileName: String?): String? {
        var jsonString: String? = null
        try {
            val inputStream = context.assets.open(fileName!!)
            val sizeOfJSONFile = inputStream.available()
            val bytes = ByteArray(sizeOfJSONFile)
            inputStream.read(bytes)
            inputStream.close()
            jsonString = String(bytes, StandardCharsets.UTF_8)
        } catch (ex: IOException) {
            ex.printStackTrace()
        }
        return jsonString
    }
}
