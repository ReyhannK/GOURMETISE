package com.example.gourmetisemobile.dataclass

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Note(
    var bakerySiret: String = "",
    var criteria_id: Int = 0,
    var value: Int = 0
) : Parcelable

