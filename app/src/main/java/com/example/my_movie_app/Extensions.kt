package com.example.my_movie_app

import java.io.File
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.*

fun Date.toIso(locale: Locale = Locale.getDefault(), timeZone: TimeZone? = null): String {
    val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", locale)
    if (timeZone != null) {
        simpleDateFormat.timeZone = timeZone
    }
    return simpleDateFormat
        .format(this)
        .replace("GMT", "")
}

fun File.copyInputStreamToFile(inputStream: InputStream) {
    this.outputStream().use { fileOut ->
        inputStream.copyTo(fileOut)
    }
}