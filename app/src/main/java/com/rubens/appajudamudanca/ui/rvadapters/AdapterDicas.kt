package com.rubens.appajudamudanca.ui.rvadapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.rubens.appajudamudanca.data.models.Dica
import com.rubens.appajudamudanca.databinding.ItemDicaBinding
import com.rubens.appajudamudanca.ui.rvadapters.interfaces.CliqueDica

class AdapterDicas(private val listaDicas: ArrayList<Dica>,
                   private val cliqueDica: CliqueDica
): RecyclerView.Adapter<AdapterDicas.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemDicaBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun getItemCount(): Int{

        return listaDicas.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.bind(listaDicas[position])
    }

    inner class ViewHolder(private val binding: ItemDicaBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(dica: Dica){
            binding.tvTitleDica.text = dica.titulo_dica
            if(dica.foto_dica != ""){
                Glide.with(binding.root.context).load(dica.foto_dica).into(binding.ivFotoDica)

            }
            binding.cvDica.setOnClickListener {
                cliqueDica.cliqueNaDica(dica)
            }

        }
    }
}