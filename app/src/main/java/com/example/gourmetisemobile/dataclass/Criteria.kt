package com.example.gourmetisemobile.dataclass

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Criteria(
    var id: Int = 0,
    var label: String = ""
) : Parcelable
