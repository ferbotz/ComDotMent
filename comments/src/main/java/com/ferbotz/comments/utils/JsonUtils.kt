package com.ferbotz.comments.utils

import com.google.gson.Gson
import java.lang.reflect.Type

object JsonUtils {

    private val mGson = getGsonObject()

    private fun getGsonObject(): Gson {
        return Gson()
    }

    fun jsonify(data: Any?): String? {
        return mGson.toJson(data)
    }

    fun objectify(jsonString: String?, T: Class<*>?): Any? {
        return mGson.fromJson(jsonString, T)
    }

    fun <T> arrayObjectify(jsonString: String?, listType: Type?): Any? {
        return mGson.fromJson(jsonString, listType)
    }

}