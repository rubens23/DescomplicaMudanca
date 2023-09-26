package com.rubens.appajudamudanca.ui.rvadapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rubens.appajudamudanca.data.models.RVCardViewItem
import com.rubens.appajudamudanca.databinding.ItemCategoriaBinding
import com.rubens.appajudamudanca.ui.rvadapters.interfaces.CliqueItemRVFragmentOrganizacaoMudanca

class AdapterCardViewItem(private val listaCategorias: List<RVCardViewItem>,
                          private val cliqueItemRVFragmentOrganizacaoMudanca: CliqueItemRVFragmentOrganizacaoMudanca? = null): RecyclerView.Adapter<AdapterCardViewItem.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding = ItemCategoriaBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    inner class ViewHolder(private val binding: ItemCategoriaBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(categoria: RVCardViewItem){
            binding.ivCategoria.setImageResource(categoria.foto)
            binding.tvCategoria.text = categoria.nome
            binding.cardItem.setOnClickListener {
                cliqueItemRVFragmentOrganizacaoMudanca?.clique(categoria.nome)
            }

        }



    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listaCategorias[position])
    }

    override fun getItemCount(): Int = listaCategorias.size
}