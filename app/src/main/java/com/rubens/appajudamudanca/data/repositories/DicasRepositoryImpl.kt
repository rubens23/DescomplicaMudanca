package com.rubens.appajudamudanca.data.repositories

import android.util.Log
import com.rubens.appajudamudanca.data.datasource.DicasApi
import com.rubens.appajudamudanca.data.models.Dica
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class DicasRepositoryImpl @Inject constructor(
    private val dicasApi: DicasApi
): DicasRepository {
    override fun getAllDicas(dicas: (dicas: ArrayList<Dica>) -> Unit) {
        dicasApi.getDicas().enqueue(object: Callback<ArrayList<Dica>> {
            override fun onResponse(call: Call<ArrayList<Dica>>, response: Response<ArrayList<Dica>>){
                if (response.isSuccessful){
                    response.body()?.let { dicas(it) }


                    }else{
                        //deu erro
                }
            }

            override fun onFailure(call: Call<ArrayList<Dica>>, t: Throwable){
                Log.d("onfailure", "deu erro" + t.message.toString())

            }
        })
    }
}