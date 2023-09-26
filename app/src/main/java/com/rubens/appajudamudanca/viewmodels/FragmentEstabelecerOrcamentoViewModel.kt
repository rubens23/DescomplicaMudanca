package com.rubens.appajudamudanca.viewmodels

import android.widget.EditText
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rubens.appajudamudanca.data.repositories.RoomRepository
import com.rubens.appajudamudanca.data.room.entities.CustoMudanca
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import java.lang.NumberFormatException
import javax.inject.Inject

@HiltViewModel
class FragmentEstabelecerOrcamentoViewModel @Inject constructor(
    private val roomRepository: RoomRepository
): ViewModel() {

    private val _msgErroPreenchimentoNovoCusto: MutableSharedFlow<String> = MutableSharedFlow(replay = 0)
    val msgErroPreenchimentoNovoCusto: SharedFlow<String> = _msgErroPreenchimentoNovoCusto

    private val _msgSalvouAlteracaoNoCusto: MutableSharedFlow<String> = MutableSharedFlow(replay = 0)
    val msgSalvouAlteracaoNoCusto: SharedFlow<String> = _msgSalvouAlteracaoNoCusto

    private val _salvouNovoCusto: MutableSharedFlow<Boolean> = MutableSharedFlow(replay = 0)
    val salvouNovoCusto: SharedFlow<Boolean> = _salvouNovoCusto


    private val _pegouAListaDeCustos: MutableSharedFlow<List<CustoMudanca>> = MutableSharedFlow(replay = 0)
    val pegouAListaDeCustos: SharedFlow<List<CustoMudanca>> = _pegouAListaDeCustos



    fun salvarNovoCustoDeMudanca(nomeCustoEditText: EditText?, valorCusto: EditText?) {
        viewModelScope.launch {
            if(!nomeCustoEditText?.text.isNullOrEmpty()
                && !valorCusto?.text.isNullOrEmpty()){
                try{
                    roomRepository.salvarNovoCusto(CustoMudanca(nomeCusto = nomeCustoEditText!!.text.toString(),
                    valorCusto = valorCusto!!.text.toString().toDouble()), salvouCusto = ::salvouNovoCusto)
                }catch (e: NumberFormatException){
                    _msgErroPreenchimentoNovoCusto.emit("o valor digitado não é válido")

                }

            }else{
                _msgErroPreenchimentoNovoCusto.emit("Preencha todos os dados")
            }
        }
    }

    fun pegarTodosOsCustos() {
        viewModelScope.launch {
            roomRepository.pegarTodosOsCustos(pegouCustos= ::pegouTodosOsCustos)
        }
    }

    fun atualizarCustos(listaDeCustos: List<CustoMudanca>) {
        viewModelScope.launch {
            roomRepository.atualizarCustos(listaDeCustos, salvouAlteracoes = ::salvouAlteracoesNosCustos)
        }

    }

    private fun salvouAlteracoesNosCustos(salvou: Boolean) {
        viewModelScope.launch {
            if (salvou){
                _msgSalvouAlteracaoNoCusto.emit("alterações foram salvas com sucesso")

            }else{
                _msgSalvouAlteracaoNoCusto.emit("falha ao salvar alterações")

            }
        }

    }

    fun pegouTodosOsCustos(listaDeCustos: List<CustoMudanca>?){
        viewModelScope.launch {
            if(!listaDeCustos.isNullOrEmpty()){
                _pegouAListaDeCustos.emit(listaDeCustos)
            }
        }
    }

    private fun salvouNovoCusto(salvou: Boolean){
        viewModelScope.launch {
            _salvouNovoCusto.emit(salvou)
        }
    }




}