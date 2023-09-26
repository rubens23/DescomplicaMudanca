package com.rubens.appajudamudanca.ui

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.rubens.appajudamudanca.R
import com.rubens.appajudamudanca.data.room.entities.CustoMudanca
import com.rubens.appajudamudanca.databinding.FragmentEstabelecerOrcamentoBinding
import com.rubens.appajudamudanca.ui.rvadapters.AdapterCustosMudanca
import com.rubens.appajudamudanca.viewmodels.FragmentEstabelecerOrcamentoViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FragmentEstabelecerOrcamento : Fragment() {


    private var valorCusto: EditText? = null
    private var nomeCustoEditText: EditText? = null
    private lateinit var binding: FragmentEstabelecerOrcamentoBinding
    private lateinit var viewModel: FragmentEstabelecerOrcamentoViewModel
    private lateinit var adapter: AdapterCustosMudanca

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEstabelecerOrcamentoBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViewModel()

        onClickListeners()
    }

    override fun onResume() {
        super.onResume()

        initListaDeCustos()

    }

    private fun initListaDeCustos() {
        viewModel.pegarTodosOsCustos()
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(requireActivity())[FragmentEstabelecerOrcamentoViewModel::class.java]
        initCollectors()
    }

    private fun initCollectors() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.msgErroPreenchimentoNovoCusto.collectLatest {
                    msg->
                    showToast(msg)
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.msgSalvouAlteracaoNoCusto.collectLatest {
                        msg->
                    showToast(msg)
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.salvouNovoCusto.collectLatest {
                        salvou->
                    if(salvou){
                        showToast("salvou novo custo com sucesso")
                        //ele recarrega toda a lista. Futuramente eu posso implementar
                        //o codigo para mostrar somente o item novo da lista
                        //e manter o resto da lista como estava
                        initListaDeCustos()
                    } else{
                        showToast("erro ao salvar custo")
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.pegouAListaDeCustos.collectLatest {
                    listaDeCustos->
                    mostrarListaDeCustos(listaDeCustos)
                }
            }
        }
    }

    private fun mostrarListaDeCustos(listaDeCustos: List<CustoMudanca>) {
        adapter = AdapterCustosMudanca(listaDeCustos)
        binding.rvCustosMudanca.adapter = adapter
    }

    private fun showToast(msg: String) {
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
    }

    private fun onClickListeners() {
        binding.btnAddCusto.setOnClickListener {
            showDialog()
        }
        binding.btnSalvarCustos.setOnClickListener {
            viewModel.atualizarCustos(adapter.getListInstance())
        }

    }


    private fun showDialog(){
        val dialog = Dialog(requireContext())

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.bottomsheet_custos)

        val btnSalvarNovoCusto = dialog.findViewById<Button>(R.id.btn_salvar_novo_custo)
        nomeCustoEditText = dialog.findViewById<EditText>(R.id.et_nome_custo)
        valorCusto = dialog.findViewById<EditText>(R.id.et_valor_custo)

        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.attributes?.windowAnimations = R.style.DialogAnimation
        dialog.window?.setGravity(Gravity.BOTTOM)

        dialog.show()

        initBtnSalvarNovoCustoClick(btnSalvarNovoCusto, dialog)



    }

    private fun initBtnSalvarNovoCustoClick(btnSalvarNovoCusto: Button?, dialog: Dialog) {
        btnSalvarNovoCusto?.let {
            it.setOnClickListener {
                viewModel.salvarNovoCustoDeMudanca(nomeCustoEditText, valorCusto)
            }
        }
    }
}