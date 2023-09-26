package com.rubens.appajudamudanca.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rubens.appajudamudanca.data.repositories.RoomRepository
import com.rubens.appajudamudanca.data.room.entities.CustoMudanca
import com.rubens.appajudamudanca.ui.utils.ConversorValoresDouble
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FragmentMetasViewModel @Inject constructor(
    private val roomRepository: RoomRepository
): ViewModel() {

    private val _pegouAListaDeCustos: MutableSharedFlow<List<CustoMudanca>> = MutableSharedFlow(replay = 0)
    val pegouAListaDeCustos: SharedFlow<List<CustoMudanca>> = _pegouAListaDeCustos

    private val _valorEconomizadoRestante: MutableSharedFlow<Double> = MutableSharedFlow(replay = 0)
    val valorEconomizadoRestante: SharedFlow<Double> = _valorEconomizadoRestante


    private val _msgSalvouAlteracaoNasMetas: MutableSharedFlow<String> = MutableSharedFlow(replay = 0)
    val msgSalvouAlteracaoNasMetas: SharedFlow<String> = _msgSalvouAlteracaoNasMetas

    fun pegarTodosOsCustos() {
        viewModelScope.launch {
            roomRepository.pegarTodosOsCustos(pegouCustos= ::pegouTodosOsCustos)
        }
    }

    fun pegarValorDisponivelParaGastar() {
        viewModelScope.launch {
            roomRepository.pegarValorEconomizadoRestante(valorEconomizadoRestante = ::pegouValorEconomizadoRestante)
        }
    }

    fun pegouTodosOsCustos(listaDeCustos: List<CustoMudanca>?){
        viewModelScope.launch {
            if(!listaDeCustos.isNullOrEmpty()){
                _pegouAListaDeCustos.emit(listaDeCustos)
            }
        }
    }



    private fun pegouValorEconomizadoRestante(valorRestante: Double){
        viewModelScope.launch {
            _valorEconomizadoRestante.emit(valorRestante)

        }
    }

    fun converterValor(valorRestante: Double): String {
        return ConversorValoresDouble.formatarDouble2(valorRestante)
    }

    fun atualizarMetas(listaDeCustos: List<CustoMudanca>) {
        viewModelScope.launch {
            roomRepository.atualizarCustos(listaDeCustos, salvouAlteracoes = ::salvouAlteracoesNasMetas)
        }

    }

    private fun salvouAlteracoesNasMetas(salvou: Boolean) {
        viewModelScope.launch {
            if (salvou){
                _msgSalvouAlteracaoNasMetas.emit("alterações foram salvas com sucesso")

            }else{
                _msgSalvouAlteracaoNasMetas.emit("falha ao salvar alterações")

            }
        }

    }
}