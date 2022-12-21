package propertywatch.com

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import propertywatch.com.database.Property
import propertywatch.com.database.PropertyListViewModel
import propertywatch.com.database.PropertyRepository
import java.util.*
import kotlin.collections.ArrayList

// class to inflate and populate the property list, on the list fragment
class PropertyListFragment : Fragment() {

    companion object {
        fun newInstance() = PropertyListFragment()
    }

    // lateinit property list items
    private lateinit var mPropertyListViewModel: PropertyListViewModel
    private lateinit var mPropertyWatchrViewModel: PropertyWatchrViewModel

    // create view models and their context
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val context = activity as ViewModelStoreOwner

        mPropertyListViewModel = ViewModelProvider(context).get(PropertyListViewModel::class.java)
        mPropertyWatchrViewModel = ViewModelProvider(context).get(PropertyWatchrViewModel::class.java)
    }

    // inflate property list fragment with properties drawn from the local database
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        val recyclerView = inflater.inflate(R.layout.fragment_list, container, false) as RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(context)

        // set observer to check if properties exist in the database, if not populate from local list
        mPropertyListViewModel.propertyList.observe (
            viewLifecycleOwner,
                Observer { propertyList ->

                if (propertyList.isEmpty()) {
                    PropertyRepository.loadTestData()
                    return@Observer
                }
                recyclerView.adapter = PropertyAdapter(propertyList)
            })

        return recyclerView
    }

    /*
    * Here, new properties are pulled from the HTTP Jason fetched property data array
    * then saved to the database, and finally update the property list view.
    * (I had an issue were using the observer in the OnCreate, OnCreateView, OnViewCreated functions
    *  would endlessly reconnect to the http server, re-fetch the Jason, and try to re-insert
    *  the property objects into the database. return@Observer didn't seem to help.)
    * So for now by using onSaveInstanceState() this will only occur once every time
    * the user goes back to the Property List view after viewing a property google map.
    * */
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        // local array to temporarily store newly fetched properties in
        val propertyArray: ArrayList<Property> = ArrayList()

        // observer to call PropertyRepository and get new properties
        mPropertyWatchrViewModel.propertyItemLiveData.observe (
            viewLifecycleOwner,
            Observer { propertyList ->

                // if fetched list is not empty, add them to a local list
                if (propertyList.isNotEmpty()) {

                    for (PropertyItem in propertyList) {
                        propertyArray.add(
                            Property(
                                PropertyItem.id,
                                PropertyItem.address,
                                PropertyItem.price,
                                PropertyItem.phone,
                                PropertyItem.lat,
                                PropertyItem.lon
                            )
                        )
                    }
                    // finally, add property entries to the local database
                    val propertyRepository = PropertyRepository.get()
                    propertyRepository.addProperties(propertyArray)
                }
            })
    }
}
