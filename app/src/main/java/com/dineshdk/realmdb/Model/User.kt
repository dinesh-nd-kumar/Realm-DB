package com.dineshdk.realmdb.Model

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass

@RealmClass
open class User(
    @PrimaryKey
    var id: Int = -1 ,
    var name: String? = null,
    var email: String? = null,
    var password: String? = null
): RealmObject() {


}