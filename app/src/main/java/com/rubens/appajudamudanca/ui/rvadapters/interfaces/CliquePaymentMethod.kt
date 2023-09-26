package com.rubens.appajudamudanca.ui.rvadapters.interfaces

import com.rubens.appajudamudanca.data.models.GanhoOuDespesa
import com.rubens.appajudamudanca.data.room.entities.Despesa

interface CliquePaymentMethod {
    fun cliquePagamentoEmDinheiro(isPressed: Boolean, despesa: GanhoOuDespesa)
    fun cliquePagamentoEmCartao(isPressed: Boolean, despesa: GanhoOuDespesa)
}