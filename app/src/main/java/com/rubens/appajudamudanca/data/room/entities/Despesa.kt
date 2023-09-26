package com.rubens.appajudamudanca.data.room.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Despesa(
    @PrimaryKey(autoGenerate = true)
    var idDespesa: Long = 0,
    var formaDePagamento: String = "",
    var nomeDespesa: String = "",
    var categoria: String = "",
    var custo: Double = 0.00,
    val date: String = ""

)
