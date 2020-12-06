package info.szadkowski.sensor.data.collector.infrastructure.influxdb

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface InfluxdbClient {

    @POST("/write")
    fun write(@Body content: String): Call<Unit>
}
