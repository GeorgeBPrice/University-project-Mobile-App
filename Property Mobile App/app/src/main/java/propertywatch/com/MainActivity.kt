package propertywatch.com

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.location.*
import propertywatch.com.database.Property
import propertywatch.com.database.PropertyListViewModel
import propertywatch.com.database.PropertyRepository
import java.util.*

class MainActivity : AppCompatActivity() {

    // load the property list view model in time
    private lateinit var mPropertyListViewModel: PropertyListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // prepare and initialise the property repository
        PropertyRepository.initialize(this)

        // add property list ViewModel to property list fragment
        mPropertyListViewModel = ViewModelProvider(this).get(PropertyListViewModel::class.java)

        mPropertyListViewModel.propertyList.observe(this) {
            loadFragment(PropertyListFragment.newInstance())
        }

        // if no property fragment exists, load it up
        if (savedInstanceState == null) {
            loadFragment(PropertyListFragment.newInstance())
        }

        // check for and initialise location services
        val manager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        // launch alert and prompt if location setting is off
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER))
        {
            showAlertLocation()
        }
    }

    // function to create fragment in the main container
    fun loadFragment(fragment: Fragment)
    {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.container, fragment)
            .commit()
    }

    // alert to check if GPS location setting is on, or prompt the user
    private fun showAlertLocation()
    {
        // declare a new dialog alert
        val dialog = AlertDialog.Builder(this)

        // set main alert message
        dialog.setMessage("Location service is OFF, please turn it on to use this App.")
        dialog.setCancelable(false)

        // set option to click on location settings
        dialog.setPositiveButton("Settings") {
                _, _ ->
            val myIntent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            startActivity(myIntent)
        }

        // set option to cancel request
        dialog.setNegativeButton("Cancel") {
                dialog, _ ->
            dialog.cancel()
        }

        // launch the whole dialog alert
        dialog.show()
    }

    // reloads the property list fragment when a user clicks back, while on a google map
    override fun onBackPressed() {

        val currentFragment: Fragment? =
            supportFragmentManager.findFragmentById(R.id.container)

        if (currentFragment != null && currentFragment.view?.id == R.id.property_list_fragment)
            loadFragment(PropertyListFragment.newInstance())
    }
}