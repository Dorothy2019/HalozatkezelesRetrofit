package hu.bme.aut.android.cameralabor.network

import android.net.Uri
import android.os.Handler
import hu.bme.aut.android.cameralabor.model.Image
import hu.bme.aut.android.cameralabor.network.GalleryAPI.Companion.MULTIPART_FORM_DATA
import hu.bme.aut.android.cameralabor.network.GalleryAPI.Companion.PHOTO_MULTIPART_KEY_IMG
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.MediaType.Companion.toMediaTypeOrNull

class GalleryInteractor {

    private val galleryApi: GalleryAPI

    init {
        val retrofit = Retrofit.Builder()
                .baseUrl(GalleryAPI.ENDPOINT_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        this.galleryApi = retrofit.create(GalleryAPI::class.java)
    }

    private fun <T> runCallOnBackgroundThread(
        call: Call<T>,
        onSuccess: (T) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        val handler = Handler()
        Thread {
            try {
                val response = call.execute().body()!!
                handler.post { onSuccess(response) }

            } catch (e: Exception) {
                e.printStackTrace()
                handler.post { onError(e) }
            }
        }.start()
    }

    fun getImages(
        onSuccess: (List<Image>) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        val getImagesRequest = galleryApi.getImages()
        runCallOnBackgroundThread(getImagesRequest, onSuccess, onError)
    }

    fun uploadImage(
        fileUri: Uri,
        name: String,
        description: String,
        onSuccess: (ResponseBody) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        val file = File(fileUri.path)
        val requestFile = file.asRequestBody(MULTIPART_FORM_DATA.toMediaTypeOrNull())
        val body = MultipartBody.Part.createFormData(PHOTO_MULTIPART_KEY_IMG, file.name, requestFile)

        val nameParam = name.toRequestBody(MultipartBody.FORM)
        val descriptionParam = description.toRequestBody(MultipartBody.FORM)

        val uploadImageRequest = galleryApi.uploadImage(body, nameParam, descriptionParam)
        runCallOnBackgroundThread(uploadImageRequest, onSuccess, onError)
    }
}