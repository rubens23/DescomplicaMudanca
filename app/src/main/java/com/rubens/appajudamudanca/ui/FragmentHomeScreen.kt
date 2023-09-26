package com.rubens.appajudamudanca.ui

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.findNavController
import com.rubens.appajudamudanca.R
import com.rubens.appajudamudanca.data.models.GanhoOuDespesa
import com.rubens.appajudamudanca.data.room.entities.Despesa
import com.rubens.appajudamudanca.data.room.entities.Ganho
import com.rubens.appajudamudanca.databinding.FragmentHomeScreenBinding
import com.rubens.appajudamudanca.ui.rvadapters.AdapterGanhosDespesas
import com.rubens.appajudamudanca.ui.rvadapters.interfaces.CliquePaymentMethod
import com.rubens.appajudamudanca.viewmodels.FragmentHomeScreenViewModel
import com.rubens.appajudamudanca.viewmodels.SharedViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FragmentHomeScreen : Fragment(), CliquePaymentMethod {


    private var listaDespesas: List<Despesa>? = null
    private lateinit var dialogReceitaMensal: Dialog
    private lateinit var binding: FragmentHomeScreenBinding
    private lateinit var adapter: AdapterGanhosDespesas
    private lateinit var viewModel: FragmentHomeScreenViewModel
    private val sharedViewModel by activityViewModels<SharedViewModel>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeScreenBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViewModel()
        putCurrentMonthInMonthButton()
        initYearWithCurrentYear()
        onClickListeners()


    }

    private fun initYearWithCurrentYear() {
        viewModel.anoSelecionado = viewModel.getCurrentYear()
    }

    override fun onResume() {
        super.onResume()

        putSaldoAtual()
        atualizarTvReceitaMensal()
        atualizarTvDespesas()
        initListaDespesasEGanhos()

    }

    private fun initListaDespesasEGanhos() {
        pegarListaDeDespesas()
    }

    private fun pegarListaDeDespesas(){
        viewModel.pegarListaDeDespesas()

    }


    private fun putSaldoAtual() {
        viewModel.getSaldoAtual()
    }

    private fun putCurrentMonthInMonthButton() {
        val currentMonth: String = viewModel.getCurrentMonth()
        initCurrentMonthInAlertDialog(currentMonth)
        binding.tvBtnMonth.text = currentMonth
    }

    private fun initCurrentMonthInAlertDialog(currentMonth: String) {
        viewModel.mesSelecionado = currentMonth

    }


    private fun initViewModel() {
        viewModel = ViewModelProvider(requireActivity())[FragmentHomeScreenViewModel::class.java]
        initCollectors()
    }

    private fun initListaDespesaEGanhos() {

//        adapter = AdapterGanhosDespesas(FakeDespesaOuGanho.despesasEGanhos)
//        binding.rvGanhosDespesas.adapter = adapter
    }

    private fun onClickListeners() {
        binding.cvDespesas.setOnClickListener {
            if(view?.findNavController()?.currentDestination?.id == R.id.navigationHome){
                view?.findNavController()?.navigate(FragmentHomeScreenDirections.actionFragmentHomeScreenToFragmentDetalhesDespesas())
            }
        }
        binding.monthButton.setOnClickListener{
            showMonthSelectorDatePicker()
        }

        binding.cvReceitaMensal.setOnClickListener {
            showDialog()
        }
    }

    private fun showDialog() {
        dialogReceitaMensal = Dialog(requireContext())
        dialogReceitaMensal.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialogReceitaMensal.setContentView(R.layout.bottomsheet_receita_mensal)


        dialogReceitaMensal.show()
        dialogReceitaMensal.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialogReceitaMensal.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialogReceitaMensal.window?.attributes?.windowAnimations = R.style.DialogAnimation
        dialogReceitaMensal.window?.setGravity(Gravity.BOTTOM)

        val btnSalvarReceitaMensal = dialogReceitaMensal.findViewById<Button>(R.id.btn_salvar_receita_mensal)
        initBtnSalvarReceitaMensalClick(btnSalvarReceitaMensal, dialogReceitaMensal)
    }

    private fun initBtnSalvarReceitaMensalClick(btnSalvarReceitaMensal: Button?, dialog: Dialog) {
        btnSalvarReceitaMensal?.let{
            it.setOnClickListener {
                viewModel.salvarReceitaMensal(dialog.findViewById<EditText>(R.id.et_receita_mensal).text)

            }
        }
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

    private fun setarOTextoDoBotaoComOMesEscolhido(month: Int) {
        //strings vão hardcoded mesmo porque eu ainda nao vou fazer nenhuma traducao
        when(month){
            0->{binding.tvBtnMonth.text = "Janeiro"}
            1->{binding.tvBtnMonth.text = "Fevereiro"}
            2->{binding.tvBtnMonth.text = "Março"}
            3->{binding.tvBtnMonth.text = "Abril"}
            4->{binding.tvBtnMonth.text = "Maio"}
            5->{binding.tvBtnMonth.text = "Junho"}
            6->{binding.tvBtnMonth.text = "Julho"}
            7->{binding.tvBtnMonth.text = "Agosto"}
            8->{binding.tvBtnMonth.text = "Setembro"}
            9->{binding.tvBtnMonth.text = "Outubro"}
            10->{binding.tvBtnMonth.text = "Novembro"}
            11->{binding.tvBtnMonth.text = "Dezembro"}
        }

    }

    private fun initCollectors() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                sharedViewModel.atualizouListaDeDespesas.collectLatest {

                    pegarListaDeDespesas()
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                sharedViewModel.atualizouListaDeGanhos.collectLatest {

                    pegarListaDeGanhos()
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                sharedViewModel.podeAtualizarSaldoDepoisDeGanhoOuDespesa.collectLatest {

                    atualizarSaldoDepoisDeGanhoOuDespesa()
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.pegouSaldoAtual.collectLatest {
                    saldoAtual->
                    atualizarSaldoAtual(saldoAtual)
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.msgReceitaDigitadaInvalida.collectLatest {
                        msg->
                    mostrarToast(msg)
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.salvouReceitaMensal.collectLatest {
                        salvou->
                    if(salvou){
                        mostrarToast("receita salva!")
                        fecharDialog()
                        atualizarTvReceitaMensal()
                    }else{
                        mostrarToast("ocorreu um erro ao salvar receita")

                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.pegouListaDeDespesas.collectLatest {
                        listaDeDespesas->
                    listaDespesas = listaDeDespesas
                    calcularDespesaTotalAtual(listaDeDespesas)
                    pegarListaDeGanhos()

                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.pegouListaDeGanhos.collectLatest {
                        listaDeGanhos->
                    prepararDadosParaInflarOAdapter(listaDespesas, listaDeGanhos)


                }
            }
        }


        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.pegouReceitaAtualizada.collectLatest {
                    receita->
                    atualizarTextViewReceita(receita)
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.pegouDespesasAtualizadas.collectLatest {
                        despesas->
                    atualizarTextViewDespesas(despesas)
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.atualizouOSaldo.collectLatest {
                        saldoAtualizado->
                    atualizarSaldoAtual(saldoAtualizado)
                }
            }
        }
    }

    private fun atualizarSaldoDepoisDeGanhoOuDespesa() {
        viewModel.atualizarSaldoDepoisDeGanhoOuDespesa()
    }

    private fun calcularDespesaTotalAtual(listaDeDespesas: List<Despesa>?) {
        val despesaAtual = viewModel.calcularDespesaTotalAtual(listaDeDespesas)
        despesaAtual?.let {
            despesaAtual
            atualizarTextViewDespesas(despesaAtual)
        }

    }

    private fun prepararDadosParaInflarOAdapter(listaDespesas: List<Despesa>?, listaDeGanhos: List<Ganho>?) {
        var listaGanhoOuDespesa = viewModel.prepararListasGanhoDespesaParaOAdapter(listaDespesas, listaDeGanhos)
        iniciarListaGanhoOuDespesa(listaGanhoOuDespesa)

    }

    private fun iniciarListaGanhoOuDespesa(listaGanhoOuDespesa: List<GanhoOuDespesa>) {
        adapter = AdapterGanhosDespesas(listaGanhoOuDespesa, this)
        binding.rvGanhosDespesas.adapter = adapter
    }

    private fun pegarListaDeGanhos() {
        viewModel.pegarListaDeGanhos()

    }

    private fun atualizarTextViewDespesas(despesas: Double) {
        val valorFormatado = viewModel.formatarDouble(despesas)
        binding.tvValorDespesas.text = "R$ $valorFormatado"
    }

    private fun atualizarTextViewReceita(receita: Double) {
        val valorFormatado = viewModel.formatarDouble(receita)
        binding.tvValorReceita.text = "R$ $valorFormatado"

    }

    private fun atualizarTvReceitaMensal() {
        viewModel.pegarReceitaMensal()
    }


    private fun atualizarTvDespesas() {
        viewModel.pegarDespesaAtual()
    }

    private fun fecharDialog() {
        dialogReceitaMensal.hide()
    }

    private fun mostrarToast(msg: String) {
        Toast.makeText(requireContext(), msg, Toast.LENGTH_LONG).show()
    }

    private fun atualizarSaldoAtual(saldoAtual: Double) {
        val valorFormatado = viewModel.formatarDouble(saldoAtual)
        binding.tvSaldo.text = "R$ ${valorFormatado}"

    }

    override fun cliquePagamentoEmDinheiro(isPressed: Boolean, despesa: GanhoOuDespesa) {
        if(isPressed){
            //botao cash payment ta pressionado. Enviar a informação de que a
            //forma de pagamento escolhida é dinheiro

            viewModel.atualizarFormaDePagamentoDaDespesa(despesa, "dinheiro")
        }else{
            //botao cash payment não esta pressionado. Enviar a informação de que a
            //forma de pagamento escolhida é ""
            viewModel.atualizarFormaDePagamentoDaDespesa(despesa, "")

        }
        Toast.makeText(requireContext(), "cliquei em pagamento em dinheiro", Toast.LENGTH_SHORT).show()
    }

    override fun cliquePagamentoEmCartao(isPressed: Boolean, despesa: GanhoOuDespesa) {
        if (isPressed){
            viewModel.atualizarFormaDePagamentoDaDespesa(despesa, "cartao")

        }else{
            viewModel.atualizarFormaDePagamentoDaDespesa(despesa, "")

        }
    }


}