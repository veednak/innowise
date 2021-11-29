package com.example.testinnowise.Presenter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.innowise.Adapter.ModelWeather
import com.example.innowise.Adapter.MyAdapter
import com.example.innowise.JSON.Weather5DaysDataJSON
import com.example.testinnowise.R
import com.example.testinnowise.databinding.FragmentDashboardBinding
import com.example.testinnowise.lat
import com.example.testinnowise.lon
import com.example.testinnowise.View.ui.dashboard.DashboardViewModel
import com.example.xxxx.JSONParse.RetrofitAPI2
import kotlinx.android.synthetic.main.fragment_dashboard.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.DecimalFormat

class DashboardFragment : Fragment() {

    private lateinit var dashboardViewModel: DashboardViewModel
    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(

        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {

        dashboardViewModel =
            ViewModelProvider(this).get(DashboardViewModel::class.java)
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        getWeather()
    }

    private fun getWeather() {

        CoroutineScope(Dispatchers.IO).launch {
            var retrofit: Retrofit = Retrofit.Builder()
                .baseUrl("https://api.openweathermap.org/data/2.5/")
                .addConverterFactory(GsonConverterFactory.create()).build()
            val retrofitAPI = retrofit.create(RetrofitAPI2::class.java)
            val call: Call<Weather5DaysDataJSON?>? = retrofitAPI.getWeather(lat, lon)
            call?.enqueue(object : Callback<Weather5DaysDataJSON?> {
                override fun onResponse(
                    call: Call<Weather5DaysDataJSON?>?,
                    response: Response<Weather5DaysDataJSON?>
                ) {
                    val postlist = response.body()
                    val weatherList = ArrayList<ModelWeather>()
                    for (element in postlist!!.list) {
                        val f = DecimalFormat("##")
                        val d = element.main.temp
                        var temperature = f.format(d)
                        if (temperature == "-0") {
                            temperature = "0"
                        }
                        var img: Int
                        if (element.weather.first().main == null) {
                            img = R.drawable.ic_snow
                        } else {
                            img = EnumPrint.valueOf(element.weather.first().main.value).draw
                        }
                        weatherList.add(
                            ModelWeather(
                                temperature,
                                element.dt_txt,
                                if (element.weather.first().main == null) {
                                    "Snow"
                                } else
                                    element.weather.first().main.value,
                                img
                            )
                        )
                    }
                    list.layoutManager = LinearLayoutManager(requireContext())
                    list.adapter = MyAdapter(weatherList.toList())
                }

                override fun onFailure(call: Call<Weather5DaysDataJSON?>, t: Throwable) {
                    Toast.makeText(
                        activity,
                        "Ошибка загрузки данных,проверьте интернет подключение и перезапустите",
                        Toast.LENGTH_LONG
                    ).show()
                }
            })
        }
    }
}


