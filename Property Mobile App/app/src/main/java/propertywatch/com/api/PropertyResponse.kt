package propertywatch.com

import com.google.gson.annotations.SerializedName

// initialise property items
class PropertyResponse {
    @SerializedName("property")
    lateinit var propertyItems: List<PropertyItem>
}
