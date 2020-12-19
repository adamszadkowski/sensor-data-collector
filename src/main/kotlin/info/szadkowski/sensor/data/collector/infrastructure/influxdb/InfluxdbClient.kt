package info.szadkowski.sensor.data.collector.infrastructure.influxdb

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

interface InfluxdbClient {

    @POST("/write?precision=s")
    fun write(
        @Query("db") db: String,
        @Query("u") username: String,
        @Query("p") password: String,
        @Body content: String
    ): Call<Unit>
}
