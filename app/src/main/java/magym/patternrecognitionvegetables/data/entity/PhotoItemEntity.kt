package magym.patternrecognitionvegetables.data.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class PhotoItemEntity(@SerializedName("name")
                           @Expose
                           val name: String,
                           @SerializedName("percentage_probability")
                           @Expose
                           val percentageProbability: Double,
                           @SerializedName("box_points")
                           @Expose
                           val boxPoints: List<Int>)