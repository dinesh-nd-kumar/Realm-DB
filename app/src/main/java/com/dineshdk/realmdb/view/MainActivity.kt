package com.dineshdk.realmdb.view

import android.os.Bundle
import com.dineshdk.realmdb.Model.User
import com.dineshdk.realmdb.Model.UserLocation
import com.dineshdk.realmdb.databinding.ActivityMainBinding
import io.realm.Realm

class MainActivity : LocationActivity() {
    private lateinit var binding : ActivityMainBinding




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val realm = Realm.getDefaultInstance()


        val ids = realm.where(User::class.java).max("id")


        val nextids = ids?.toInt()?.plus(1) ?: 1

//        val user = User(nextids, "dinesh", "dd@mail.com")
//        val userLocation = UserLocation(73.0,11.1)
        realm.executeTransaction {
//            it.insert(userLocation)
//            it.insert(user)
        }

        val users = realm.where(User::class.java).findAll()
        val locations = realm.where(UserLocation::class.java).findAll()

//        binding.name.text = users[users.size-1]?.name ?: "dd"
//        binding.total.text = "${users.size} ${locations.size}"




    }






}