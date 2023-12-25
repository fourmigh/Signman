package org.caojun.signman.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

/**
 * Created by CaoJun on 2017/8/31.
 */
@Database(entities = [App::class], version = 2, exportSchema = false)
@TypeConverters(DataConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getAppDao(): AppDao

    companion object {
        private var INSTANCE: AppDatabase? = null

        @JvmStatic fun getDatabase(context: Context): AppDatabase {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, "app_database")
                        .fallbackToDestructiveMigration().build()
            }
            return INSTANCE!!
        }

        @JvmStatic fun destroyInstance() {
            INSTANCE = null
        }
    }
}