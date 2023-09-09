package com.example.blog_app

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.Circle
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private const val PREFS_FILE_NAME = "GeofencePrefs"
private const val PREFS_LATITUDE = "latitude"
private const val PREFS_LONGITUDE = "longitude"
private const val PREFS_RADIUS = "radius"

class UbicationFragment : Fragment(), OnMapReadyCallback {

    private lateinit var map: GoogleMap
    private lateinit var geofenceCircle: Circle
    var latitude: Double = 40.420118
    var longitude: Double = -3.706466

    private var radius: Double = 400.0

    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ubication, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        retrieveGeofenceFromPrefs() // Recuperar la geovalla desde las preferencias compartidas
        createFragment() // Crear el fragmento del mapa
        setupFab() // Configurar el botón flotante
    }

    private fun createFragment() {
        val mapFragment: SupportMapFragment =
            childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        createMarker() // Crear un marcador en el mapa
        createGeofence() // Crear la geovalla en el mapa
    }

    // Mostrar la ubicación del módulo
    private fun createMarker() {
        val coordinates = LatLng(latitude, longitude)
        val marker = MarkerOptions().position(coordinates).title("Dispositivo!")
        map.addMarker(marker)
        map.animateCamera(
            CameraUpdateFactory.newLatLngZoom(coordinates, 18f),
            4000,
            null
        )
    }


    private fun createGeofence() {
        val coordinates = LatLng(latitude, longitude)

        val fillAlpha = 0.3f // Establecer el valor alfa (transparencia) para el color de relleno
        val fillColor = Color.parseColor("#40FF0000") // Color rojo semitransparente

        val circleOptions = CircleOptions()
            .center(coordinates)
            .radius(radius)
            .strokeWidth(2f)
            .strokeColor(resources.getColor(R.color.geofence_stroke_color))
            .fillColor(fillColor)

        geofenceCircle = map.addCircle(circleOptions)
    }


    private fun setupFab() {
        val fab: FloatingActionButton = requireView().findViewById(R.id.fab)
        fab.setOnClickListener {
            showGeofenceEditDialog() // Mostrar el diálogo para editar la geovalla
        }
    }

    private fun showGeofenceEditDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_geofence_edit, null)
        val latitudeEditText = dialogView.findViewById<EditText>(R.id.latitudeEditText)
        val longitudeEditText = dialogView.findViewById<EditText>(R.id.longitudeEditText)
        val radiusEditText = dialogView.findViewById<EditText>(R.id.radiusEditText)


        latitudeEditText.setText(latitude.toString())
        longitudeEditText.setText(longitude.toString())
        radiusEditText.setText(radius.toString())

        val builder = AlertDialog.Builder(requireContext())
            .setTitle("Edit Geofence")
            .setView(dialogView)
            .setPositiveButton("Confirm") { dialog, _ ->
                val newLatitude = latitudeEditText.text.toString().toDoubleOrNull()
                val newLongitude = longitudeEditText.text.toString().toDoubleOrNull()
                val newRadiusKm = radiusEditText.text.toString().toDoubleOrNull()

                if (newLatitude != null && newLongitude != null && newRadiusKm != null) {
                    latitude = newLatitude
                    longitude = newLongitude
                    radius = newRadiusKm * 1000 // Convertir el radio de km a metros
                    saveGeofenceToPrefs() // Guardar la geovalla en las preferencias compartidas
                    updateGeofence() // Actualizar la geovalla en el mapa
                }
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }

        builder.create().show()
    }

    private fun updateGeofence() {
        geofenceCircle.remove() // Eliminar la geovalla actual del mapa

        val coordinates = LatLng(latitude, longitude)

        val fillAlpha = 0.3f // Establecer el valor alfa (transparencia) para el color de relleno
        val fillColor = Color.parseColor("#40FF0000") // Color rojo semitransparente

        val circleOptions = CircleOptions()
            .center(coordinates)
            .radius(radius)
            .strokeWidth(2f)
            .strokeColor(resources.getColor(R.color.geofence_stroke_color))
            .fillColor(fillColor)

        geofenceCircle = map.addCircle(circleOptions)
    }

    private fun saveGeofenceToPrefs() {
        val prefs = requireContext().getSharedPreferences(PREFS_FILE_NAME, Context.MODE_PRIVATE)
        val editor = prefs.edit()
        editor.putFloat(PREFS_LATITUDE, latitude.toFloat())
        editor.putFloat(PREFS_LONGITUDE, longitude.toFloat())
        editor.putFloat(PREFS_RADIUS, radius.toFloat())
        editor.apply()
    }

    private fun retrieveGeofenceFromPrefs() {
        val prefs = requireContext().getSharedPreferences(PREFS_FILE_NAME, Context.MODE_PRIVATE)
        latitude = prefs.getFloat(PREFS_LATITUDE, latitude.toFloat()).toDouble()
        longitude = prefs.getFloat(PREFS_LONGITUDE, longitude.toFloat()).toDouble()
        radius = prefs.getFloat(PREFS_RADIUS, radius.toFloat()).toDouble()
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            UbicationFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}