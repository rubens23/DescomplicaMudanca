package com.rubens.appajudamudanca.viewmodels

import android.text.Editable
import android.widget.TextView
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rubens.appajudamudanca.data.models.GanhoOuDespesa
import com.rubens.appajudamudanca.data.repositories.RoomRepository
import com.rubens.appajudamudanca.data.room.entities.Despesa
import com.rubens.appajudamudanca.data.room.entities.Ganho
import com.rubens.appajudamudanca.data.utils.ConversorGanhoOuDespesa
import com.rubens.appajudamudanca.ui.utils.ConversorValoresDouble
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import java.lang.NumberFormatException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class FragmentHomeScreenViewModel
    @Inject constructor(
        private val roomRepository: RoomRepository

    ): ViewModel() {

    var mesSelecionado: String = ""
    var anoSelecionado: String = ""


    private val _pegouDespesasAtualizadas: MutableSharedFlow<Double> = MutableSharedFlow(replay = 0)
    val pegouDespesasAtualizadas: SharedFlow<Double> = _pegouDespesasAtualizadas

    private val _atualizouOSaldo: MutableSharedFlow<Double> = MutableSharedFlow(replay = 0)
    val atualizouOSaldo: SharedFlow<Double> = _atualizouOSaldo

    private val _pegouListaDeDespesas: MutableSharedFlow<List<Despesa>?> = MutableSharedFlow(replay = 0)
    val pegouListaDeDespesas: SharedFlow<List<Despesa>?> = _pegouListaDeDespesas

    private val _pegouListaDeGanhos: MutableSharedFlow<List<Ganho>?> = MutableSharedFlow(replay = 0)
    val pegouListaDeGanhos: SharedFlow<List<Ganho>?> = _pegouListaDeGanhos


    private val _pegouReceitaAtualizada: MutableSharedFlow<Double> = MutableSharedFlow(replay = 0)
    val pegouReceitaAtualizada: SharedFlow<Double> = _pegouReceitaAtualizada



    private val _pegouSaldoAtual: MutableSharedFlow<Double> = MutableSharedFlow(replay = 0)
    val pegouSaldoAtual: SharedFlow<Double> = _pegouSaldoAtual

    private val _salvouReceitaMensal: MutableSharedFlow<Boolean> = MutableSharedFlow(replay = 0)
    val salvouReceitaMensal: SharedFlow<Boolean> = _salvouReceitaMensal

    private val _msgReceitaDigitadaInvalida: MutableSharedFlow<String> = MutableSharedFlow(replay = 0)
    val msgReceitaDigitadaInvalida: SharedFlow<String> = _msgReceitaDigitadaInvalida





    /**
     *         //esse metodo da erro em dispositivos que estao em ingles
     */
    fun subtractOneMonth(monthTextView: TextView, yearTextView: TextView): ArrayList<String> {
        val calendar = Calendar.getInstance()
        val monthName = monthTextView.text.toString()
        var year = yearTextView.text.toString().toInt()

        val dateFormat = SimpleDateFormat("MMMM", Locale.getDefault())
        calendar.time = dateFormat.parse(monthName)!!

        if (calendar.get(Calendar.MONTH) == Calendar.JANUARY) {
            calendar.set(Calendar.MONTH, Calendar.DECEMBER)
            year--
        } else {
            calendar.add(Calendar.MONTH, -1)
        }

        val newMonth = dateFormat.format(calendar.time)


        return arrayListOf(newMonth, year.toString())
    }

    fun addOneMonth(monthTextView: TextView, yearTextView: TextView): ArrayList<String> {
        val calendar = Calendar.getInstance()
        val monthName = monthTextView.text.toString()
        var year = yearTextView.text.toString().toInt()

        val dateFormat = SimpleDateFormat("MMMM", Locale.getDefault())
        calendar.time = dateFormat.parse(monthName)!!

        if (calendar.get(Calendar.MONTH) == Calendar.DECEMBER) {
            calendar.set(Calendar.MONTH, Calendar.JANUARY)
            year++
        } else {
            calendar.add(Calendar.MONTH, 1)
        }

        val newMonth = dateFormat.format(calendar.time)

        return arrayListOf(newMonth, year.toString())



    }

    fun getCurrentMonth(): String {
        val calendar = Calendar.getInstance()
        val formato = SimpleDateFormat("MMMM", Locale.getDefault())
        return formato.format(calendar.time).toLowerCase(Locale.getDefault())
    }

    fun getCurrentYear(): String {
        val calendar = Calendar.getInstance()
        return calendar.get(Calendar.YEAR).toString()
    }


    fun getSaldoAtual() {
        viewModelScope.launch {
            roomRepository.getSaldoAtual(saldoAtual = ::pegouSaldoAtual)

        }
    }


    fun pegarReceitaMensal() {
        viewModelScope.launch {
            roomRepository.pegarReceitaMensalAtual(receita = ::pegouReceitaAtual)
        }

    }

    fun pegarDespesaAtual() {
        viewModelScope.launch {
            roomRepository.pegarDespesasAtuais(despesas = ::pegouDespesas)
        }
    }

    fun pegarListaDeDespesas() {
        viewModelScope.launch {
            roomRepository.pegarListaDeDespesas(listaDeDespesas = ::pegouListaDeDespesas)
        }
    }

    fun pegarListaDeGanhos() {
        viewModelScope.launch {
            roomRepository.pegarListaDeGanhos(listaDeGanhos = ::pegouListaDeGanhos)
        }
    }

    fun atualizarSaldoDepoisDeGanhoOuDespesa() {
        viewModelScope.launch {
            roomRepository.atualizarSaldoDepoisDeGanhoOuDespesa(atualizouSaldoDepoisDeGanhoOuDespesa = ::atualizouSaldoDepoisDeDespesaOuGanho)
        }
    }

    fun atualizarFormaDePagamentoDaDespesa(despesa: GanhoOuDespesa, formaDePagamento: String) {
        viewModelScope.launch {
            roomRepository.atualizarFormaDePagamentoDaDespesa(despesa, formaDePagamento)
        }
    }

    private fun atualizouSaldoDepoisDeDespesaOuGanho(saldoAtualizado: Double) {
        viewModelScope.launch {
            _atualizouOSaldo.emit(saldoAtualizado)
        }

    }

    private fun pegouListaDeDespesas(listaDespesas: List<Despesa>?){
        viewModelScope.launch {
                _pegouListaDeDespesas.emit(listaDespesas)
        }
    }

    private fun pegouListaDeGanhos(listaGanhos: List<Ganho>?){
        viewModelScope.launch {
            _pegouListaDeGanhos.emit(listaGanhos)
        }
    }

    private fun pegouReceitaAtual(receita: Double){
        viewModelScope.launch {
            _pegouReceitaAtualizada.emit(receita)
        }
    }

    private fun pegouDespesas(despesas: Double){
        viewModelScope.launch {
            _pegouDespesasAtualizadas.emit(despesas)
        }
    }


    private fun pegouSaldoAtual(saldo: Double){
        viewModelScope.launch {
            _pegouSaldoAtual.emit(saldo)
        }
    }

    fun salvarReceitaMensal(receitaMensal: Editable) {
        viewModelScope.launch {
            try {
                val receita = receitaMensal.toString().toDouble()
                roomRepository.salvarReceitaMensal(receita, salvouReceita = ::salvouReceitaMensal)
            }catch (e: NumberFormatException){
                mostrarMensagemDeQuantidadeInvalida("quantidade invalida, verifique o preenchimento!")
            }


        }
    }

    private fun mostrarMensagemDeQuantidadeInvalida(msg: String) {
        viewModelScope.launch {
            _msgReceitaDigitadaInvalida.emit(msg)
        }
    }

    private fun salvouReceitaMensal(salvou: Boolean) {
        viewModelScope.launch {
            _salvouReceitaMensal.emit(salvou)
        }


    }

    fun prepararListasGanhoDespesaParaOAdapter(listaDespesas: List<Despesa>?, listaDeGanhos: List<Ganho>?): List<GanhoOuDespesa> {
        return ConversorGanhoOuDespesa.converteListaDeGanhosEListaDeDespesasParaListaGanhoOuDespesa(listaDeGanhos, listaDespesas)

    }

    fun calcularDespesaTotalAtual(listaDeDespesas: List<Despesa>?): Double? {
        if(listaDeDespesas == null){
            return null
        }else{
            var despesaAtual = 0.0
            listaDeDespesas.forEach {
                despesa->
                despesaAtual += despesa.custo

            }
            return despesaAtual
        }

    }

    fun formatarDouble(despesas: Double): String {
        return ConversorValoresDouble.formatarDouble(despesas)
    }



}