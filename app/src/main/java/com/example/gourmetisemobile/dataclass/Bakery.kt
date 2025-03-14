package com.example.gourmetisemobile.dataclass

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Bakery (
    var siret: String? = null,
    var name: String="",
    var street: String="",
    var postalCode: String="",
    var city: String="",
    var telephoneNumber:String="",
    var bakeryDescription: String="",
    var productsDecription: String="",
    var codeTicket: String = "",
    var dateEvaluation: String = "",
    val notes: List<Note>? = null,
): Parcelable{
    fun getFullAdress() : String{
        return this.street + " " + this.postalCode + " " + this.city;
    }

    fun getPhoneNumber() : String{
        return "+33 " + this.telephoneNumber;
    }
}
