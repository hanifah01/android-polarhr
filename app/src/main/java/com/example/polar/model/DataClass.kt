package com.example.polar.model

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Keep
@Parcelize
data class Profil(
    @SerializedName("pengguna") var pengguna : String?= "",
    @SerializedName("berat") var berat: String?= "",
    @SerializedName("jenis_olahraga") var jenis_olahraga: String?= "",
    @SerializedName("nama") var nama: String?= "",
    @SerializedName("tinggi") var tinggi: String?= "",
    @SerializedName("ttl") var ttl : String?= "",
    @SerializedName("nama_pelatih") var nama_pelatih : String?= ""
) : Parcelable

@Keep
@Parcelize
data class Murid(
    @SerializedName("atlit1") var atlit1 : String?= "",
    @SerializedName("atlit2") var atlit2: String?= "",
    @SerializedName("atlit3") var atlit3: String?= ""
) : Parcelable

@Keep
@Parcelize
data class DataLatihan(
    @SerializedName("tanggal_latihan") var tanggal_latihan: String?= "",
    @SerializedName("jam_mulai") var jam_mulai: String?= "",
    @SerializedName("jam_selesai") var jam_selesai: String?= "",
    @SerializedName("durasi_aktif") var durasi_aktif: String?= "0",
    @SerializedName("durasi_istirahat") var durasi_istirahat : String?= "0",
    @SerializedName("durasi_total") var durasi_total: String?= "0",
    @SerializedName("partial_intensity") var partial_intensity : String?= "0",
    @SerializedName("dn_vl") var dn_vl : String?= "0",
    @SerializedName("peak_hrp") var peak_hrp: String?= "",
    @SerializedName("heart_rate_reserve") var heart_rate_reserve: String?= "",
    @SerializedName("heart_rate_latihan") var heart_rate_latihan: String?= "",
    @SerializedName("heart_rate_max") var heart_rate_max: String?= "",
    @SerializedName("absolute_density") var absolute_density : String?= "0",
    @SerializedName("overal_intensity") var overal_intensity: String?= "0",
    @SerializedName("iod") var i_od: String?= "0"
) : Parcelable