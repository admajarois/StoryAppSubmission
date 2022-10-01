package com.admaja.storyappsubmission.view.widget

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import androidx.core.os.bundleOf
import com.admaja.storyappsubmission.R
import com.admaja.storyappsubmission.data.local.entity.StoryEntity
import com.admaja.storyappsubmission.data.local.room.Dao
import com.admaja.storyappsubmission.data.local.room.StoryDatabase
import com.bumptech.glide.Glide
import kotlinx.coroutines.runBlocking

class StackRemoteViewsFactory(private val mContext: Context): RemoteViewsService.RemoteViewsFactory {

    private var mWidgetItems = listOf<StoryEntity>()
    private lateinit var dao: Dao

    override fun onCreate() {
        dao = StoryDatabase.getInstance(mContext).dao()
        fetchDataDB()
    }

    override fun onDataSetChanged() {
        fetchDataDB()
    }

    override fun onDestroy() {

    }

    override fun getCount(): Int = mWidgetItems.size

    override fun getViewAt(position: Int): RemoteViews {
        val rv = RemoteViews(mContext.packageName, R.layout.widget_item)
        try {
            val bitmap: Bitmap = Glide.with(mContext.applicationContext)
                .asBitmap()
                .load(mWidgetItems[position].photoUrl)
                .submit()
                .get()
            rv.setImageViewBitmap(R.id.iv_banner_widget, bitmap)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return rv
    }

    override fun getLoadingView(): RemoteViews? = null
    override fun getViewTypeCount(): Int = 1
    override fun getItemId(position: Int): Long = 0
    override fun hasStableIds(): Boolean = false

    private fun fetchDataDB() {
        runBlocking {
            mWidgetItems = dao.getStoriesForWidget()
        }
    }
}