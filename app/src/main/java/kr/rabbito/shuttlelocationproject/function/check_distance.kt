package kr.rabbito.shuttlelocationproject.function

import android.location.Location
import kotlin.math.roundToInt

fun check_distance(shuttle:Array<Double>,target:Array<Double>):Int{
    var distance:Int
    //셔틀위치정보(배열)를(을) locationA객체에 저장
    var locationA: Location = Location("point A")
    locationA.latitude=shuttle[0]
    locationA.longitude=shuttle[1]

    //교차로 위치정보(배열)를(을) locationB객체에 저장
    var locationB: Location = Location("point B")
    locationB.latitude=target[0]
    locationB.longitude=target[1]

    //셔틀위치와 꺾이는 교차로(target)위치의 거리계산
    distance = locationA.distanceTo(locationB).toDouble().roundToInt()

    //거리반환
    return distance
}