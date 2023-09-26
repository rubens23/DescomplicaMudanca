package com.rubens.appajudamudanca.data.repositories

import com.rubens.appajudamudanca.data.models.Dica

interface DicasRepository {
    fun getAllDicas(dicas: (dicas: ArrayList<Dica>)->Unit)
}