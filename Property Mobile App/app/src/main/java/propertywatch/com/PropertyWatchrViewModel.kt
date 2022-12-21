package propertywatch.com

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel

// class with a property watcher that fetches new properties and adds to LiveData list
class PropertyWatchrViewModel : ViewModel() {

    val propertyItemLiveData: LiveData<List<PropertyItem>>
    init {
        propertyItemLiveData = WatchrFetchr().fetchProperties()
    }
}

