package hu.bme.aut.android.cameralabor.model

import com.google.gson.annotations.SerializedName

data class Image(
        @SerializedName("_id")
        val id: String,
        val name: String,
        val description: String,
        val timestamp: Long,
        val url: String,
        val size: Long,
        val mimetype: String,
        val encoding: String
)