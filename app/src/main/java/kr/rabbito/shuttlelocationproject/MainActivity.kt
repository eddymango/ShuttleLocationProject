package kr.rabbito.shuttlelocationproject

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.ZoomControls
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kr.rabbito.shuttlelocationproject.data.Location
import kr.rabbito.shuttlelocationproject.databinding.ActivityMainBinding
import kr.rabbito.shuttlelocationproject.function.setChildEventListener
import kr.rabbito.shuttlelocationproject.function.showRoute

class MainActivity : AppCompatActivity(), OnMapReadyCallback {
        private var mBinding: ActivityMainBinding? = null
        private val binding get() = mBinding!!

        private lateinit var map: GoogleMap
        private val postList = mutableListOf<Location>()

        @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            mBinding = ActivityMainBinding.inflate(layoutInflater)

            setContentView(binding.root)

            window.apply {
                decorView.systemUiVisibility =
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                statusBarColor = Color.TRANSPARENT
            }

            val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
            mapFragment.getMapAsync(this)

            //postActivity -> communityActivity에서 btn 구현 완료
//            binding.mainBtnToPost.setOnClickListener {
//                val intent = Intent(this, PostActivity::class.java)
//                startActivity(intent)
//            }

            binding.mainBtnToCommunity.setOnClickListener {
                val intent = Intent(this, CommunityActivity::class.java)
                startActivity(intent)
            }
        }

        override fun onMapReady(googleMap: GoogleMap) {
            map = googleMap

            // 나침판
            map.uiSettings.isCompassEnabled = true
            // 지도 화면 회전
            map.uiSettings.isRotateGesturesEnabled = true
            // 확대 축소(+-) 버튼
            map.uiSettings.isZoomControlsEnabled = true

            // Add a marker in Sydney and move the camera
            //val yeouido = LatLng(37.521814, 126.923596)
            //map.addMarker(MarkerOptions().position(yeouido).title("Marker in Yeouido"))
            //map.moveCamera(CameraUpdateFactory.newLatLng(yeouido))

                // 학교 셔틀 경로
            var school_route = arrayOf(LatLng(37.337272, 126.736072), LatLng(37.340786, 126.730372),LatLng(37.343790, 126.733322)
                , LatLng(37.345289, 126.734745), LatLng(37.347356,126.736804), LatLng(37.351476,126.740745)
                ,LatLng(37.350695,126.743040), LatLng(37.350127,126.744053),LatLng(37.350846, 126.744857)
                ,LatLng(37.351964,126.740970),LatLng(37.351479,126.740708), LatLng(37.345314, 126.734715)
                ,LatLng(37.343800, 126.733302), LatLng(37.340248, 126.738970), LatLng(37.337272, 126.736072))

            showRoute(map, school_route)

            setChildEventListener(postList, map, "Driver/tuk")
        }
}

