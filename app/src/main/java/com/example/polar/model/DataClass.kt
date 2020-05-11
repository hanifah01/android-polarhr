package com.example.polar.model

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Keep
@Parcelize
data class Profil(
    @SerializedName("berat") var berat: String= "",
    @SerializedName("jenis_olahraga") var jenis_olahraga: String= "",
    @SerializedName("nama") var nama: String= "",
    @SerializedName("tinggi") var tinggi: String= "",
    @SerializedName("ttl") var ttl : String= ""
) : Parcelable