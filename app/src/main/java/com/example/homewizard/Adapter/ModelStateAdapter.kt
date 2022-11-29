package com.example.homewizard.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.homewizard.Model.ModelState
import com.example.homewizard.R
import com.example.homewizard.Service.StateApi
import com.google.android.material.slider.Slider
import kotlinx.android.synthetic.main.state_one_item.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ModelStateAdapter(private val modelStateStateList : List<ModelState>, private val listener : Listener) : RecyclerView.Adapter<ModelStateAdapter.RowHolder>() {



    interface Listener{
        fun onItemClick(modelState: ModelState){
        }
    }
    class RowHolder(view : View) : RecyclerView.ViewHolder(view){
        fun bind(modelState : ModelState, position: Int, listener: Listener)
        {


            itemView.brightness_btn.value = modelState.brightness.toFloat()
            itemView.power_btn.isChecked = modelState.power_on
            itemView.switch_btn.isChecked = modelState.power_on

            if (itemView.switch_btn.isChecked && itemView.power_btn.isChecked)
                itemView.power_btn.isEnabled = false
            if (!itemView.switch_btn.isChecked)
                itemView.power_btn.isEnabled = true

            itemView.brightness_btn.addOnSliderTouchListener(object : Slider.OnSliderTouchListener{
                override fun onStartTrackingTouch(slider: Slider) {

                }

                override fun onStopTrackingTouch(slider: Slider) {
                    if (modelState.brightness != slider.value.toInt()){
                        modelState.brightness = slider.value.toInt()
                        setData(modelState)
                    }
                }
            })
            itemView.setOnClickListener{
                listener.onItemClick(modelState)
            }


            itemView.switch_btn.setOnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked){
                    itemView.switch_btn.isChecked = true
                    modelState.switch_lock = true
                    if (itemView.power_btn.isChecked)
                        itemView.power_btn.isEnabled = false

                }
                else {
                    itemView.switch_btn.isChecked = false
                    modelState.switch_lock = false
                    itemView.power_btn.isEnabled = true
                }
                CoroutineScope(Dispatchers.IO).launch{
                    setData(modelState)
                }
            }

            itemView.power_btn.setOnCheckedChangeListener { buttonView, isChecked ->
                if(isChecked){
                    itemView.power_btn.isChecked = true
                    modelState.power_on = true
                    if (itemView.switch_btn.isChecked)
                        itemView.power_btn.isEnabled = false
                }
                else{
                    itemView.power_btn.isChecked = false
                    modelState.power_on = false
                }
                CoroutineScope(Dispatchers.IO).launch {
                    setData(modelState)
                }
            }
            
        }
        private  fun setData(modelState : ModelState)
         {

            val retrofit = Retrofit.Builder().baseUrl(modelState.url)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            val service = retrofit.create(StateApi::class.java)
            val push = service.setState(modelState)
            push.enqueue(object : Callback<ModelState> {
                override fun onResponse(call: Call<ModelState>, response: Response<ModelState>) {
                    println("response")
                    if (response.isSuccessful)
                    {
                        Toast.makeText(itemView.context, "Succes!",Toast.LENGTH_SHORT).show()
                    }

                }

                override fun onFailure(call: Call<ModelState>, t: Throwable) {
                    Toast.makeText(itemView.context, "Error.. 5 second later one more time try..", Toast.LENGTH_SHORT).show()
                }

            })
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RowHolder {
    val view = LayoutInflater.from(parent.context).inflate(R.layout.state_one_item, parent, false )
        return  RowHolder(view)
    }


    override fun onBindViewHolder(holder: RowHolder, position: Int) {
    holder.bind(modelStateStateList[position],position, listener)
    }


    override fun getItemCount(): Int {
        return  modelStateStateList.count()
    }
}