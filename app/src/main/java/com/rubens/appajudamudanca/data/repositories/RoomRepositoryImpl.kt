package com.rubens.appajudamudanca.data.repositories

import com.rubens.appajudamudanca.data.models.GanhoOuDespesa
import com.rubens.appajudamudanca.data.room.dao.GestaoFinanceiraDao
import com.rubens.appajudamudanca.data.room.entities.CustoMudanca
import com.rubens.appajudamudanca.data.room.entities.Despesa
import com.rubens.appajudamudanca.data.room.entities.Financas
import com.rubens.appajudamudanca.data.room.entities.Ganho
import java.lang.NullPointerException
import javax.inject.Inject

class RoomRepositoryImpl @Inject constructor(
    private val gestaoFinanceiraDao: GestaoFinanceiraDao
): RoomRepository {
    override suspend fun getSaldoAtual(saldoAtual: (saldo: Double) -> Unit) {
        try{
            saldoAtual(gestaoFinanceiraDao.getSaldoAtual())

        }catch (e: NullPointerException){
            gestaoFinanceiraDao.inserirDadosPelaPrimeiraVez(Financas())
            saldoAtual(0.0)

        }
    }

    override suspend fun pegarReceitaMensalAtual(receita: (receita: Double) -> Unit) {
        try {
            receita(gestaoFinanceiraDao.getReceitaAtual())
        }catch (e: NullPointerException){
            gestaoFinanceiraDao.inserirDadosPelaPrimeiraVez(Financas())
            receita(0.00)
        }
    }

    //não sei se vou colocar um tratamento para dados nulos
    override suspend fun pegarListaDeDespesas(listaDeDespesas: (listaDespesas: List<Despesa>?) -> Unit) {
        listaDeDespesas(gestaoFinanceiraDao.pegarListaDeDespesas())
    }

    //não sei se vou colocar um tratamento para dados nulos
    override suspend fun pegarListaDeGanhos(listaDeGanhos: (listaGanhos: List<Ganho>?) -> Unit) {
        listaDeGanhos(gestaoFinanceiraDao.pegarListaDeGanhos())
    }

    override suspend fun pegarDespesasPorCategoria(
        categoria: String,
        pegouDespesas: (lista: List<Despesa>?) -> Unit
    ) {
        pegouDespesas(gestaoFinanceiraDao.pegarListaDeDespesasPorCategoria(categoria))
    }

    override suspend fun pegarTodosOsCustos(pegouCustos: (pegouListaCustos: List<CustoMudanca>?) -> Unit) {
        pegouCustos(gestaoFinanceiraDao.pegarTodosOsCustos())

    }

    override suspend fun atualizarSaldoDepoisDeGanhoOuDespesa(atualizouSaldoDepoisDeGanhoOuDespesa: (saldoAtualizado: Double) -> Unit) {
        var totalGanhos = 0.0
        var totalDespesas = 0.0
        val listaGanhos = gestaoFinanceiraDao.pegarListaDeGanhos()
        val listaDespesas = gestaoFinanceiraDao.pegarListaDeDespesas()

        listaGanhos?.forEach {
            ganho->
            totalGanhos += ganho.ganho
        }

        listaDespesas?.forEach {
            despesa->
            totalDespesas += despesa.custo
        }
        val saldoAtual = totalGanhos - totalDespesas
        gestaoFinanceiraDao.atualizarSaldoAtual(saldoAtual)
        atualizouSaldoDepoisDeGanhoOuDespesa(saldoAtual)
    }

    override suspend fun pegarValorEconomizadoRestante(valorEconomizadoRestante: (valorRestante: Double) -> Unit) {
        var totalDeValorUsadoDasEconomias = 0.0
        val valorTotalEconomizado = gestaoFinanceiraDao.pegarValorTotalEconomizado()
        val listaValorDeEconomiaUsado: List<Double>? = gestaoFinanceiraDao.pegarValoresDeEconomiaUsadosNosCustos()
        totalDeValorUsadoDasEconomias = listaValorDeEconomiaUsado?.sumOrNull() ?: 0.0

        val valorRestante = valorTotalEconomizado - totalDeValorUsadoDasEconomias
        valorEconomizadoRestante(valorRestante)
    }

    override suspend fun atualizarFormaDePagamentoDaDespesa(
        despesa: GanhoOuDespesa,
        formaDePagamento: String
    ) {
        gestaoFinanceiraDao.atualizarFormaDePagamentoDaDespesa(despesa.nomeDespesaGanho,
            despesa.valorDespesaGanho,
            despesa.date,formaDePagamento)
    }

    override suspend fun atualizarCustos(listaDeCustos: List<CustoMudanca>, salvouAlteracoes: (salvou: Boolean)->Unit) {
        listaDeCustos.forEach {
            custo->
            gestaoFinanceiraDao.atualizarCustoPorId(custo.idCusto, custo.nomeCusto, custo.valorCusto, custo.valorEconomizado)
        }
        salvouAlteracoes(true)
    }



    override suspend fun pegarDespesasAtuais(despesas: (despesas: Double) -> Unit) {
        try {
            despesas(gestaoFinanceiraDao.getDespesasAtuais())
        }catch (e: NullPointerException){
            gestaoFinanceiraDao.inserirDadosPelaPrimeiraVez(Financas())
            despesas(0.00)

        }
    }



    override suspend fun salvarNovaDespesa(
        despesa: Despesa,
        salvouDespesa: (salvou: Boolean) -> Unit
    ) {
        gestaoFinanceiraDao.salvarNovaDespesa(despesa)
        salvouDespesa(true)
    }

    override suspend fun salvarNovoCusto(
        custoMudanca: CustoMudanca,
        salvouCusto: (salvou: Boolean) -> Unit
    ) {
        gestaoFinanceiraDao.salvarNovoCusto(custoMudanca)
        salvouCusto(true)
    }

    override suspend fun salvarNovoGanho(ganho: Ganho, salvouGanho: (salvou: Boolean) -> Unit) {
        gestaoFinanceiraDao.salvarNovoGanho(ganho)
        salvouGanho(true)
    }



    override suspend fun salvarReceitaMensal(
        receita: Double,
        salvouReceita: (salvou: Boolean) -> Unit
    ) {
        try{
            gestaoFinanceiraDao.salvarReceitaMensal(receita)
            salvouReceita(true)

        }catch (e: NullPointerException){
            gestaoFinanceiraDao.inserirDadosPelaPrimeiraVez(Financas())
            salvouReceita(false)

        }

    }

    fun List<Double>?.sumOrNull(): Double? {
        return this?.takeIf { it.isNotEmpty() }?.sum()
    }




}