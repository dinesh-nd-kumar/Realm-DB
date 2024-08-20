package com.dineshdk.realmdb.repo

import android.content.Context
import android.content.SharedPreferences
import com.dineshdk.realmdb.Model.User
import com.dineshdk.realmdb.others.Constant.USER_EMAIL
import com.dineshdk.realmdb.others.Constant.USER_ID
import com.dineshdk.realmdb.others.Constant.USER_NAME
import com.dineshdk.realmdb.others.Constant.USER_PREF
import com.dineshdk.realmdb.others.SessionData

class CurrentUser(val context: Context) {
    private var userPref : SharedPreferences

    init {
        userPref = context.getSharedPreferences(
            USER_PREF,
            Context.MODE_PRIVATE
        )

    }



    fun getId(): Int {
        return userPref.getInt(USER_ID,-1)
    }

    fun getName(): String {
        return userPref.getString(USER_NAME,"")!!
    }

    fun isValidUser(): Boolean {
        return (getId() > 0) && (getName().isNotBlank())
    }

    fun getEmail(): String{
         return userPref.getString(USER_EMAIL,"")!!
    }

    fun inValidateCurrentUser(){
        SessionData.userid = -1
        val et = userPref.edit()
        et.clear()
        et.commit()

    }

    fun setUser( user: User): Unit {

        val et = userPref.edit()
        et.putInt(USER_ID, user.id)
        et.putString(USER_NAME, user.name)
        et.putString(USER_EMAIL, user.email)
        et.commit()
    }

    fun setUser( name: String, email: String, id: Int ): Unit {

        val et = userPref.edit()
        et.putInt(USER_ID, id)
        et.putString(USER_NAME, name)
        et.putString(USER_EMAIL, email)
        et.commit()
    }
}