package com.example.blog_app

import android.content.Intent
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, SensorEventListener {

    lateinit var fab: FloatingActionButton
    lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var userId: String // Agregar userId como variable miembro del MainActivity

    // Sensor
    private lateinit var sensorManager: SensorManager
    private var brightness: Sensor? = null


    override fun onDestroy() {
        super.onDestroy()

        // Detener el sensor de luz antes de destruir la actividad
        sensorManager.unregisterListener(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        setUpSensorStuff() // Configurar el sensor de luz

        drawerLayout = findViewById(R.id.drawer_layout) // Inicializar drawerLayout

        val toggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            toolbar,
            R.string.open_nav,
            R.string.close_nav
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        val navigationView: NavigationView = findViewById(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this) // Establecer el escuchador en la actividad

        // Obtener el userId desde el Intent
        userId = intent.getStringExtra("user_id") ?: ""

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, UbicationFragment())
                .commit()
            navigationView.setCheckedItem(R.id.nav_home)
        }

        bottomNavigationView = findViewById(R.id.bottomNavigationView)
        bottomNavigationView.background = null
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> replaceFragment(UbicationFragment())
                //R.id.board -> replaceFragment(HomeFragment())
                R.id.user -> replaceFragment(PersonalFragment.newInstance(userId))
                R.id.notify -> replaceFragment(NotificationFragment.newInstance(userId))
                R.id.contact -> replaceFragment(ContactFragment.newInstance(userId)) // Pasar el userId al ContactFragment
            }
            true
        }

        // Verificar el estado de inicio de sesión al abrir la actividad
        val sharedPreferences = getSharedPreferences("MY_APP_PREFS", MODE_PRIVATE)
        val isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false)
        if (!isLoggedIn) {
            startLoginActivity()
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager: FragmentManager = supportFragmentManager
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment_container, fragment)
        fragmentTransaction.commit()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_home -> {
                replaceFragment(UbicationFragment())
            }
            R.id.nav_user -> {
                replaceFragment(PersonalFragment.newInstance(userId))
            }
            R.id.nav_contact -> {
                replaceFragment(ContactFragment.newInstance(userId)) // Pasar el userId al ContactFragment
            }
            R.id.nav_about -> {
                replaceFragment(AboutFragment())
            }
            R.id.nav_logout -> {
                // Eliminar estado de inicio de sesión en SharedPreferences
                val sharedPreferences = getSharedPreferences("MY_APP_PREFS", MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                editor.putBoolean("isLoggedIn", false)
                editor.apply()

                startLoginActivity()
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }


    private fun startLoginActivity() {
        val intent = Intent(this@MainActivity, Login::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        finish()
    }

    // Sensor
    private fun setUpSensorStuff() {
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        brightness = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_LIGHT) {
            val light = event.values[0]
            setAppBrightness(light)
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // No se necesita hacer nada aquí
    }

    private fun setAppBrightness(brightness: Float) {
        return when {
            brightness < 1 -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
            else -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        sensorManager.registerListener(this, brightness, SensorManager.SENSOR_DELAY_NORMAL)
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }
}
