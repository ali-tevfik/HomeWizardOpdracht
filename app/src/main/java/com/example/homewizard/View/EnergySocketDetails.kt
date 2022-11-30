package com.example.homewizard.View

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.homewizard.Model.ModelData
import com.example.homewizard.R
import com.example.homewizard.Service.DataApi
import kotlinx.android.synthetic.main.activity_energy_socket_details.*
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class EnergySocketDetails : AppCompatActivity() {

    var modelData : ModelData?=null
    var i : Int = 1
    lateinit var sharedPreferences : SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_energy_socket_details)

        //get energy costs
        sharedPreferences = this.getSharedPreferences("com.example.homewizard.View",
            Context.MODE_PRIVATE)
       var money =  sharedPreferences.getInt("money", 0)
        price_input.setText("${money.toString()}")

        //get which one apparat is?
        val id : String? = intent.getStringExtra("ID")


        val retrofit = Retrofit.Builder().baseUrl(id)
            .addConverterFactory(GsonConverterFactory.create())
            .build()


        //I use IO Coroutine. Every 5 second refresh data
        CoroutineScope(Dispatchers.IO).launch {
                while (i == 1)
                {
                    loadData(id!!, money, retrofit)
                    delay(5000)
                }
            }


    }

    //Get data with retrofit
     private fun loadData(id: String, money : Int, retrofit: Retrofit)
    {


        val serviceData = retrofit.create(DataApi::class.java)
        val callData = serviceData.getData()
        callData.enqueue(object : Callback<ModelData> {
            override fun onResponse(call: Call<ModelData>, response: Response<ModelData>) {
                if (response.isSuccessful)
                {
                    response.body()!!.let {
                        it.url = id
                        modelData = it
                        id_of_productData.setText("ID = ${it.url}")
                        wifi_ssidId.setText("Wifi ssid = ${it.wifi_ssid}")
                        total_power_export_t1_kwhid.text= "total power export = ${it.total_power_export_t1_kwh.toString()}"
                        total_power_import_t1_kwhId.text = "total power import =  ${it.total_power_import_t1_kwh.toString()}"
                        wifi_strengthId.text = "wifi strentg = ${it.wifi_strength.toString()}"
                        active_power_wId.text = "active power w = ${it.active_power_w.toString()}"
                        active_power_l1_wId.text = "active power l1 = ${it.active_power_l1_w.toString()}"
                        total_priceId.text = "Total price is : ${(it.total_power_import_t1_kwh * money!!).toString()}"

                    }
                }
            }

            override fun onFailure(call: Call<ModelData>, t: Throwable) {
            }

        })
    }

    //Click buttton and save new energy cost
    fun money_saveBtn(view : View)
    {
        var money : Int = 0
        //check Edittext
        try {
            money = price_input.text.toString().toInt()
        }
        catch (e : Exception)
        {
            money = 0
        }
        //save new energy cost
        sharedPreferences.edit().putInt("money",money!!).apply()
        Toast.makeText(this, "Saved Succes!", Toast.LENGTH_SHORT).show()
    }

    // stop refresh data
    override fun onBackPressed() {
        i = 2;
        super.onBackPressed()
    }

    // stop refresh data
    override fun onStop() {
        i = 1
        super.onStop()
    }
}