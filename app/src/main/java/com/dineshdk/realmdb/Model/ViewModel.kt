package com.dineshdk.realmdb.Model

import androidx.lifecycle.ViewModel
import com.dineshdk.realmdb.repo.CurrentUser
import com.dineshdk.realmdb.repo.Repository
import com.google.android.gms.maps.model.Marker

class ViewModel : ViewModel() {
    private val repo = Repository()
    private lateinit var cUser : CurrentUser
    lateinit var maker : Marker



    fun getUserLocationList() = repo.getList()

//    fun getAvailUsersEmail() = repo.getAvailUserList()
    fun getrealm() = repo.getrealm()




}