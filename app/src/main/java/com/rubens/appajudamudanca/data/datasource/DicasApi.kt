package com.rubens.appajudamudanca.data.datasource

import com.rubens.appajudamudanca.data.models.Dica
import retrofit2.Call
import retrofit2.http.GET

interface DicasApi {

    @GET(END_POINT)
    fun getDicas(): Call<ArrayList<Dica>>

    companion object{
        const val END_POINT = "dicasmudanca.json"
        const val BASE_URL = "https://rubens23.github.io/api-dicas-mudanca/"
    }
}