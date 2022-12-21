package propertywatch.com

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import propertywatch.com.database.PropertyRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.system.exitProcess

const val TAG = "WatchrFetchr"

// this class uses retrofit and Gson to call to an external server hosting the properties.jason file
class WatchrFetchr {

    private val watchrApi: WatchrApi


    init {
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl("http:jellymud.com/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        watchrApi = retrofit.create(WatchrApi::class.java)
    }

    // gets the property objects from the json, outputting them as a LiveData response of property items
    fun fetchProperties(): LiveData<List<PropertyItem>> {
        val responseLiveData: MutableLiveData<List<PropertyItem>> = MutableLiveData()
        val request: Call<WatchrResponse> = watchrApi.fetchProperties()
        
        request.enqueue(object : Callback<WatchrResponse> {

            override fun onFailure(call: Call<WatchrResponse>, t: Throwable) {
                Log.e(TAG, "Failed to fetch properties", t)
            }

            override fun onResponse(
                call: Call<WatchrResponse>,
                response: Response<WatchrResponse>
            ) {
                Log.d(TAG, "Response received")
                val response: WatchrResponse? = response.body()
                val propertyResponse: PropertyResponse? = response?.properties
                var propertyItems: List<PropertyItem> = propertyResponse?.propertyItems
                    ?: mutableListOf()

                responseLiveData.value = propertyItems
            }
        })
        
        //return responseLiveData
        return responseLiveData
    }
}
