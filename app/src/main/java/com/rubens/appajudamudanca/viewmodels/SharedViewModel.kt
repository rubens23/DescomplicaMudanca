package com.rubens.appajudamudanca.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rubens.appajudamudanca.data.repositories.RoomRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor(
    private val roomRepository: RoomRepository
): ViewModel() {

    private val _atualizouListaDeDespesas: MutableSharedFlow<Boolean> = MutableSharedFlow(replay = 0)
    val atualizouListaDeDespesas: SharedFlow<Boolean> = _atualizouListaDeDespesas

    private val _atualizouListaDeGanhos: MutableSharedFlow<Boolean> = MutableSharedFlow(replay = 0)
    val atualizouListaDeGanhos: SharedFlow<Boolean> = _atualizouListaDeGanhos

    private val _podeAtualizarSaldoDepoisDeGanhoOuDespesa: MutableSharedFlow<Boolean> = MutableSharedFlow(replay = 0)
    val podeAtualizarSaldoDepoisDeGanhoOuDespesa: SharedFlow<Boolean> = _podeAtualizarSaldoDepoisDeGanhoOuDespesa


    fun listaDeDespesasFoiAtualizada(){
        viewModelScope.launch {
            _atualizouListaDeDespesas.emit(true)
        }
    }

    fun atualizarSaldoDepoisDeGanhoOuDespesa() {
        viewModelScope.launch {
            _podeAtualizarSaldoDepoisDeGanhoOuDespesa.emit(true)
        }
    }

    fun listaDeGanhosFoiAtualizada() {
        viewModelScope.launch {
            _atualizouListaDeGanhos.emit(true)
        }
    }

}