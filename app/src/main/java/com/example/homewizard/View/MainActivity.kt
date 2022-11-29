package com.example.homewizard.View

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Switch
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.homewizard.Adapter.ModelStateAdapter
import com.example.homewizard.Model.ModelState
import com.example.homewizard.R
import com.example.homewizard.Service.StateApi
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.state_one_item.*
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity(), ModelStateAdapter.Listener {

    lateinit var power : Switch
    lateinit var switch : Switch

    private var  modelStates : ArrayList<ModelState>?= null
    private var  modelState : ModelState?= null
    private var recyclerStateViewAdapter : ModelStateAdapter? = null
    var i : Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val layoutManager : RecyclerView.LayoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        get_id()

    }

    private fun get_id(){

        modelStates = ArrayList<ModelState>()
        val id1 : String = this.getString(R.string.id1)
        val id2 : String = this.getString(R.string.id2)
        //run blocking coroutine

            CoroutineScope(Dispatchers.IO).launch {
                while (i == 0){
                        loadState(id1)
                        loadState(id2)
                        delay(5000)
                    }
            }
    }


    private fun loadState (id : String)
    {
        val retrofit = Retrofit.Builder().baseUrl(id)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val serviceState = retrofit.create(StateApi::class.java)
        val callState = serviceState.getState()
        callState.enqueue(object  : Callback<ModelState>{
            override fun onResponse(call: Call<ModelState>, response: Response<ModelState>) {
                if (response.isSuccessful){
                    response.body()!!.let {
                        it.url = id
                        modelState = it
                        println("Succes State")
                        if (modelStates!!.size == 2)
                        modelStates!!.clear()
                        modelStates!!.add(modelState!!)
                        modelStates.let {
                            recyclerStateViewAdapter = ModelStateAdapter(modelStates!!, this@MainActivity)
                            recyclerView.adapter = recyclerStateViewAdapter
                        }

                    }
                }
            }

            override fun onFailure(call: Call<ModelState>, t: Throwable) {

                Toast.makeText(this@MainActivity, "error retrofit.. Wait 3 seconden", Toast.LENGTH_SHORT).show()

            }
        })




    }


    override fun onItemClick(modelState: ModelState) {
//        Toast.makeText(this,"click ${this.modelState!!.url}", Toast.LENGTH_LONG).show()
            var intent = Intent(this,EnergySocketDetails::class.java)
        i = 1;
        intent.putExtra("ID",modelState.url)
        startActivity(intent)
    }

}