package sap.star.flickr

import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso

//create a view holder class
class FlickrImageViewHolder(view: View): RecyclerView.ViewHolder(view) {
    var thumbnail: ImageView = view.findViewById(R.id.thumbnail)
    var title: TextView = view.findViewById(R.id.title)
}
class FlickrRecyclerViewAdapter(private var photoList: List<Photo>) : RecyclerView.Adapter<FlickrImageViewHolder>(){
    private val TAG = "FlickrRecyclerViewAdapt"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FlickrImageViewHolder {
        //called by the layout manager when need a new view
        Log.d(TAG, ".onCreateViewHolder new view requested")
        val view = LayoutInflater.from(parent.context).inflate(R.layout.browse, parent, false) //inflate the browse layout,

        return FlickrImageViewHolder(view)
    }

    override fun getItemCount(): Int {
//        Log.d(TAG, "getItemCount called")
        return if (photoList.isNotEmpty()) photoList.size else 0
    }

    fun loadNewData(newPhotos:List<Photo>){
        photoList = newPhotos
        notifyDataSetChanged()
    }

    fun getPhoto(position: Int): Photo? {
        return if (photoList.isNotEmpty()) photoList[position] else null
    }
    override fun onBindViewHolder(holder: FlickrImageViewHolder, position: Int) {
       val photoItem = photoList[position]
//        Log.d(TAG, ".onBindViewHolder:) ${photoItem.title} --> $position")
        Picasso.with(holder.thumbnail.context).load(photoItem.image)
            .error(R.drawable.placeholder)
            .placeholder(R.drawable.placeholder)
            .into(holder.thumbnail)
        holder.title.text = photoItem.title
    }
}