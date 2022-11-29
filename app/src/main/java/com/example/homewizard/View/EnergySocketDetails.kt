package com.example.homewizard.View

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.homewizard.Adapter.ModelDataAdapter
import com.example.homewizard.Model.ModelData
import com.example.homewizard.R
import com.example.homewizard.Service.DataApi
import kotlinx.android.synthetic.main.activity_energy_socket_details.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class EnergySocketDetails : AppCompatActivity() {

    var modelData : ModelData?=null
    var modelDatas : ArrayList<ModelData>? = null
    private var recyclerStateViewAdapter : ModelDataAdapter? = null
    var i = 1;


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_energy_socket_details)
        val layoutManager : RecyclerView.LayoutManager = LinearLayoutManager(this)
        recyclerViewData.layoutManager = layoutManager

        val id : String? = intent.getStringExtra("ID")
        modelDatas = ArrayList<ModelData>()

        CoroutineScope(Dispatchers.IO).launch {
            while (i == 1)
            {
                loadData(id!!)
                delay(6000)
            }
        }

    }


     private fun loadData(id: String)
    {
        val retrofit = Retrofit.Builder().baseUrl(id)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val serviceData = retrofit.create(DataApi::class.java)
        val callData = serviceData.getData()
        callData.enqueue(object : Callback<ModelData> {
            override fun onResponse(call: Call<ModelData>, response: Response<ModelData>) {
                if (response.isSuccessful)
                {
                    response.body()!!.let {
                        it.url = id
                        modelData = it
                        println("Succes Data")
                        modelDatas!!.clear()

                        modelDatas!!.add(modelData!!)
                        modelDatas.let {
                            recyclerStateViewAdapter = ModelDataAdapter(modelDatas!!)
                            recyclerViewData.adapter = recyclerStateViewAdapter
                        }

                    }
                }
            }

            override fun onFailure(call: Call<ModelData>, t: Throwable) {
                Toast.makeText(this@EnergySocketDetails,"Error retrofit!", Toast.LENGTH_SHORT).show()
            }

        })
    }

    override fun onBackPressed() {
        i = 2;
        super.onBackPressed()
    }
}