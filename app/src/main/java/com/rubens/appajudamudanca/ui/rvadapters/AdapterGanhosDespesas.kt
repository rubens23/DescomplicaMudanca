package com.rubens.appajudamudanca.ui.rvadapters

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.rubens.appajudamudanca.R
import com.rubens.appajudamudanca.data.models.GanhoOuDespesa
import com.rubens.appajudamudanca.databinding.ItemDemonstrativoMensalBinding
import com.rubens.appajudamudanca.ui.rvadapters.interfaces.CliquePaymentMethod
import com.rubens.appajudamudanca.ui.utils.ConversorValoresDouble

/**
 * essa classe pode receber uma lista com despesas e ganhos, ou uma lista
 * só com despesas.
 */

class AdapterGanhosDespesas(private val listaGanhosDespesas: List<GanhoOuDespesa>, private val cliquePaymentMethod: CliquePaymentMethod? = null): RecyclerView.Adapter<AdapterGanhosDespesas.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding = ItemDemonstrativoMensalBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    inner class ViewHolder(private val binding: ItemDemonstrativoMensalBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(ganhoOuDespesa: GanhoOuDespesa){
            if(ganhoOuDespesa.despesaOuGanho == "ganho"){
                setViewsGanho(ganhoOuDespesa)

            }else{
                setViewsDespesa(ganhoOuDespesa)
                setCashPaymentOnClickListeners(ganhoOuDespesa)
                setCardPaymentOnClickListeners(ganhoOuDespesa)


            }

        }

        private fun setViewsGanho(ganhoOuDespesa: GanhoOuDespesa) {
            binding.tvNomePrincipal.text = ganhoOuDespesa.nomeDespesaGanho
            binding.tvGanhoGasto.text = "R$ ${ConversorValoresDouble.formatarDouble(ganhoOuDespesa.valorDespesaGanho)}"
            binding.ivBgGanho.visibility = View.VISIBLE
            binding.ivGanho.visibility = View.VISIBLE
            binding.ivDespesaGanho.visibility = View.INVISIBLE
            binding.ivDespesa.visibility = View.INVISIBLE
        }


        private fun setViewsDespesa(ganhoOuDespesa: GanhoOuDespesa) {
            binding.tvNomePrincipal.text = ganhoOuDespesa.nomeDespesaGanho
            binding.tvGanhoGasto.text = "R$ ${ConversorValoresDouble.formatarDouble(ganhoOuDespesa.valorDespesaGanho)}"
            configureButtonAppearance(ganhoOuDespesa)
            binding.ivBgGanho.visibility = View.INVISIBLE
            binding.ivGanho.visibility = View.INVISIBLE
            binding.ivDespesaGanho.visibility = View.VISIBLE
            binding.ivDespesa.visibility = View.VISIBLE
        }


        private fun setCardPaymentOnClickListeners(despesa: GanhoOuDespesa) {
            binding.paymentCard.setOnClickListener {
                val isPressed = colocarStatePressedDoBotaoCard()
                cliquePaymentMethod?.cliquePagamentoEmCartao(isPressed, despesa)

            }
            binding.ivCard.setOnClickListener {
                val isPressed = colocarStatePressedDoBotaoCard()
                cliquePaymentMethod?.cliquePagamentoEmCartao(isPressed, despesa)

            }
            binding.tvCard.setOnClickListener {
                val isPressed = colocarStatePressedDoBotaoCard()
                cliquePaymentMethod?.cliquePagamentoEmCartao(isPressed, despesa)

            }
        }



        private fun setCashPaymentOnClickListeners(despesa: GanhoOuDespesa) {
            binding.paymentCash.setOnClickListener {
                val isPressed = colocarStatePressedDoBotaoCash()
                cliquePaymentMethod?.cliquePagamentoEmDinheiro(isPressed, despesa)

            }
            binding.ivCash.setOnClickListener {
                val isPressed = colocarStatePressedDoBotaoCash()
                cliquePaymentMethod?.cliquePagamentoEmDinheiro(isPressed, despesa)

            }
            binding.tvCash.setOnClickListener {
                val isPressed = colocarStatePressedDoBotaoCash()
                cliquePaymentMethod?.cliquePagamentoEmDinheiro(isPressed, despesa)

            }
        }

        private fun colocarStatePressedDoBotaoCash(): Boolean {
            binding.paymentCash.isPressed = !binding.paymentCash.isPressed
            if(binding.paymentCash.isPressed){
                //o botao de cartao tem que ser desselecionado
                binding.paymentCard.isPressed = false
                mudarBotaoDeCartaoQuandoDesselecionado()
                mudarBotaoDeDinheiroQuandoSelecionado()
            }
            return binding.paymentCash.isPressed

        }


        private fun colocarStatePressedDoBotaoCard(): Boolean {
            binding.paymentCard.isPressed = !binding.paymentCard.isPressed
            if(binding.paymentCard.isPressed){
                //o botao de dinheiro tem que ser desselecionado
                binding.paymentCash.isPressed = false

                mudarBotaoDeCartaoQuandoSelecionado()
                mudarBotaoDeDinheiroQuandoDesselecionado()


                binding.tvCard.setTextColor(ContextCompat.getColor(binding.root.context, R.color.white))
            }
            return binding.paymentCard.isPressed
        }

        private fun mudarBotaoDeDinheiroQuandoSelecionado() {
            binding.tvCash.setTextColor(ContextCompat.getColor(binding.root.context, R.color.white))
            binding.ivCash.imageTintList = ColorStateList.valueOf(ContextCompat.getColor(binding.root.context, R.color.white))
        }




        private fun mudarBotaoDeDinheiroQuandoDesselecionado() {
            binding.tvCash.setTextColor(ContextCompat.getColor(binding.root.context, R.color.lighter_black))
            binding.ivCash.imageTintList = ColorStateList.valueOf(ContextCompat.getColor(binding.root.context, R.color.lighter_black))
        }

        private fun mudarBotaoDeCartaoQuandoSelecionado() {
            binding.tvCard.setTextColor(ContextCompat.getColor(binding.root.context, R.color.white))
            binding.ivCard.setImageResource(R.drawable.cardwhite)

        }

        private fun mudarBotaoDeCartaoQuandoDesselecionado() {
            binding.tvCard.setTextColor(ContextCompat.getColor(binding.root.context, R.color.lighter_black))
            binding.ivCard.setImageResource(R.drawable.card)
        }


        private fun configureButtonAppearance(ganhoOuDespesa: GanhoOuDespesa) {
            when(ganhoOuDespesa.formaPagamentoDespesaOuGanho){
                "dinheiro"->{
                    binding.paymentCash.isPressed = true
                    binding.paymentCard.isPressed = false
                mudarBotaoDeCartaoQuandoDesselecionado()
                mudarBotaoDeDinheiroQuandoSelecionado()}
                "cartao"->{
                    binding.paymentCash.isPressed = false
                    binding.paymentCard.isPressed = true
                    mudarBotaoDeCartaoQuandoSelecionado()
                    mudarBotaoDeDinheiroQuandoDesselecionado()
                }
            }

        }


    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listaGanhosDespesas[position])
    }

    override fun getItemCount(): Int = listaGanhosDespesas.size
}