package com.example.polar.model

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Keep
@Parcelize
data class Profil(
    @SerializedName("penggunga") var pengguna : String?= "",
    @SerializedName("berat") var berat: String?= "",
    @SerializedName("jenis_olahraga") var jenis_olahraga: String?= "",
    @SerializedName("nama") var nama: String?= "",
    @SerializedName("tinggi") var tinggi: String?= "",
    @SerializedName("ttl") var ttl : String?= ""
) : Parcelable

@Keep
@Parcelize
data class Murid(
    @SerializedName("Atlit_1") var atlit1 : String?= "",
    @SerializedName("Atlit_2") var atlit2: String?= "",
    @SerializedName("Atlit_3") var atlit3: String?= ""
) : Parcelable

@Keep
@Parcelize
data class DataLatihan(
    @SerializedName("tanggal_latihan") var tanggal_latihan: String?= "",
    @SerializedName("jam_mulai") var jam_mulai: String?= "",
    @SerializedName("jam_selesai") var jam_selesai: String?= "",
    @SerializedName("durasi_aktif") var durasi_aktif: String?= "",
    @SerializedName("durasi_istirahat") var durasi_istirahat : String?= "",
    @SerializedName("durasi_total") var durasi_total: String?= "",
    @SerializedName("intensitas") var intensitas : String?= "",
    @SerializedName("peak_hrp") var peak_hrp: String?= "",
    @SerializedName("heart_rate_reserve") var heart_rate_reserve: String?= "",
    @SerializedName("dosis_latihan") var dosis_latihan : String?= "",
    @SerializedName("kualitas_pelatihan") var kualitas_pelatihan: String?= ""
) : Parcelable