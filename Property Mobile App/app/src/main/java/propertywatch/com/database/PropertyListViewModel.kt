package propertywatch.com.database

import androidx.lifecycle.ViewModel

// view model for the property list, loading property repository from the database
class PropertyListViewModel : ViewModel() {
    private val propertyRepository = PropertyRepository.get()
    val propertyList = propertyRepository.getProperties()

}
