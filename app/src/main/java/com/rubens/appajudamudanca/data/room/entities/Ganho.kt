package com.rubens.appajudamudanca.data.room.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Ganho(
    @PrimaryKey(autoGenerate = true)
    var idGanho: Long = 0,
    var formaDePagamento: String = "",
    var nomeGanho: String = "",
    var ganho: Double = 0.00,
    val date: String = ""

)
