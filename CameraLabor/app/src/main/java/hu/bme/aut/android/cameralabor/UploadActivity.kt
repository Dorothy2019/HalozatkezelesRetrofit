package hu.bme.aut.android.cameralabor

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.signature.ObjectKey
import hu.bme.aut.android.cameralabor.network.GalleryInteractor
import kotlinx.android.synthetic.main.activity_upload.*
import okhttp3.ResponseBody
import org.greenrobot.eventbus.EventBus
import java.io.File

class UploadActivity : AppCompatActivity() {

    companion object {
        private const val TMP_IMAGE_JPG = "/tmp_image.jpg"
        private val IMAGE_PATH = Environment.getExternalStorageDirectory().absolutePath + TMP_IMAGE_JPG
        private const val REQUEST_CAMERA_IMAGE = 101
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload)

        btnCapture.setOnClickListener {
            val imageFile = File(IMAGE_PATH)
            val imageFileUri = Uri.fromFile(imageFile)
            val cameraIntent = Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE)

            EventBus.getDefault().post({cameraIntent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, imageFileUri)
                    startActivityForResult(cameraIntent, REQUEST_CAMERA_IMAGE)})

        }

        btnUpload.setOnClickListener {
            val galleryInteractor = GalleryInteractor()

            val name = etName.text.toString()
            val description = etDescription.text.toString()

            EventBus.getDefault().post(galleryInteractor.uploadImage(
                fileUri = Uri.fromFile(File(IMAGE_PATH)),
                name = name,
                description = description,
                onSuccess = this::uploadSuccess,
                onError = this::uploadError
            ))

        }
    }

    private fun uploadSuccess(responseBody: ResponseBody) {
        Toast.makeText(this, "Successfully uploaded!", Toast.LENGTH_SHORT).show()
        finish()
    }

    private fun uploadError(e: Throwable) {
        Toast.makeText(this, "Error during uploading photo!", Toast.LENGTH_SHORT).show()
        e.printStackTrace()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CAMERA_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                try {
                    Glide.with(this)
                        .load(Uri.fromFile(File(IMAGE_PATH)))
                        .apply(RequestOptions().signature(ObjectKey(System.currentTimeMillis())))
                        .into(ivImage)

                } catch (t: Throwable) {
                    t.printStackTrace()
                    Toast.makeText(this, "ERROR: " + t.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}
