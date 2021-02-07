package org.caojun.signman.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import org.caojun.signman.R
import android.app.Activity
import android.content.pm.ApplicationInfo
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import org.caojun.signman.Constant
import org.caojun.signman.adapter.CacheAdapter
import org.caojun.signman.room.App
import org.caojun.signman.room.AppDatabase
import org.caojun.signman.utils.AppSortComparator
import org.caojun.signman.utils.CacheUtils
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.util.Collections

/**
 * Created by CaoJun on 2017/8/31.
 */
class CacheActivity : AppCompatActivity() {

    private val list: ArrayList<App> = ArrayList()
    private var adapter:CacheAdapter? = null
    private var totalSize = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnAppList.text = getString(R.string.clear_cache, "0 KB")

        btnAppList.setOnClickListener {
            clearCache()
        }

        list.clear()

        btnAnimation.visibility = View.GONE
        tvTips.visibility = View.GONE
    }

    private fun clearCache() {
        progressBar.visibility = View.VISIBLE
        val apps = list
        doAsync {
            for (app in apps) {
                val folder = CacheUtils.getFolder(app.packageName) ?: continue
                CacheUtils.deleteDir(folder)
            }
            list.clear()
            refreshData()
        }
    }

    private fun refreshData() {
        progressBar.visibility = View.VISIBLE
        doAsync {
            if (list.isEmpty()) {

                val packages = packageManager.getInstalledPackages(0)
                totalSize = 0.0
                for (i in packages.indices) {
                    val packageInfo = packages[i]
                    if ((packageInfo.applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM) == 0) { //非系统应用
                        if (packageInfo.packageName.equals(packageName)) {
                            //不把自身应用加载到列表中
                            continue
                        }
                        // AppInfo 自定义类，包含应用信息
                        val app = App()
                        app.name = packageInfo.applicationInfo.loadLabel(packageManager).toString()//获取应用名称
                        app.packageName = packageInfo.packageName //获取应用包名，可用于卸载和启动应用
//                        app.setVersionName(packageInfo.versionName)//获取应用版本名
//                        app.setVersionCode(packageInfo.versionCode)//获取应用版本号
//                        app.icon = packageInfo.applicationInfo.loadIcon(packageManager)//获取应用图标

                        app.cache = CacheUtils.getFolderSize(app.packageName).toDouble()
                        list.add(app)

                        totalSize += app.cache
                    }
                }

                Collections.sort(list, AppSortComparator())

//                for (app in list) {
//                    if (Constant.Apps.contains(app)) {
//                        app.isSelected = true
//                    }
//                }

                uiThread {
                    adapter = CacheAdapter(baseContext, list, object : CacheAdapter.Listener {
                        override fun onCacheCleared(size: Double) {
                            totalSize -= size
                            btnAppList.text = getString(R.string.clear_cache, CacheUtils.getFormatSize(totalSize))
                        }
                    })
                    listView.adapter = adapter

                    btnAppList.text = getString(R.string.clear_cache, CacheUtils.getFormatSize(totalSize))
                }

            }

            uiThread {
                progressBar.visibility = View.GONE
            }
        }
    }

    override fun onResume() {
        super.onResume()
        refreshData()
    }
}