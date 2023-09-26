package com.rubens.appajudamudanca.data.models

data class GanhoOuDespesa(
    val nomeDespesaGanho: String,
    val valorDespesaGanho: Double,
    val categoria: String,
    val despesaOuGanho: String,
    val formaPagamentoDespesaOuGanho: String,
    val date: String
)
