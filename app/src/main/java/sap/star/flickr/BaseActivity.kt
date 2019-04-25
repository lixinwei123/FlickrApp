package sap.star.flickr

import android.annotation.SuppressLint
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.View

//made internal because don't have to be available outside the project
internal const val FLICKR_QUERY = "FLICK_QUERY"
internal const val PHOTO_TRANSFER = "PHOTO_TRANSFER"
@SuppressLint("Registered")
// open enables you to create a subclass because we need base activity
open class BaseActivity: AppCompatActivity() {
    private val TAG = "BaseActivity"


    internal fun activateToolbar(enableHome: Boolean){
        Log.d(TAG, ".activateToolbar")
        //inflate the toolbar, then put the toolbar at the top of the screenn by using setSupportActionBar
        var toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar) //
        supportActionBar?.setDisplayHomeAsUpEnabled(enableHome) //automatically add the home button
    }

}