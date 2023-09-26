package com.rubens.appajudamudanca.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.rubens.appajudamudanca.R
import com.rubens.appajudamudanca.data.models.RVCardViewItem
import com.rubens.appajudamudanca.databinding.FragmentOrganizacaoMudancaBinding
import com.rubens.appajudamudanca.ui.rvadapters.AdapterCardViewItem
import com.rubens.appajudamudanca.ui.rvadapters.interfaces.CliqueItemRVFragmentOrganizacaoMudanca


class FragmentOrganizacaoMudanca : Fragment(), CliqueItemRVFragmentOrganizacaoMudanca {


    private lateinit var binding: FragmentOrganizacaoMudancaBinding
    private lateinit var adapter: AdapterCardViewItem


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOrganizacaoMudancaBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val listaOpcoes = listOf<RVCardViewItem>(
            RVCardViewItem("estabeleça um orçamento", R.drawable.estabelecerorcamento),
            RVCardViewItem("economias para mudança", R.drawable.economiasparamudancas),
            RVCardViewItem("metas para mudança", R.drawable.metaspramudanca),
            RVCardViewItem("dicas mudança", R.drawable.dicasmudanca)
        )



        adapter = AdapterCardViewItem(listaOpcoes, this)
        binding.rvOrganizacaoMudanca.adapter = adapter


    }

    override fun clique(destino: String) {

                when (destino) {
                    "estabeleça um orçamento" -> {
                        if(view?.findNavController()?.currentDestination?.id == R.id.navigationOrganizacaoMudanca){
                            view?.findNavController()?.navigate(FragmentOrganizacaoMudancaDirections.actionNavigationOrganizacaoMudancaToFragmentEstabelecerOrcamento())
                        }
                    }
                    "economias para mudança" -> {
                        if(view?.findNavController()?.currentDestination?.id == R.id.navigationOrganizacaoMudanca){
                            view?.findNavController()?.navigate(FragmentOrganizacaoMudancaDirections.actionNavigationOrganizacaoMudancaToFragmentEconomias())
                        }
                    }
                    "metas para mudança" -> {
                        if(view?.findNavController()?.currentDestination?.id == R.id.navigationOrganizacaoMudanca){
                            view?.findNavController()?.navigate(FragmentOrganizacaoMudancaDirections.actionNavigationOrganizacaoMudancaToFragmentMetas())
                        }
                    }
                    "dicas mudança" -> {
                        if(view?.findNavController()?.currentDestination?.id == R.id.navigationOrganizacaoMudanca){
                            view?.findNavController()?.navigate(FragmentOrganizacaoMudancaDirections.actionNavigationOrganizacaoMudancaToFragmentDicas())
                        }
                    }
                    else -> {}
                }
    }

}