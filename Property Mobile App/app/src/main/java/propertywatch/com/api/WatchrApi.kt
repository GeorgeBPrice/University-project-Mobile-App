package propertywatch.com

import retrofit2.Call
import retrofit2.http.GET

// user retrofit to GET json with http request
interface WatchrApi {

    @GET("properties.json")
    fun fetchProperties(): Call<WatchrResponse>

}