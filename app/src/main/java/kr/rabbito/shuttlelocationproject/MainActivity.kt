package kr.rabbito.shuttlelocationproject

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import kr.rabbito.shuttlelocationproject.data.Location
import kr.rabbito.shuttlelocationproject.databinding.ActivityMainBinding
import kr.rabbito.shuttlelocationproject.function.setChildEventListener

class MainActivity : AppCompatActivity(), OnMapReadyCallback {
    private var mBinding: ActivityMainBinding? = null
    private val binding get() = mBinding!!

    private lateinit var map: GoogleMap
    private val postList = mutableListOf<Location>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        binding.mainBtnPost.setOnClickListener {
            var intent = Intent(this,CommunityActivity::class.java)
            startActivity(intent)
        }


        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        setChildEventListener(postList, map, "Driver/tuk")
    }
}
