package com.rubens.appajudamudanca.ui.rvadapters

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rubens.appajudamudanca.data.room.entities.CustoMudanca
import com.rubens.appajudamudanca.databinding.ItemCustoOrcamentoBinding
import com.rubens.appajudamudanca.databinding.ItemMetaBinding
import com.rubens.appajudamudanca.ui.utils.ConversorValoresDouble

class AdapterMetas(private val listaDeCustos: List<CustoMudanca>): RecyclerView.Adapter<AdapterMetas.ViewHolder>() {
    inner class ViewHolder(private val binding: ItemMetaBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(meta: CustoMudanca, position: Int){
            setCustoView(meta, position)
        }

        private fun setCustoView(meta: CustoMudanca, position: Int) {
            binding.tvLabelNomeMeta.text = meta.nomeCusto
            val custoConvertido = ConversorValoresDouble.formatarDouble2(meta.valorCusto)
            binding.tvTotalMeta.text = "R$ ${custoConvertido}"
            val editableValorEconomizado = Editable.Factory.getInstance().newEditable(ConversorValoresDouble.formatarDouble2(meta.valorEconomizado))
            binding.etQntTotalMeta.text = editableValorEconomizado

            binding.etQntTotalMeta.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    listaDeCustos[position].valorEconomizado = s.toString().toDoubleOrNull()?: 0.0
                }

                override fun afterTextChanged(p0: Editable?) {

                }

            })
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemMetaBinding.inflate(LayoutInflater.from(parent.context), parent, false)

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