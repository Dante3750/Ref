package com.globals.netconnect.netconnect.app

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import android.util.Log
import com.globals.netconnect.netconnect.app.Cons.LOGGED_IN_PREF
import com.globals.netconnect.netconnect.app.Cons.TOKEN
import com.globals.netconnect.netconnect.model.Token
import com.globals.netconnect.netconnect.model.User


class SharedPrefManager (private val mCtx: Context) {

    private fun getPreferences(context: Context): SharedPreferences {
        return PreferenceManager.getDefaultSharedPreferences(context)
    }

    val isLoggedIn: Boolean
        get() {
            val sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
            return sharedPreferences.getInt("id", -1) != -1
        }

    val user: User
        get() {
            val sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
            return User(
                sharedPreferences.getString("firstName", null)!!,
                sharedPreferences.getString("lastName", null)!!,
                sharedPreferences.getString("employeeId", null)!!,
                sharedPreferences.getString("emailId", null)!!,
                sharedPreferences.getString("mobileNo", null)!!


            )
        }



    val tokenToServer: Token
        get(){
            val sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
            sharedPreferences.getString(TOKEN, null)

            return Token( sharedPreferences.getString("token", null)!!)
        }



    fun saveUser(user: User) {
        Log.d("TAG", "message"+user.emailId)

        val sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        editor.putString("emailId", user.emailId)
        editor.putString("employeeId", user.employeeId)
        editor.putString("mobileNo", user.mobileNo)
        editor.putString("firstName", user.firstName)
        editor.putString("lastName", user.lastName)

        editor.apply()

    }

    fun clear() {
        val sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
    }

    companion object {

        private val SHARED_PREF_NAME = "my_shared_preff"

        private var mInstance: SharedPrefManager? = null


        @Synchronized
        fun getInstance(mCtx: Context): SharedPrefManager {
            if (mInstance == null) {
                mInstance = SharedPrefManager(mCtx)
            }
            return mInstance as SharedPrefManager
        }
    }

    fun saveToken(tokenToServer: String) {

        val sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        editor.putString("token",tokenToServer)


        editor.apply()

    }

    fun setLoggedIn(context: Context, loggedIn: Boolean) {
        val editor = getPreferences(context).edit()
        editor.putBoolean(LOGGED_IN_PREF, loggedIn)
        editor.apply()
    }
    fun getLoggedStatus(context: Context): Boolean {
        return getPreferences(context).getBoolean(LOGGED_IN_PREF, false)
    }


}
