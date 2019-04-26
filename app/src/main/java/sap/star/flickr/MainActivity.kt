package sap.star.flickr

import android.content.Intent
import android.location.Criteria
import android.net.Uri
import android.nfc.NdefRecord.createUri
import android.nfc.Tag
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

private val TAG = "MainActivity"
class MainActivity : BaseActivity(), GetRawData.OnDownloadComplete, GetFlickrJsonData.OnDataAvailable,
                        RecyclerItemClickListener.OnRecyclerClickListener{

    private val flickrRecyclerViewAdapter = FlickrRecyclerViewAdapter(ArrayList())
    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG,"onCreate called")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        setSupportActionBar(toolbar)
        activateToolbar(false)
        recycler_view.layoutManager = LinearLayoutManager(this) as RecyclerView.LayoutManager?
        recycler_view.addOnItemTouchListener(RecyclerItemClickListener(this,recycler_view,this))
        recycler_view.adapter = flickrRecyclerViewAdapter
        val url = createUri("https://api.flickr.com/services/feeds/photos_public.gne","dogs","en-us",true)
        val getRawData = GetRawData(this) //create a class instance

        //getRawData.setDownloadCompleteListener(this) //put a listener on this file
        getRawData.execute(url)
        Log.d(TAG,"onCreate ends")
 //https://api.flickr.com/services/feeds/photos_public.gne?tagmode=any&format=json&nojsoncallback=1

        Log.d(TAG,"onCreate ends")
    }

    override fun onItemClick(view: View, position: Int) {
       Log.d(TAG, ".onItemClick: starts")
        Toast.makeText(this, "Normal tap at position $position", Toast.LENGTH_SHORT).show()

    }

    override fun onItemLongClick(view: View, position: Int) {
        Log.d(TAG, "onItenLongClick: starts")
//        Toast.makeText(this, "Long tap at position $position", Toast.LENGTH_SHORT).show()
        val photo = flickrRecyclerViewAdapter.getPhoto(position)
        if (photo != null) {
            val intent = Intent(this, PhotoDetailsActivity:: class.java)
            intent.putExtra(PHOTO_TRANSFER, photo)
            startActivity(intent)
        }
    }

    //this function allows you create a URL with the parameters of the function as the arguments
    private fun createUri(baseURL: String, searchCriteria: String, lang: String, matchAll: Boolean): String {
        Log.d(TAG,".createUri starts")
        return Uri.parse(baseURL).
            buildUpon().
            appendQueryParameter("tags",searchCriteria).
            appendQueryParameter("tagmode", if (matchAll) "ALL" else "ANY").
            appendQueryParameter("lang",lang).
            appendQueryParameter("format","json").
            appendQueryParameter("nojsoncallback","1").
            build().toString()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        Log.d(TAG,"onCreateOptionsMenu called")
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        Log.d(TAG,"onOptionsItemSelected called")
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
    //use companion object to reduce overusage
//    companion object {
//        private const val TAG = "MainActivity"
//    }
    //this function is put into post Execute in GetRawData, executed when got the data
    override fun onDownloadComplete(data: String, status: DownloadStatus){
        if (status == DownloadStatus.OK){
            Log.d(TAG, "onDownloadComplete called, data is $data")

            val getFlickrJsonData = GetFlickrJsonData(this)
            getFlickrJsonData.execute(data)
        }else {
            Log.d(TAG, "onDownloadCompleted failed with status $status. Error message is $data")
        }
    }
    //will get a list of photo objects
    override fun onDataAvailable(data: List<Photo>) {
        Log.d(TAG, ".onDataAvailable called, data is $data")
        flickrRecyclerViewAdapter.loadNewData(data)
        Log.d(TAG, "onDataAvailable ends")
    }

    override fun onError(exception: Exception) {
        Log.e(TAG, "onError called with ${exception.message}")
    }
}
