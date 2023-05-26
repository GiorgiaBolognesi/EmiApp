package com.example.emiapp.models

class ProfileData {
    var dataName: String? = null
    var dataSesso: String? = null
    var dataNascita: String? = null
    var dataImage: String? = null


    constructor(
        dataName: String?,
        dataSesso: String?,
        dataNascita: String?,
        dataImage: String?
    ) {
        this.dataName = dataName
        this.dataSesso = dataSesso
        this.dataNascita = dataNascita
        this.dataImage = dataImage
    }
}