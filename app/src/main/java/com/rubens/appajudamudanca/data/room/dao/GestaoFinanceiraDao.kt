package com.rubens.appajudamudanca.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.rubens.appajudamudanca.data.models.GanhoOuDespesa
import com.rubens.appajudamudanca.data.room.entities.CustoMudanca
import com.rubens.appajudamudanca.data.room.entities.Despesa
import com.rubens.appajudamudanca.data.room.entities.Financas
import com.rubens.appajudamudanca.data.room.entities.Ganho

@Dao
interface GestaoFinanceiraDao {

    @Query("SELECT saldoAtual FROM Financas")
    suspend fun getSaldoAtual(): Double

    @Query("SELECT receitaMensal FROM Financas")
    suspend fun getReceitaAtual(): Double

    @Query("SELECT Despesas FROM Financas")
    suspend fun getDespesasAtuais(): Double

    @Query("SELECT economiasParaMudanca FROM Financas")
    suspend fun pegarValorTotalEconomizado(): Double

    @Query("SELECT valorEconomizado FROM CustoMudanca")
    suspend fun pegarValoresDeEconomiaUsadosNosCustos(): List<Double>?





    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun inserirDadosPelaPrimeiraVez(financa: Financas)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun salvarNovaDespesa(despesa: Despesa)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun salvarNovoGanho(ganho: Ganho)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun salvarNovoCusto(custoMudanca: CustoMudanca)




    @Query("UPDATE Financas SET receitaMensal =:receita WHERE idFinanca =1")
    suspend fun salvarReceitaMensal(receita: Double)

    @Query("SELECT * FROM Despesa WHERE categoria =:categoria")
    suspend fun pegarListaDeDespesasPorCategoria(categoria: String): List<Despesa>?


    @Query("UPDATE Financas SET saldoAtual=:saldoAtual WHERE idFinanca =1")
    suspend fun atualizarSaldoAtual(saldoAtual: Double)


    @Query("SELECT * FROM Ganho")
    suspend fun pegarListaDeGanhos(): List<Ganho>?

    @Query("SELECT * FROM Despesa")
    suspend fun pegarListaDeDespesas(): List<Despesa>?

    @Query("UPDATE Despesa SET formaDePagamento = :formaDePagamento WHERE nomeDespesa = :nomeDespesaGanho AND custo = :valorDespesaGanho AND date = :date")
    suspend fun atualizarFormaDePagamentoDaDespesa(nomeDespesaGanho: String, valorDespesaGanho: Double, date: String, formaDePagamento: String)

    @Query("UPDATE CustoMudanca SET nomeCusto = :nomeCusto, valorCusto = :valorCusto, valorEconomizado = :valorEconomizado WHERE idCusto = :idCusto")
    suspend fun atualizarCustoPorId(idCusto: Long, nomeCusto: String, valorCusto: Double, valorEconomizado: Double)



    @Query("SELECT * FROM CustoMudanca")
    suspend fun pegarTodosOsCustos(): List<CustoMudanca>?


}