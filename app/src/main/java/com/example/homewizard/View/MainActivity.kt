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
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.state_one_item.*
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity(), ModelStateAdapter.Listener {

    private var  modelStates : ArrayList<ModelState>?= null
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

        val retrofit1 = Retrofit.Builder().baseUrl(id1)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val retrofit2 = Retrofit.Builder().baseUrl(id2)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        CoroutineScope(Dispatchers.IO).launch {
            while (i == 0) {
                var first = async { loadState(id1, retrofit1) }
                first.await()
                var second = async { loadState(id2, retrofit2) }
                second.await()
                delay(5000)
            }
        }
    }


    private fun loadState (id : String, retrofit: Retrofit)
    {

        val serviceState = retrofit.create(StateApi::class.java)
        val callState = serviceState.getState()
        callState.enqueue(object  : Callback<ModelState>{
            override fun onResponse(call: Call<ModelState>, response: Response<ModelState>) {
                if (response.isSuccessful){
                    response.body()!!.let {
                        it.url = id
                        if (modelStates!!.size == 2)
                        {
                            var i : Int = 0
                            for (modelstate in modelStates!!){
                                if (modelstate.url == it.url)
                                    modelStates!!.set(i, it)
                                i++
                            }
                            recyclerStateViewAdapter!!.notifyDataSetChanged()
                        }
                        else {
                            modelStates!!.add(it!!)
                            modelStates.let {
                                recyclerStateViewAdapter =
                                    ModelStateAdapter(modelStates!!, this@MainActivity)
                                recyclerView.adapter = recyclerStateViewAdapter
                            }
                        }
                    }
                }
            }

            override fun onFailure(call: Call<ModelState>, t: Throwable) {

                Toast.makeText(this@MainActivity, "error retrofit..", Toast.LENGTH_SHORT).show()

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