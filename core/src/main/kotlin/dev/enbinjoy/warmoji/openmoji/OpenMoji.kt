package dev.enbinjoy.warmoji.openmoji

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class OpenMoji(
    @Expose
    @SerializedName("hexcode")
    val hexcode: String,
    @Expose
    @SerializedName("order")
    val order: String,
)
