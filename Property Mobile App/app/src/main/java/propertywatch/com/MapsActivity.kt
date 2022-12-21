package propertywatch.com

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var mMap: GoogleMap
    var mName: String? = null
    var mLat = 0.0
    var mLon = 0.0

    // creates the google map content view
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        // pass local property data to Mapdata
        if (intent != null && intent.hasExtra("map_data"))
        {
            val mapData = intent.getSerializableExtra("map_data") as MapData

            mName = mapData.name
            mLat = mapData.latitude
            mLon = mapData.longitude
        }

        // grab the support map fragment and notify map is ready
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    // override and add map location, markers and zoom level
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        val venueLatLon = LatLng(mLat, mLon)

        mMap.addMarker(MarkerOptions().position(venueLatLon).title(mName))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(venueLatLon, 12f))
    }
}