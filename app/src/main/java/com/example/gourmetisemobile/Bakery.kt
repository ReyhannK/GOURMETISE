package com.example.gourmetisemobile

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Bakery (
    var siret: String? = null,
    var name: String="",
    var street: String="",
    var postal_code: String="",
    var city: String="",
    var telephone_number:String="",
    var bakery_description: String="",
    var products_decription: String=""
): Parcelable{
    fun getFullAdress() : String{
        return this.street + " " + this.postal_code + " " + this.city;
    }
}
