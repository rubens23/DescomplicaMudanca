package com.rubens.appajudamudanca.ui.rvadapters

import android.service.autofill.CustomDescription
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rubens.appajudamudanca.data.models.Dica
import com.rubens.appajudamudanca.data.room.entities.CustoMudanca
import com.rubens.appajudamudanca.databinding.ItemCustoOrcamentoBinding
import com.rubens.appajudamudanca.ui.utils.ConversorValoresDouble

class AdapterCustosMudanca(private val listaDeCustos: List<CustoMudanca>): RecyclerView.Adapter<AdapterCustosMudanca.ViewHolder>() {
    inner class ViewHolder(private val binding: ItemCustoOrcamentoBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(custoMudanca: CustoMudanca, position: Int){
            setCustoView(custoMudanca, position)
        }

        private fun setCustoView(custoMudanca: CustoMudanca, position: Int) {
            binding.tvLabelCusto.text = custoMudanca.nomeCusto
            val custoConvertido = ConversorValoresDouble.formatarDouble2(custoMudanca.valorCusto)
            val valorCustoEditable = Editable.Factory.getInstance().newEditable(custoConvertido)
            binding.etValorCusto.text = valorCustoEditable

            binding.etValorCusto.addTextChangedListener(object : TextWatcher{
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    listaDeCustos[position].valorCusto = s.toString().toDoubleOrNull()?: 0.0
                }

                override fun afterTextChanged(p0: Editable?) {

                }

            })
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCustoOrcamentoBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return listaDeCustos.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listaDeCustos[position], position)
    }

    fun getListInstance(): List<CustoMudanca>{
        return listaDeCustos
    }
}