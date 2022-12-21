package propertywatch.com

// class for property objects
data class PropertyItem (
    var id: String = "",
    var address: String = "",
    var price: Int = 0,
    var phone: String = "",
    var lat : Double = 0.0,
    var lon : Double = 0.0
)
