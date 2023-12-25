package org.caojun.signman.room

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.luhuiguo.chinese.pinyin.Pinyin
import com.luhuiguo.chinese.pinyin.PinyinFormat
import java.util.Date
import kotlin.collections.ArrayList


/**
 * Created by CaoJun on 2017/8/31.
 */
@Entity
class App : Parcelable {
    @PrimaryKey
    var packageName: String = ""

    //签到的时间
    var time: ArrayList<Date> = ArrayList()

    var name: String? = null

//    var icon: Drawable? = null

    var isSigned: Boolean = false

    @Ignore
    var isSelected: Boolean = false

    @Ignore
    var cache: Double = 0.0

    fun addTime() {
        time.add(0, Date())
    }

    fun getLastTime(): Date {
        return time[0]
    }

    fun getSortString(): Char {
        val pinyin = Pinyin.INSTANCE.convert(name, PinyinFormat.DEFAULT_PINYIN_FORMAT).toLowerCase()
        return pinyin[0]
    }

    override fun equals(other: Any?): Boolean {
        if (other == null) {
            return false
        }
        if (other is App) {
            return packageName.equals(other.packageName)
        }
        return false;
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        val dataConverter = DataConverter()
        val times = dataConverter.toString(time)
//        val icons = dataConverter.toByteArray(icon!!)

        dest.writeString(packageName)
        dest.writeString(times)
        dest.writeString(name)
//        dest.writeInt(icons.size)
//        dest.writeByteArray(icons)
        dest.writeByte((if (isSigned) 1 else 0).toByte())
    }

    override fun describeContents(): Int {
        return 0
    }

    constructor()

    constructor(_in: Parcel): this() {
        packageName = _in.readString() ?: ""
        val times = _in.readString() ?: ""
        name = _in.readString()
//        val size = _in.readInt()
//        val icons = ByteArray(size)
//        _in.readByteArray(icons)
        val signs = _in.readByte()

        val dataConverter = DataConverter()
        time = dataConverter.toArrayListDate(times)
//        icon = dataConverter.toDrawable(icons)
        isSigned = signs.compareTo(1) == 0
    }

    companion object {
        @JvmField
        @Ignore
        val CREATOR: Parcelable.Creator<App> = object : Parcelable.Creator<App> {
            override fun createFromParcel(_in: Parcel): App {
                return App(_in)
            }

            override fun newArray(size: Int): Array<App?> {
                return arrayOfNulls(size)
            }
        }
    }
}