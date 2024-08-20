package com.dineshdk.realmdb.repo

import com.dineshdk.realmdb.Model.User
import com.dineshdk.realmdb.Model.UserLocation
import com.dineshdk.realmdb.others.SessionData
import io.realm.Realm
import io.realm.RealmResults
import io.realm.kotlin.where

class Repository {
    private  var userLocationList : ArrayList<UserLocation>
    private var realm: Realm = Realm.getDefaultInstance()
//    private var availUsersEmail : List<String>
    private lateinit var cUser : CurrentUser


    init {
        val results: RealmResults<UserLocation> = realm.where<UserLocation>().equalTo("userId",SessionData.userid).findAll()

        // Copying results to avoid Lazy Loading issues
        userLocationList = realm.copyFromRealm(results) as ArrayList<UserLocation>
//    userLocationList = realm.where(UserLocation::class.java).findAllAsync()


//        availUsersEmail = realm.where(User::class.java).findAll().map {
//            it.email!!
//        }


    }

    fun getList() = userLocationList
//    fun getAvailUserList() = availUsersEmail

    fun getrealm() = realm


}