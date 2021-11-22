package com.example.testinnowise.ui.home

import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.testinnowise.EnumPrint
import com.example.testinnowise.JSONParse.RetrofitAPI
import com.example.testinnowise.databinding.FragmentHomeBinding
import com.example.testinnowise.lat
import com.example.testinnowise.lon
import com.example.xxxx.JSONParse.CurrentWeatherDataJson
import kotlinx.android.synthetic.main.fragment_home.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class HomeFragment : Fragment() {
    private var locationManager: LocationManager? = null
    private lateinit var homeViewModel: HomeViewModel
    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textHumidity
        homeViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })

        return root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()

        try {
            getWeather()
        } catch (e: Exception) {
            Log.e("zxc", e.toString())
        }
    }

    private fun getWeather() {


        var retrofit: Retrofit = Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org/data/2.5/")
            .addConverterFactory(GsonConverterFactory.create()).build()
        val retrofitAPI = retrofit.create(RetrofitAPI::class.java)
        val call: Call<CurrentWeatherDataJson?>? = retrofitAPI.getWeather(lat, lon)


        call?.enqueue(object : Callback<CurrentWeatherDataJson?> {
            override fun onResponse(
                call: Call<CurrentWeatherDataJson?>?,
                response: Response<CurrentWeatherDataJson?>
            ) {
                try {
                    var post: CurrentWeatherDataJson =
                        response.body() as CurrentWeatherDataJson
                    weatherToDay.text =
                        post.main.temp.toString() + " | " + post.weather[0].main
                    textHumidity.text = post.main.humidity.toString() + "%"
                    textPressure.text = post.main.pressure.toString() + "hPa"
                    textSpeed.text = post.wind.speed.toString() + "km/h"
                    cityToDay.text = post.name + ", " + post.sys.country
                    imageGl.setImageResource(EnumPrint.valueOf(post.weather[0].main).draw)

                } catch (e: Exception) {
                    Log.e("catch json parse error", e.toString())
                }
            }


            override fun onFailure(call: Call<CurrentWeatherDataJson?>, t: Throwable) {
                Toast.makeText(
                    activity,
                    "Ошибка загрузки данных,проверьте интернет подключение и перезапустите",
                    Toast.LENGTH_LONG
                ).show()
            }


        })

    }
}


