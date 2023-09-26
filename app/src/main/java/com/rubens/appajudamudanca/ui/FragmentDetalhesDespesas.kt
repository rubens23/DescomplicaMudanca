package com.rubens.appajudamudanca.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.rubens.appajudamudanca.R
import com.rubens.appajudamudanca.data.FakeDespesaOuGanho
import com.rubens.appajudamudanca.data.models.RVCardViewItem
import com.rubens.appajudamudanca.data.room.entities.Despesa
import com.rubens.appajudamudanca.databinding.FragmentDetalhesDespesasBinding
import com.rubens.appajudamudanca.ui.rvadapters.AdapterCardViewItem
import com.rubens.appajudamudanca.ui.rvadapters.AdapterGanhosDespesas
import com.rubens.appajudamudanca.ui.rvadapters.interfaces.CliqueItemRVFragmentOrganizacaoMudanca
import com.rubens.appajudamudanca.viewmodels.FragmentDetalhesDespesasViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FragmentDetalhesDespesas : Fragment(), CliqueItemRVFragmentOrganizacaoMudanca {


    private lateinit var binding: FragmentDetalhesDespesasBinding
    private lateinit var adapter: AdapterCardViewItem
    private lateinit var adapterGanhosDespesas: AdapterGanhosDespesas
    private lateinit var viewModel: FragmentDetalhesDespesasViewModel



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentDetalhesDespesasBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mostrarCategoriasIniciais()



        initViewModel()
        putCurrentMonthInMonthButton()
        initYearWithCurrentYear()
        onClickListeners()


    }

    private fun mostrarCategoriasIniciais() {
        val listaCategorias = listOf<RVCardViewItem>(
            RVCardViewItem("Alimentação", R.drawable.alimentacao),
            RVCardViewItem("Moradia", R.drawable.moradia),
            RVCardViewItem("Transporte", R.drawable.transporte),
            RVCardViewItem("Saúde", R.drawable.saude),
            RVCardViewItem("Educação", R.drawable.educacao),
            RVCardViewItem("Lazer", R.drawable.lazer),
            RVCardViewItem("Vestuário", R.drawable.vestuario),
            RVCardViewItem("Economias", R.drawable.economia),
            RVCardViewItem("Dívidas", R.drawable.dividas)
        )

        adapter = AdapterCardViewItem(listaCategorias, this)
        binding.rvCategorias.adapter = adapter
    }

    private fun putCurrentMonthInMonthButton() {
        val currentMonth: String = viewModel.getCurrentMonth()
        initCurrentMonthInAlertDialog(currentMonth)
        binding.tvBtnMonth.text = currentMonth
    }

    private fun initCurrentMonthInAlertDialog(currentMonth: String) {
        viewModel.mesSelecionado = currentMonth

    }

    private fun initYearWithCurrentYear() {
        viewModel.anoSelecionado = viewModel.getCurrentYear()
    }

    private fun onClickListeners() {
        binding.monthButton.setOnClickListener{
            showMonthSelectorDatePicker()
        }

        binding.btnVoltarCategoria.setOnClickListener {
            setSwitchMostrarBotaoDeCategoria(false)
            setBackBtnCategoriesVisibility(false)
            mostrarCategoriasIniciais()
            reinstanciarAdapterListaDeDespesas()

            //esconde o botao e depois tem que mostrar a recycler padrao
        }
    }

    private fun setSwitchMostrarBotaoDeCategoria(b: Boolean) {
        viewModel.switchBotaoVoltarCategorias = b
    }

    private fun showMonthSelectorDatePicker() {
        val inflater = layoutInflater
        val dialogView = inflater.inflate(R.layout.month_picker_layout, null)
        val builder = AlertDialog.Builder(requireContext())
        val monthTextView = dialogView.findViewById<TextView>(R.id.tv_month)
        val yearTextView = dialogView.findViewById<TextView>(R.id.tv_year)
        val botaoVoltarMes = dialogView.findViewById<ImageView>(R.id.btn_voltar_mes)
        val botaoAdiantarMes = dialogView.findViewById<ImageView>(R.id.btn_avancar_mes)

        monthTextView.text = viewModel.mesSelecionado
        yearTextView.text = viewModel.anoSelecionado




        clickListenersDasSetasDeAdiantarOuVoltarMes(botaoVoltarMes, botaoAdiantarMes, monthTextView, yearTextView)
        builder.setView(dialogView)
        builder.setTitle("")
        builder.setPositiveButton("OK") { _, _ ->
            binding.tvBtnMonth.text = viewModel.mesSelecionado

        }
        builder.setNegativeButton("Cancelar") { _, _ ->
            // Código a ser executado quando o botão "Cancelar" é clicado
        }

        val alertDialog: AlertDialog = builder.create()
        alertDialog.show()




    }

    private fun clickListenersDasSetasDeAdiantarOuVoltarMes(
        botaoVoltarMes: ImageView?,
        botaoAdiantarMes: ImageView?,
        monthTextView: TextView,
        yearTextView: TextView
    ) {
        botaoVoltarMes?.let { btnVoltar ->
            btnVoltar.setOnClickListener {
                val newSelectedMonth: ArrayList<String> = viewModel.subtractOneMonth(monthTextView, yearTextView)


                setarTextoNoBotaoDeMes(monthTextView, yearTextView, newSelectedMonth)


            }
        }

        botaoAdiantarMes?.let { btnAdiantar ->
            btnAdiantar.setOnClickListener {
                val newSelectedMonth: ArrayList<String> = viewModel.addOneMonth(monthTextView, yearTextView)

                setarTextoNoBotaoDeMes(monthTextView, yearTextView, newSelectedMonth)



            }

        }

    }

    private fun setarTextoNoBotaoDeMes(
        monthTextView: TextView,
        yearTextView: TextView,
        newSelectedMonth: ArrayList<String>
    ) {
        monthTextView.text = newSelectedMonth[0]
        yearTextView.text = newSelectedMonth[1]
        viewModel.anoSelecionado = newSelectedMonth[1]
        viewModel.mesSelecionado = newSelectedMonth[0]
    }


    private fun initViewModel() {
        viewModel = ViewModelProvider(requireActivity())[FragmentDetalhesDespesasViewModel::class.java]
        initCollectors()
    }

    private fun initCollectors() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.msgPegarListaPorCategoria.collectLatest {
                    msg->
                    if(msg == "Você não tem nenhuma despesa nessa categoria!"){
                        //nao tem nenhum item nessa categoria, adapter volta a fica vazio
                        reinstanciarAdapterListaDeDespesas()

                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.pegouListaDeDespesasPorCategoria.collectLatest {
                        listaDeDespesas->
                    initListaDespesas(listaDeDespesas)
                }
            }
        }
    }

    private fun reinstanciarAdapterListaDeDespesas() {
        adapterGanhosDespesas = AdapterGanhosDespesas(arrayListOf())
        binding.rvGanhosDespesas.adapter = adapterGanhosDespesas
    }

    private fun showToast(msg: String) {
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
    }

    private fun initListaDespesas(listaDeDespesas: List<Despesa>) {
        adapterGanhosDespesas = AdapterGanhosDespesas(viewModel.converterListaDespesaParaGanhoOuDespesa(listaDeDespesas))
        binding.rvGanhosDespesas.adapter = adapterGanhosDespesas
    }

    override fun clique(destino: String) {
        //destino é a mesma coisa que o nome da categoria
        abrirSubCategoria(destino)
        viewModel.switchBotaoVoltarCategorias = true
        setBackBtnCategoriesVisibility(viewModel.switchBotaoVoltarCategorias)

    }

    private fun setBackBtnCategoriesVisibility(podeMostrar: Boolean) {
        if(podeMostrar){
            binding.btnVoltarCategoria.visibility = View.VISIBLE

        }else{
            binding.btnVoltarCategoria.visibility = View.GONE

        }
    }

    private fun abrirSubCategoria(nomeCategoria: String) {
        viewModel.pegarDespesasPorCategoria(nomeCategoria)

        when(nomeCategoria){
            "Alimentação"->{
                val listaFiltrada = listOf<RVCardViewItem>(
                    RVCardViewItem("Restaurantes", R.drawable.restaurantes),
                RVCardViewItem("Supermercado", R.drawable.supermercado),
                RVCardViewItem("Cafeterias", R.drawable.cafeterias),
                RVCardViewItem("Fast Food", R.drawable.fastfood)
                )
                reinstanciarAdapter(listaFiltrada)


            }
            "Moradia"->{
                val listaFiltrada = listOf<RVCardViewItem>(
                    RVCardViewItem("Aluguel", R.drawable.aluguel),
                    RVCardViewItem("Financiamento", R.drawable.financiamento),
                    RVCardViewItem("Água", R.drawable.agua),
                    RVCardViewItem("Luz", R.drawable.luz),
                    RVCardViewItem("Gás", R.drawable.gas),
                    RVCardViewItem("Condomínio", R.drawable.condominio),
                    RVCardViewItem("Fundo de Emergência", R.drawable.fundodereserva))
                reinstanciarAdapter(listaFiltrada)


            }
            "Transporte"->{
                val listaFiltrada = listOf<RVCardViewItem>(
                    RVCardViewItem("Combustível", R.drawable.gasolina),
                    RVCardViewItem("Transporte público", R.drawable.transportepublico),
                    RVCardViewItem("Uber/Taxi", R.drawable.taxi))
                reinstanciarAdapter(listaFiltrada)



            }
            "Saúde"->{
                val listaFiltrada = listOf<RVCardViewItem>(
                    RVCardViewItem("Consultas Médicas", R.drawable.consultamedica),
                    RVCardViewItem("Medicamentos", R.drawable.medicamentos),
                    RVCardViewItem("Plano de Saúde", R.drawable.planodesaude2),
                    RVCardViewItem("Seguro", R.drawable.segurodesaude))
                reinstanciarAdapter(listaFiltrada)



            }
            "Educação"->{
                val listaFiltrada = listOf<RVCardViewItem>(
                    RVCardViewItem("Escola", R.drawable.escolauniversidade),
                    RVCardViewItem("Universidade", R.drawable.universidade),
                    RVCardViewItem("Material Didático", R.drawable.materiaisdidaticos),
                    RVCardViewItem("Livros", R.drawable.livros),
                    RVCardViewItem("Cursos", R.drawable.cursos))
                reinstanciarAdapter(listaFiltrada)



            }
            "Lazer"->{
                val listaFiltrada = listOf<RVCardViewItem>(
                    RVCardViewItem("Cinema", R.drawable.cinema),
                    RVCardViewItem("Shows", R.drawable.shows),
                    RVCardViewItem("Viagens", R.drawable.viagens),
                    RVCardViewItem("Hospedagem", R.drawable.hospedagem),
                    RVCardViewItem("Hobbies", R.drawable.hobbies))
                reinstanciarAdapter(listaFiltrada)



            }
            "Vestuário"->{
                val listaFiltrada = listOf<RVCardViewItem>(
                    RVCardViewItem("Roupas", R.drawable.roupas),
                    RVCardViewItem("Sapatos", R.drawable.sapatos),
                    RVCardViewItem("Acessórios", R.drawable.acessorios),
                    RVCardViewItem("Bolsas", R.drawable.bolsas))
                reinstanciarAdapter(listaFiltrada)



            }
            "Economias"->{
                val listaFiltrada = listOf<RVCardViewItem>(
                    RVCardViewItem("Poupança", R.drawable.poupanca),
                    RVCardViewItem("Investimentos", R.drawable.investimentos),
                    RVCardViewItem("Previdência Privada", R.drawable.previdencia))
                reinstanciarAdapter(listaFiltrada)


            }
            "Dívidas"->{
                val listaFiltrada = listOf<RVCardViewItem>(
                    RVCardViewItem("Cartões de Crédito", R.drawable.cartaodecredito),
                    RVCardViewItem("Empréstimos", R.drawable.emprestimo))
                reinstanciarAdapter(listaFiltrada)


            }

        }
    }

    private fun reinstanciarAdapter(listaFiltrada: List<RVCardViewItem>) {
        adapter = AdapterCardViewItem(listaFiltrada, this)
        binding.rvCategorias.adapter = adapter

    }


}