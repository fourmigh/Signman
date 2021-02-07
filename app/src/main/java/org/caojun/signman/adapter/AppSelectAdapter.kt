package org.caojun.signman.adapter

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckedTextView
import org.caojun.signman.R
import org.caojun.signman.room.App
import org.caojun.signman.room.AppDatabase
import org.jetbrains.anko.doAsync

/**
 * Created by CaoJun on 2017/8/31.
 */
class AppSelectAdapter(context: Context, list: ArrayList<App>) : BaseAdapter(context, list) {

    fun getSelectedApps(): ArrayList<App> {
        val apps: ArrayList<App> = ArrayList()
        for (i in 0..(count - 1)) {
            val app = getItem(i)
            if (app.isSelected) {
                apps.add(app)
            }
        }
        return apps
    }

    override fun getView(position: Int, convertView: View?, viewGrouo: ViewGroup?): View {
        val holder: ViewHolder
        var view = convertView
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_app_select, null)
            holder = ViewHolder()
            holder.ctvName = view.findViewById(R.id.ctvName)
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

        holder.ctvName?.isChecked = app.isSelected
        holder.ctvName?.text = app.name
        holder.ctvName?.compoundDrawablePadding = icon_padding
        holder.ctvName?.gravity = Gravity.CENTER_VERTICAL
//        holder.ctvName?.setCompoundDrawables(app.icon, null, null, null)
        holder.ctvName?.setCompoundDrawables(icon, null, null, null)
        holder.ctvName?.setOnClickListener {
            holder.ctvName?.toggle()
            app.isSelected = holder.ctvName?.isChecked!!
            if (!app.isSelected) {
                doAsync {
                    AppDatabase.getDatabase(context).getAppDao().delete(app)
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
        internal var ctvName: CheckedTextView? = null
    }
}