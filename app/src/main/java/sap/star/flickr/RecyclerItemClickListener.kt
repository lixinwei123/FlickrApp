package sap.star.flickr

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View


class RecyclerItemClickListener(context: Context, recyclerView: RecyclerView, private val listener: OnRecyclerClickListener)
    : RecyclerView.SimpleOnItemTouchListener() {

    private val TAG = "RecyclerItemClickListen"

    interface OnRecyclerClickListener {
        fun onItemClick(view: View, position: Int)
        fun onItemLongClick(view: View, position: Int)
    }

    override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
        Log.d(TAG, ".onInterceptTouchEvent: starts $e")
//        return super.onInterceptTouchEvent(rv, e)

        val result = gestureDetector.onTouchEvent(e)
        Log.d(TAG, "onInterceptTouchEvent() returning : $result")
        return result
    }
        //add gestureDetector
        private val gestureDetector = GestureDetector(context, object: GestureDetector.SimpleOnGestureListener(){
            override fun onSingleTapUp(e: MotionEvent): Boolean {
                Log.d(TAG, ".onSingleTapUp: starts")
                val childView = recyclerView.findChildViewUnder(e.x, e.y)
                Log.d(TAG, "onSingleTapUp calling listener.onItemClick")
                listener.onItemClick(childView!!, recyclerView.getChildAdapterPosition(childView))
                return true

            }

            override fun onLongPress(e: MotionEvent) {
                Log.d(TAG, ".onLongPress: starts")
                val childView = recyclerView.findChildViewUnder(e.x, e.y) //see what's the X-y coordinates and return the result
                Log.d(TAG, "onLongPress calling listener.onItemClick")
                listener.onItemLongClick(childView!!, recyclerView.getChildAdapterPosition(childView))
            }
        })


}