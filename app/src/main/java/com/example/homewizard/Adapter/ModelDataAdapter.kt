package com.example.homewizard.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.homewizard.Model.ModelData
import com.example.homewizard.R
import kotlinx.android.synthetic.main.data_one_item.view.*

class ModelDataAdapter(private val modelDataList : List<ModelData>) : RecyclerView.Adapter<ModelDataAdapter.RowHolder>(){
    class RowHolder(view : View) : RecyclerView.ViewHolder(view){
        fun bind(modelData : ModelData, position: Int)
        {
            itemView.id_of_productData.setText("ID = ${modelData.url}")
            itemView.wifi_ssidId.setText("Wifi ssid = ${modelData.wifi_ssid}")
            itemView.total_power_export_t1_kwhid.text= "total power export = ${modelData.total_power_export_t1_kwh.toString()}"
            itemView.total_power_import_t1_kwhId.text = "total power import =  ${modelData.total_power_import_t1_kwh.toString()}"
            itemView.wifi_strengthId.text = "wifi strentg = ${modelData.wifi_strength.toString()}"
            itemView.active_power_wId.text = "active power w = ${modelData.active_power_w.toString()}"
            itemView.active_power_l1_wId.text = "active power l1 = ${modelData.active_power_l1_w.toString()}"

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RowHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.data_one_item, parent,false)
        return  RowHolder(view)
    }

    override fun onBindViewHolder(holder: RowHolder, position: Int) {
       holder.bind(modelDataList[position],position)
    }

    override fun getItemCount(): Int {
        return  modelDataList.count()
    }
}