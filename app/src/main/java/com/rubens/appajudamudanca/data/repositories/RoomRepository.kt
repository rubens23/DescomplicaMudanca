package com.rubens.appajudamudanca.data.repositories

import com.rubens.appajudamudanca.data.models.GanhoOuDespesa
import com.rubens.appajudamudanca.data.room.entities.CustoMudanca
import com.rubens.appajudamudanca.data.room.entities.Despesa
import com.rubens.appajudamudanca.data.room.entities.Ganho

interface RoomRepository {
    suspend fun getSaldoAtual(saldoAtual: (saldo: Double)->Unit)
    suspend fun salvarReceitaMensal(receita: Double, salvouReceita: (salvou: Boolean)->Unit)
    suspend fun pegarReceitaMensalAtual(receita: (receita: Double)->Unit)
    suspend fun pegarDespesasAtuais(despesas: (despesas: Double)-> Unit)
    suspend fun pegarValorEconomizadoRestante(valorEconomizadoRestante: (valorRestante: Double)->Unit)
    suspend fun salvarNovaDespesa(despesa: Despesa, salvouDespesa: (salvou: Boolean)->Unit)
    suspend fun salvarNovoCusto(custoMudanca: CustoMudanca, salvouCusto: (salvou: Boolean)->Unit)
    suspend fun salvarNovoGanho(ganho: Ganho, salvouGanho: (salvou: Boolean)->Unit)
    suspend fun pegarListaDeDespesas(listaDeDespesas: (listaDespesas: List<Despesa>?)->Unit)
    suspend fun pegarListaDeGanhos(listaDeGanhos: (listaGanhos: List<Ganho>?)->Unit)
    suspend fun pegarTodosOsCustos(pegouCustos: (pegouListaCustos: List<CustoMudanca>?)->Unit)
    suspend fun atualizarSaldoDepoisDeGanhoOuDespesa(atualizouSaldoDepoisDeGanhoOuDespesa: (saldoAtualizado: Double)->Unit)
    suspend fun atualizarFormaDePagamentoDaDespesa(despesa: GanhoOuDespesa, formaDePagamento: String)
    suspend fun atualizarCustos(listaDeCustos: List<CustoMudanca>, salvouAlteracoes: (salvou: Boolean)->Unit)
    suspend fun pegarDespesasPorCategoria(categoria: String, pegouDespesas: (lista: List<Despesa>?)->Unit)
}