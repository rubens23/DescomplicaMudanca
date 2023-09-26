package com.rubens.appajudamudanca.viewmodels

import android.widget.EditText
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rubens.appajudamudanca.data.repositories.RoomRepository
import com.rubens.appajudamudanca.data.repositories.RoomRepositoryImpl
import com.rubens.appajudamudanca.data.room.entities.Despesa
import com.rubens.appajudamudanca.data.room.entities.Ganho
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import java.lang.NullPointerException
import java.lang.NumberFormatException
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val roomRepository: RoomRepository
): ViewModel() {

    var categoriaEscolhida: String = ""
    private val _msgErroPreenchimentoDadosDespesaGanho: MutableSharedFlow<String> = MutableSharedFlow(replay = 0)
    val msgErroPreenchimentoDadosDespesaGanho: SharedFlow<String> = _msgErroPreenchimentoDadosDespesaGanho

    private val _salvouNovaDespesa: MutableSharedFlow<Boolean> = MutableSharedFlow(replay = 0)
    val salvouNovaDespesa: SharedFlow<Boolean> = _salvouNovaDespesa

    private val _salvouNovoGanho: MutableSharedFlow<Boolean> = MutableSharedFlow(replay = 0)
    val salvouNovoGanho: SharedFlow<Boolean> = _salvouNovoGanho

    var switchBotaoVoltarCategorias = false




    fun salvarNovaDespesa(nomeDespesaOuGanhoEditText: EditText?, custoOuGanhoEditText: EditText?) {
        viewModelScope.launch {
                if(!nomeDespesaOuGanhoEditText?.text.isNullOrEmpty()
                    && !custoOuGanhoEditText?.text.isNullOrEmpty()){
                    try {
                        roomRepository.salvarNovaDespesa(Despesa(nomeDespesa = nomeDespesaOuGanhoEditText!!.text.toString(),
                            categoria = categoriaEscolhida, custo = custoOuGanhoEditText!!.text.toString().toDouble()), salvouDespesa = ::salvouNovaDespesa)
                    }catch (e: NumberFormatException){
                        _msgErroPreenchimentoDadosDespesaGanho.emit("o valor digitado não é válido")
                    }

                }else{
                    _msgErroPreenchimentoDadosDespesaGanho.emit("Preencha todos os dados")
                }

        }

    }

    fun salvarNovoGanho(nomeDespesaOuGanhoEditText: EditText?, custoOuGanhoEditText: EditText?) {
        viewModelScope.launch {
            if(!nomeDespesaOuGanhoEditText?.text.isNullOrEmpty()
                && !custoOuGanhoEditText?.text.isNullOrEmpty()){
                try {
                    roomRepository.salvarNovoGanho(
                        Ganho(nomeGanho = nomeDespesaOuGanhoEditText!!.text.toString(), ganho = custoOuGanhoEditText!!.text.toString().toDouble()), salvouGanho = ::salvouNovoGanho)
                }catch (e: NumberFormatException){
                    _msgErroPreenchimentoDadosDespesaGanho.emit("o valor digitado não é válido")
                }

            }else{
                _msgErroPreenchimentoDadosDespesaGanho.emit("Preencha todos os dados")
            }

        }
    }




    private fun salvouNovaDespesa(salvou: Boolean){
        viewModelScope.launch {
            _salvouNovaDespesa.emit(salvou)
        }
    }

    private fun salvouNovoGanho(salvou: Boolean){
        viewModelScope.launch {
            _salvouNovoGanho.emit(salvou)
        }
    }



}