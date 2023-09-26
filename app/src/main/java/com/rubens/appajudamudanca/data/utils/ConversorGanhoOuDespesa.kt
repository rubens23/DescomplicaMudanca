package com.rubens.appajudamudanca.data.utils

import com.rubens.appajudamudanca.data.models.GanhoOuDespesa
import com.rubens.appajudamudanca.data.room.entities.Despesa
import com.rubens.appajudamudanca.data.room.entities.Ganho

object ConversorGanhoOuDespesa {

    fun converteListaDeGanhosEListaDeDespesasParaListaGanhoOuDespesa(listaGanhos: List<Ganho>? = null,
    listaDespesas: List<Despesa>?): List<GanhoOuDespesa>{

        val listaResultante = mutableListOf<GanhoOuDespesa>()

        listaGanhos?.let {
            listDeGanhos->
            listDeGanhos.map {
                ganho->
                listaResultante.add(
                GanhoOuDespesa(nomeDespesaGanho = ganho.nomeGanho,
                valorDespesaGanho = ganho.ganho,
                categoria = "",
                despesaOuGanho = "ganho",
                formaPagamentoDespesaOuGanho = ganho.formaDePagamento,
                date = ganho.date)
                )
            }
        }
        listaDespesas?.let {
            listDespesas->
            listDespesas.map {
                despesa->
                listaResultante.add(
                GanhoOuDespesa(nomeDespesaGanho = despesa.nomeDespesa,
                valorDespesaGanho = despesa.custo,
                categoria = despesa.categoria,
                despesaOuGanho = "despesa",
                formaPagamentoDespesaOuGanho = despesa.formaDePagamento,
                date = despesa.date)
                )
            }
        }

        return listaResultante
    }
}