package com.rubens.appajudamudanca.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rubens.appajudamudanca.data.models.Dica
import com.rubens.appajudamudanca.data.repositories.DicasRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import java.util.ArrayList
import javax.inject.Inject

@HiltViewModel
class FragmentDicasViewModel @Inject constructor(
    val dicasRepository: DicasRepository
): ViewModel() {

    private val _dicas: MutableSharedFlow<ArrayList<Dica>> = MutableSharedFlow(replay = 0)
    val dicas: SharedFlow<ArrayList<Dica>> = _dicas

    fun getAllDicas(){
        dicasRepository.getAllDicas(dicas = ::pegouAsDicas)
    }

    private fun pegouAsDicas(dicas: ArrayList<Dica>) {
        viewModelScope.launch {
            _dicas.emit(dicas)

        }
    }
}