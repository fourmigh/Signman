package org.caojun.signman.adapter

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckedTextView
import android.widget.TextView
import org.caojun.signman.R
import org.caojun.signman.room.App
import org.caojun.signman.room.AppDatabase
import org.caojun.signman.utils.CacheUtils
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

/**
 * Created by CaoJun on 2017/8/31.
 */
class CacheAdapter(context: Context, list: ArrayList<App>, private val listener: Listener) : BaseAdapter(context, list) {

    interface Listener {
        fun onCacheCleared(size: Double)
    }

    override fun getView(position: Int, convertView: View?, viewGrouo: ViewGroup?): View {
        val holder: ViewHolder
        var view = convertView
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_cache, null)
            holder = ViewHolder()
            holder.tvName = view.findViewById(R.id.tvName)
            holder.tvCache = view.findViewById(R.id.tvCache)
            view.tag = holder
        } else {
            holder = view.tag as ViewHolder
        }

        val icon_size: Int = context?.resources?.getDimension(R.dimen.icon_size)?.toInt()?:128
        val icon_padding: Int = context?.resources?.getDimension(R.dimen.app_icon_padding)?.toInt()?:10

        val app = getItem(position)
//        app.icon?.setBounds(0, 0, icon_size, icon_size)
        val packageInfo = context.packageManager.getPackageInfo(app.packageName, 0)
        val icon = packageInfo.applicationInfo.loadIcon(context.packageManager)
        icon.setBounds(0, 0, icon_size, icon_size)

        holder.tvName?.text = app.name
        holder.tvName?.compoundDrawablePadding = icon_padding
        holder.tvName?.gravity = Gravity.CENTER_VERTICAL
//        holder.tvName?.setCompoundDrawables(app.icon, null, null, null)
        holder.tvName?.setCompoundDrawables(icon, null, null, null)
        holder.tvCache?.text = CacheUtils.getFormatSize(app.cache)

        holder.tvCache?.setOnClickListener {
            val folder = CacheUtils.getFolder(app.packageName) ?: return@setOnClickListener
            doAsync {
                val size = app.cache
                if (CacheUtils.deleteDir(folder)) {
                    app.cache = CacheUtils.getFolderSize(folder).toDouble()
                    uiThread {
                        holder.tvCache?.text = CacheUtils.getFormatSize(app.cache)
                        listener.onCacheCleared(size)
                    }
                }
            }
        }

        return view!!
    }

    override fun getItem(position: Int): App {
        return super.getItem(position)
    }

    override fun getItemId(position: Int): Long {
        return super.getItemId(position)
    }

    override fun getCount(): Int {
        return super.getCount()
    }

    private inner class ViewHolder {
        internal var tvName: TextView? = null
        internal var tvCache: TextView? = null
    }
}