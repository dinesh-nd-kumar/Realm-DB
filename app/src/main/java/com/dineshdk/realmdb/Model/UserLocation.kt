package com.dineshdk.realmdb.Model

import io.realm.RealmObject
import io.realm.annotations.RealmClass

@RealmClass
open class UserLocation(
    var lat: Double? = null,
    var lon: Double? = null,
    var userId: Int? = null,
    var time: Long? = null,
    ): RealmObject() {


}