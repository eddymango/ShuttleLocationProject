package kr.rabbito.shuttlelocationproject.function

import android.util.Log
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kr.rabbito.shuttlelocationproject.R
import java.lang.Thread.sleep

fun showMarker(googleMap: GoogleMap, driverName: String, latitude : Double, longitude : Double) {
    val makerOptions = MarkerOptions()
    makerOptions
        .icon(BitmapDescriptorFactory.fromResource(R.drawable.main_icon_marker_bus))
        .position(LatLng(latitude, longitude))
        .title(driverName)
    googleMap.addMarker(makerOptions)
}