package propertywatch.com

import java.io.Serializable

// property map data object that google map calls to, to get lon and lat of each property
class MapData (var latitude: Double, var longitude: Double, var name: String) : Serializable