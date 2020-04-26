package hu.bme.aut.android.cameralabor.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import hu.bme.aut.android.cameralabor.R
import hu.bme.aut.android.cameralabor.model.Image
import hu.bme.aut.android.cameralabor.network.GalleryAPI
import kotlinx.android.synthetic.main.li_image.view.*

class ImagesAdapter(
    private val context: Context,
    private val images: MutableList<Image>)
    : RecyclerView.Adapter<ImagesAdapter.ViewHolder>() {

    private val layoutInflater = LayoutInflater.from(context)

    init {
        this.images.reverse()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImagesAdapter.ViewHolder {
        val view = layoutInflater.inflate(R.layout.li_image, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //TODO: Set ImageView from URL
        //val imageUrl = images[position]
        //Glide.with(context).load(imageUrl).into(holder.imageView)
        val image = images[position]

        Glide.with(context)
            .load(GalleryAPI.IMAGE_PREFIX_URL + image.url)
            .into(holder.imageView)
    }

    override fun getItemCount() = images.size

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.ivImage
    }

}