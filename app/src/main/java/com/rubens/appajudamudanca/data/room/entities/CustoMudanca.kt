package com.rubens.appajudamudanca.data.room.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CustoMudanca(
    @PrimaryKey(autoGenerate = true)
    var idCusto: Long = 0,
    var nomeCusto: String,
    var valorCusto: Double = 0.00,
    var valorEconomizado: Double = 0.00
)
