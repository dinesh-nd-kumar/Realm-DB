package com.dineshdk.realmdb.view

import android.app.Dialog
import android.content.Intent
import android.location.Geocoder
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.ArrayAdapter
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.dineshdk.realmdb.others.LocationWork
import com.dineshdk.realmdb.R
import com.dineshdk.realmdb.Model.User
import com.dineshdk.realmdb.Model.UserLocation
import com.dineshdk.realmdb.Model.ViewModel

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.dineshdk.realmdb.databinding.ActivityMapsBinding
import com.dineshdk.realmdb.databinding.CustomInfoWindowBinding
import com.dineshdk.realmdb.databinding.DialogSwitchUserBinding
import com.dineshdk.realmdb.others.SessionData
import com.dineshdk.realmdb.repo.CurrentUser
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter
import com.google.android.gms.maps.model.Marker
import io.realm.Realm
import io.realm.RealmResults
import io.realm.kotlin.where
import java.text.SimpleDateFormat
import java.util.concurrent.TimeUnit

class MapsActivity : LocationActivity(), OnMapReadyCallback,InfoWindowAdapter,
    LocationAdapter.ItemClickListener {

    private lateinit var mMap: GoogleMap
    private lateinit var mViewModel: ViewModel
    private lateinit var binding: ActivityMapsBinding
    private lateinit var infoWBinding: CustomInfoWindowBinding
    private lateinit var cUser : CurrentUser
    private lateinit var userLocationList : RealmResults<UserLocation>
    private lateinit var maker : Marker
    private var markerIndex = 0
    private lateinit var realm: Realm
    private lateinit var availUsersEmail : List<String>
    private lateinit var geoCoder : Geocoder

    private lateinit var locationAdapter: LocationAdapter

    val formatter = SimpleDateFormat("dd/MM hh:mm");

    private fun doWork(){
        val locationWorkRequest = PeriodicWorkRequestBuilder<LocationWork>(
            15, TimeUnit.MINUTES)
            .build()
//        val locationWorkRequest = OneTimeWorkRequestBuilder<LocationWork>().build()

        WorkManager.getInstance(applicationContext).enqueue(locationWorkRequest)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        cUser = CurrentUser(this)
        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mViewModel = ViewModelProvider(this).get(ViewModel::class.java)
        if (!cUser.isValidUser()){
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }else{
            SessionData.userid = cUser.getId()
            doWork()
        }
        geoCoder = Geocoder(this)
        realm = Realm.getDefaultInstance()

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.Map) as SupportMapFragment
        mapFragment.getMapAsync(this)

//        val results: RealmResults<UserLocation> = realm.where<UserLocation>().findAll()

        // Copying results to avoid Lazy Loading issues
//        userLocationList = realm.copyFromRealm(results) as ArrayList<UserLocation>

//        userLocationList = realm.where(UserLocation::class.java).equalTo("userId",cUser.getId()).findAll()
//        userLocationList = realm.where(UserLocation::class.java).findAllAsync()
        setRecycler()
//        userLocationList.addChangeListener { results, changeSet ->
//            locationAdapter.notifyDataSetChanged()
//        }

        binding.ButtonBefore.setOnClickListener {
            moveBefore()
        }
        binding.ButtonNext.setOnClickListener {
            moveNext()
        }
        binding.ButtonBack.setOnClickListener {
//            showList()
            binding.viewFlipper.displayedChild = 0
        }
        binding.ButtonSwitchUser.setOnClickListener {
            customSwitchUserDialog()
        }








    }

    override fun onStart() {
        super.onStart()
        val results: RealmResults<User> = realm.where<User>().findAll()
        availUsersEmail = realm.copyFromRealm(results).map { it.email!! }
//        availUsersEmail = realm.where(User::class.java).findAll().map { it.email!! }
    }

    private fun moveNext(){
        if(markerIndex == mViewModel.getUserLocationList().size-1){
            moveMarker(LatLng(mViewModel.getUserLocationList()[markerIndex]?.lat!!, mViewModel.getUserLocationList()[markerIndex]?.lon!!),true)
            return
        }
        markerIndex++
        moveMarker(LatLng(mViewModel.getUserLocationList()[markerIndex]?.lat!!, mViewModel.getUserLocationList()[markerIndex]?.lon!!))


    }

    private fun moveBefore(){
        if(markerIndex == 0){
            moveMarker(LatLng(mViewModel.getUserLocationList()[markerIndex]?.lat!!, mViewModel.getUserLocationList()[markerIndex]?.lon!!),true)
            return
        }
        markerIndex--
        moveMarker(LatLng(mViewModel.getUserLocationList()[markerIndex]?.lat!!, mViewModel.getUserLocationList()[markerIndex]?.lon!!))
    }


    private fun setRecycler(){
        binding.rv.apply {
            locationAdapter = LocationAdapter(this@MapsActivity,mViewModel.getUserLocationList(), geoCoder)
            adapter = locationAdapter
            layoutManager = LinearLayoutManager(this@MapsActivity)

            val decorator = DividerItemDecoration(this@MapsActivity,
                (layoutManager as LinearLayoutManager).orientation)
            addItemDecoration(decorator)

        }
    }


    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        infoWBinding = CustomInfoWindowBinding.inflate(layoutInflater)

        // Add a marker in Sydney and move the camera
        val sydney = LatLng(-34.0, 151.0)
//        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
        mViewModel.maker = mMap.addMarker(MarkerOptions().position(sydney).title("user"))!!
        mMap.setInfoWindowAdapter(this)




    }

    override fun onItemClick(position: Int) {
//        showMap()
        binding.viewFlipper.displayedChild = 1
        markerIndex = position
        moveMarker(LatLng(mViewModel.getUserLocationList()[position]?.lat!!, mViewModel.getUserLocationList()[position]?.lon!!),true)

    }

    private fun moveMarker(latLng: LatLng, doZoom: Boolean = false){
        mViewModel.maker.position = latLng
        mViewModel.maker.showInfoWindow();

        if (doZoom){
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 13f))
        }else{
            mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng,))
        }

    }

    override fun getInfoContents(p0: Marker): View? {

        infoWBinding.tvLocation.text = markerIndex.toString()
        infoWBinding.tvUser.text = cUser.getName()

        return  null
    }

    override fun getInfoWindow(p0: Marker): View? {
        val dateString = formatter.format(mViewModel.getUserLocationList()[markerIndex]?.time!!)
        infoWBinding.tvLocation.text = dateString
        infoWBinding.tvUser.text = cUser.getName()
        return infoWBinding.root
    }



    private fun switchUser() {
        cUser.inValidateCurrentUser()
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun customSwitchUserDialog() {
        val dialog = Dialog(this)
        val diBinding = DialogSwitchUserBinding.inflate(layoutInflater)
        val listView = diBinding.list
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(diBinding.root)
        val adapter = ArrayAdapter<String>(this, R.layout.textview_user, availUsersEmail )
        listView.adapter = adapter

        listView.setOnItemClickListener { _, _, pos, _ ->
            if (cUser.getEmail().equals(availUsersEmail[pos])){
                dialog.dismiss()
            }else{
                SessionData.loginUserEmail = availUsersEmail[pos]
                switchUser()
            }
        }
        diBinding.btnClose.setOnClickListener { dialog.dismiss() }
        diBinding.logoutBtn.setOnClickListener {
            SessionData.loginUserEmail = ""
            switchUser()
        }
        dialog.show()
    }


    override fun onDestroy() {
        super.onDestroy()
//        realm.close()
//        userLocationList.removeAllChangeListeners()
    }


}