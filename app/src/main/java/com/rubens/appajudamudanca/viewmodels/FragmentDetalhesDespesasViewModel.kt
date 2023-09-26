package com.rubens.appajudamudanca.viewmodels

import android.widget.TextView
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rubens.appajudamudanca.data.models.GanhoOuDespesa
import com.rubens.appajudamudanca.data.repositories.RoomRepository
import com.rubens.appajudamudanca.data.room.entities.Despesa
import com.rubens.appajudamudanca.data.utils.ConversorGanhoOuDespesa
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class FragmentDetalhesDespesasViewModel @Inject constructor(
    private val roomRepository: RoomRepository
): ViewModel() {

    var mesSelecionado: String = ""
    var anoSelecionado: String = ""
    var switchBotaoVoltarCategorias = false

    private val _msgPegarListaPorCategoria: MutableSharedFlow<String> = MutableSharedFlow(replay = 0)
    val msgPegarListaPorCategoria: SharedFlow<String> = _msgPegarListaPorCategoria

    private val _pegouListaDeDespesasPorCategoria: MutableSharedFlow<List<Despesa>> = MutableSharedFlow(replay = 0)
    val pegouListaDeDespesasPorCategoria: SharedFlow<List<Despesa>> = _pegouListaDeDespesasPorCategoria


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


    fun pegarDespesasPorCategoria(categoria: String){
        viewModelScope.launch {
            roomRepository.pegarDespesasPorCategoria(categoria, pegouDespesas = ::pegouDespesasPorCategoria)
        }
    }

    private fun pegouDespesasPorCategoria(listaDespesas: List<Despesa>?){
        viewModelScope.launch {
            if (!listaDespesas.isNullOrEmpty()){
                _pegouListaDeDespesasPorCategoria.emit(listaDespesas)


            }else{
                _msgPegarListaPorCategoria.emit("Você não tem nenhuma despesa nessa categoria!")
            }
        }
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


    fun converterListaDespesaParaGanhoOuDespesa(listaDespesas: List<Despesa>): List<GanhoOuDespesa> {
        return ConversorGanhoOuDespesa.converteListaDeGanhosEListaDeDespesasParaListaGanhoOuDespesa(listaDespesas = listaDespesas)

    }
}