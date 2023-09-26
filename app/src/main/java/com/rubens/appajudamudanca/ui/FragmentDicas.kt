package com.rubens.appajudamudanca.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.findNavController
import com.rubens.appajudamudanca.R
import com.rubens.appajudamudanca.data.models.Dica
import com.rubens.appajudamudanca.databinding.FragmentDicasBinding
import com.rubens.appajudamudanca.ui.rvadapters.AdapterDicas
import com.rubens.appajudamudanca.ui.rvadapters.interfaces.CliqueDica
import com.rubens.appajudamudanca.viewmodels.FragmentDicasViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FragmentDicas : Fragment(), CliqueDica {
    private lateinit var binding: FragmentDicasBinding
    private lateinit var viewModel: FragmentDicasViewModel
    private lateinit var adapter: AdapterDicas

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDicasBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViewModel()
        pegarTodasDicas()
    }

    private fun pegarTodasDicas() {
        viewModel.getAllDicas()
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(requireActivity())[FragmentDicasViewModel::class.java]
        initCollectors()
    }

    private fun initCollectors() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.dicas.collectLatest {
                    listaDeDicas->
                    initListaDeDicas(listaDeDicas)
                }
            }
        }
    }

    private fun initListaDeDicas(listaDeDicas: ArrayList<Dica>) {
        adapter = AdapterDicas(listaDeDicas, this)
        binding.rvDicas.adapter = adapter
    }


    override fun cliqueNaDica(dica: Dica) {
        if(view?.findNavController()?.currentDestination?.id == R.id.fragmentDicas){
            view?.findNavController()?.navigate(FragmentDicasDirections.actionFragmentDicasToFragmentDetalheDica2(dica))
        }
    }
}