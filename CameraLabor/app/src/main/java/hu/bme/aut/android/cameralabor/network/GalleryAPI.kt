package hu.bme.aut.android.cameralabor.network

import hu.bme.aut.android.cameralabor.model.Image
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface GalleryAPI {

    companion object {
        const val ENDPOINT_URL = "https://aut-android-gallery.herokuapp.com/api/"
        const val IMAGE_PREFIX_URL = "https://aut-android-gallery.herokuapp.com/"

        const val MULTIPART_FORM_DATA = "multipart/form-data"
        const val PHOTO_MULTIPART_KEY_IMG = "image"
    }

    @GET("images")
    fun getImages(): Call<List<Image>>

    @Multipart
    @POST("upload")
    fun uploadImage(@Part file: MultipartBody.Part,
                    @Part("name") name: RequestBody,
                    @Part("description") description: RequestBody): Call<ResponseBody>

}