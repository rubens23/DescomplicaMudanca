package com.rubens.appajudamudanca.data.room.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Financas(
    @PrimaryKey(autoGenerate = true)
    var idFinanca: Long = 0,
    var saldoAtual: Double = 0.00,
    var receitaMensal: Double = 0.00,
    var Despesas: Double = 0.00,
    var economiasParaMudanca: Double = 0.00
)