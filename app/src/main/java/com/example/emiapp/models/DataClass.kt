package com.example.emiapp.models

import com.google.firebase.database.Exclude

class Data (
    @get:Exclude
    var id : String? = null,
    var startDate : String? = null,
    var startTime : String? = null,
    var endDate : String? = null,
    var endTime : String? = null,
    var typeHeadHache: String? = null,
    var symptomsHeadHache: String? = null,
    var painHeadache: String? = null,
    var note: String? = null) {}

class Startdate(var start : String? = null){}