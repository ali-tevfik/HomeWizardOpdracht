package com.example.homewizard.Model

data class ModelData(
    var wifi_ssid : String,
    var wifi_strength : Int,
    var total_power_import_t1_kwh : Float,
    var url : String,
    var total_power_export_t1_kwh : Float,
    var active_power_w : Float,
    var active_power_l1_w : Float,
)