package propertywatch.com

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import propertywatch.com.database.Property
import androidx.appcompat.app.AlertDialog

// adaptor class to create property view holder, and handle interaction/onClick events
class PropertyAdapter(var properties: List<Property>) :
    RecyclerView.Adapter<PropertyAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false);
        return ViewHolder(view)
    }

    override fun getItemCount() = properties.size

    override fun onBindViewHolder(propertyHolder: ViewHolder, position: Int) {
        val property = properties[position]
        propertyHolder.bind(property)
    }

    inner class ViewHolder (val view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {

        // late initialise the property listings, make sure they are loaded in time
        lateinit var property: Property

        // declare the 'email a friend' button, from the fragment list
        val btnEmail = view.findViewById(R.id.buttonEmail) as Button

        // initialise both the fragment list and buttons with a click listener
        init {
            itemView.setOnClickListener(this)
            btnEmail.setOnClickListener(this)
        }

        // override and define custom onClick events for the 'Map' and 'Email Button'
        override fun onClick(v: View) {

            // check if user has clicked on a property listing
            if (itemView.isPressed) {

                // call property values only after list fragment is loaded
                // to avoid late initialisation errors
                val mLatitude = property.lat
                val mLongitude = property.lon
                val mPrice = property.price

                // if clicked, google map of the property address is opened
                if (mLatitude != 0.0 && mLongitude != 0.0) {
                    val intent = Intent(v.context, MapsActivity::class.java).apply {
                        var mapData = MapData(mLatitude, mLongitude, "Property Price: $$mPrice")
                        putExtra("map_data", mapData)
                }
                // initiate intent to call MapsActivity with lon and lat data
                view.context.startActivity(intent)
                }
            }

            // else check if email button is clicked, and run sendEmail()
            else if (btnEmail.isPressed) {
                sendEmail()
                Log.i("Info", "Email Button Clicked")
            }
        }

        // function that processes the send email intent
        fun sendEmail() {

            // get values for the email template, from the String resources
            val emailSubject = view.context.resources.getString(R.string.email_subject)
            val emailBody = view.context.resources.getString(R.string.email_body, property.address, property.price)

            // process email if property address and price are present
            if (property.price != 0 && property.address.isNotEmpty()) {

                // declare intent and add email subject and body
                val intent = Intent(Intent.ACTION_SEND)
                intent.putExtra(Intent.EXTRA_SUBJECT, emailSubject)
                intent.putExtra(Intent.EXTRA_TEXT, emailBody)

                // define data type
                intent.type = "text/plain"

                // launch email, using activity and intent, defined by the local view context
                view.context.startActivity(Intent.createChooser(intent, "Send using: "))
            } else {

                // warn user that property has no price and address
                val dialogBuilder = view.context?.let { noSend ->
                    AlertDialog.Builder(noSend)
                        .setMessage("We cannot email this property, it does not have a price or address!")
                        .setPositiveButton("OK", null)
                }
                // creates and shows the email alert
                val alert = dialogBuilder?.create()
                alert?.setTitle("Cannot share this property!")
                alert?.show()
            }
        }

        // function to bind each property details into each property fragment list
        fun bind(property: Property) {

            // define property object
            this.property = property

            // define location of textview components in the property fragment
            val addressView: TextView = view.findViewById(R.id.address)
            val priceView: TextView = view.findViewById(R.id.price)
            val phoneView: TextView = view.findViewById(R.id.phone)

            // assign values to the property's components, drawn from the property object
            addressView.text = property.address
            priceView.text = view.context.resources.getString(R.string.property_price, property.price)
            phoneView.text = view.context.resources.getString(R.string.property_phone, property.phone)
        }
    }
}
