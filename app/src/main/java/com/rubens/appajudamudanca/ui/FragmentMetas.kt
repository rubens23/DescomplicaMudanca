package com.rubens.appajudamudanca.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.rubens.appajudamudanca.R
import com.rubens.appajudamudanca.data.room.entities.CustoMudanca
import com.rubens.appajudamudanca.databinding.FragmentMetasBinding
import com.rubens.appajudamudanca.ui.rvadapters.AdapterCustosMudanca
import com.rubens.appajudamudanca.ui.rvadapters.AdapterMetas
import com.rubens.appajudamudanca.viewmodels.FragmentMetasViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class FragmentMetas : Fragment() {


    private lateinit var adapter: AdapterMetas
    private lateinit var binding: FragmentMetasBinding
    private lateinit var viewModel: FragmentMetasViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMetasBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViewModel()
        onClickListeners()
    }

    private fun onClickListeners() {
        binding.btnSalvarModificacoesMetas.setOnClickListener {
            viewModel.atualizarMetas(adapter.getListInstance())

        }
    }

    override fun onResume() {
        super.onResume()

        pegarEconomias()
        initListaDeCustos()

    }

    private fun pegarEconomias() {
        viewModel.pegarValorDisponivelParaGastar()
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(requireActivity())[FragmentMetasViewModel::class.java]
        initCollectors()

    }

    private fun initCollectors() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.pegouAListaDeCustos.collectLatest {
                        listaDeCustos->
                    mostrarListaDeMetas(listaDeCustos)
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.valorEconomizadoRestante.collectLatest {
                        valorRestante->
                    mostrarValorEconomizadoRestanteNaTextView(valorRestante)
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.msgSalvouAlteracaoNasMetas.collectLatest {
                        msg->
                    showToast(msg)
                }
            }
        }
    }

    private fun showToast(msg: String) {
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
    }

    private fun mostrarValorEconomizadoRestanteNaTextView(valorRestante: Double) {
        val valorConvertido = viewModel.converterValor(valorRestante)
        binding.tvValorEconomizadoRestante.text = "R$ $valorConvertido"

    }

    private fun initListaDeCustos() {
        viewModel.pegarTodosOsCustos()
    }

    private fun mostrarListaDeMetas(listaDeCustos: List<CustoMudanca>) {
        adapter = AdapterMetas(listaDeCustos)
        binding.rvMetas.adapter = adapter
    }


}