package com.example.innowise.JSON

import com.beust.klaxon.Converter
import com.beust.klaxon.Json
import com.beust.klaxon.JsonValue
import com.beust.klaxon.Klaxon

private fun <T> Klaxon.convert(
    k: kotlin.reflect.KClass<*>,
    fromJson: (JsonValue) -> T,
    toJson: (T) -> String,
    isUnion: Boolean = false
) =
    this.converter(object : Converter {
        @Suppress("UNCHECKED_CAST")
        override fun toJson(value: Any) = toJson(value as T)
        override fun fromJson(jv: JsonValue) = fromJson(jv) as Any
        override fun canConvert(cls: Class<*>) =
            cls == k.java || (isUnion && cls.superclass == k.java)
    })

private val klaxon = Klaxon()
    .convert(Pod::class, { Pod.fromValue(it.string!!) }, { "\"${it.value}\"" })
    .convert(Description::class, { Description.fromValue(it.string!!) }, { "\"${it.value}\"" })
    .convert(MainEnum::class, { MainEnum.fromValue(it.string!!) }, { "\"${it.value}\"" })

data class Weather5DaysDataJSON(
    val cod: String,
    val message: Long,
    val cnt: Long,
    val list: List<ListElement>,
    val city: City,

    ) {
    public fun toJson() = klaxon.toJsonString(this)

    companion object {
        public fun fromJson(json: String) = klaxon.parse<Weather5DaysDataJSON>(json)
    }
}

data class City(
    val id: Long,
    val name: String,
    val coord: Coord,
    val country: String,
    val population: Long,
    val timezone: Long,
    val sunrise: Long,
    val sunset: Long
)

data class Coord(
    val lat: Double,
    val lon: Double
)

data class ListElement(
    val dt: Long,
    val main: MainClass,
    val weather: List<Weather>,
    val clouds: Clouds,
    val wind: Wind,
    val visibility: Long,
    val pop: Double,
    val sys: Sys,

    @Json(name = "dt_txt")
    val dt_txt: String,

    val rain: Rain? = null
)

data class Clouds(
    val all: Long
)

data class MainClass(
    val temp: Double,

    @Json(name = "feels_like")
    val feelsLike: Double,

    @Json(name = "temp_min")
    val tempMin: Double,

    @Json(name = "temp_max")
    val tempMax: Double,

    val pressure: Long,

    @Json(name = "sea_level")
    val seaLevel: Long,

    @Json(name = "grnd_level")
    val grndLevel: Long,

    val humidity: Long,

    @Json(name = "temp_kf")
    val tempKf: Double
)

data class Rain(
    @Json(name = "3h")
    val the3H: Double
)

data class Sys(
    val pod: Pod
)

enum class Pod(val value: String) {
    D("d"),
    N("n");

    companion object {
        public fun fromValue(value: String): Pod = when (value) {
            "d" -> D
            "n" -> N
            else -> throw IllegalArgumentException()
        }
    }
}

data class Weather(
    val id: Long,
    val main: MainEnum,
    val description: Description,
    val icon: String
)

enum class Description(val value: String) {
    BrokenClouds("broken clouds"),
    ClearSky("clear sky"),
    FewClouds("few clouds"),
    LightRain("light rain"),
    OvercastClouds("overcast clouds"),
    ScatteredClouds("scattered clouds");

    companion object {
        public fun fromValue(value: String): Description = when (value) {
            "broken clouds" -> BrokenClouds
            "clear sky" -> ClearSky
            "few clouds" -> FewClouds
            "light rain" -> LightRain
            "overcast clouds" -> OvercastClouds
            "scattered clouds" -> ScatteredClouds
            else -> throw IllegalArgumentException()
        }
    }
}

enum class MainEnum(val value: String) {
    Clear("Clear"),
    Clouds("Clouds"),
    Rain("Rain");

    companion object {
        public fun fromValue(value: String): MainEnum = when (value) {
            "Clear" -> Clear
            "Clouds" -> Clouds
            "Rain" -> Rain
            else -> throw IllegalArgumentException()
        }
    }
}

data class Wind(
    val speed: Double,
    val deg: Long,
    val gust: Double
)