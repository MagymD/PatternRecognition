package magym.patternrecognitionvegetables.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Item(@SerializedName("name")
                @Expose
                val name: String,
                @SerializedName("percentage_probability")
                @Expose
                val percentageProbability: Double,
                @SerializedName("box_points")
                @Expose
                val boxPoints: List<Int>)