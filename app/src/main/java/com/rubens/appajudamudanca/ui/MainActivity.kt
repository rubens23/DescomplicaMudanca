package com.rubens.appajudamudanca.ui

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.RadioGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.RecyclerView
import com.rubens.appajudamudanca.R
import com.rubens.appajudamudanca.data.models.RVCardViewItem
import com.rubens.appajudamudanca.databinding.ActivityMainBinding
import com.rubens.appajudamudanca.ui.rvadapters.AdapterCardViewItem
import com.rubens.appajudamudanca.ui.rvadapters.interfaces.CliqueItemRVFragmentOrganizacaoMudanca
import com.rubens.appajudamudanca.viewmodels.MainActivityViewModel
import com.rubens.appajudamudanca.viewmodels.SharedViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), CliqueItemRVFragmentOrganizacaoMudanca {

    private lateinit var adapter: AdapterCardViewItem
    private var radioGroup: RadioGroup? = null
    private var btnSalvarDespesaGanho: Button? = null
    private var btnVoltarCategoria: ImageView? = null
    private var custoOuGanhoEditText: EditText? = null
    private var categoriaRecyclerView: RecyclerView? = null
    private var nomeDespesaOuGanhoEditText: EditText? = null
    private lateinit var navController: NavController
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainActivityViewModel
    private lateinit var sharedViewModel: SharedViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        configNavigation()
        initViewModel()
        customizeBottomNavigationView()
        bottomNavClickListeners()
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(this)[MainActivityViewModel::class.java]
        sharedViewModel = ViewModelProvider(this)[SharedViewModel::class.java]
        initCollectors()
    }

    private fun initCollectors() {
        lifecycleScope.launch {
            viewModel.msgErroPreenchimentoDadosDespesaGanho.collectLatest {
                msg->
                showToast(msg)
            }
        }

        lifecycleScope.launch {
            viewModel.salvouNovaDespesa.collectLatest {
            salvou->
                if(salvou){
                    showToast("despesa foi salva com sucesso")
                    sharedViewModel.listaDeDespesasFoiAtualizada()
                    sharedViewModel.atualizarSaldoDepoisDeGanhoOuDespesa()

                }else{
                    showToast("erro ao salvar despesa")
                }
            }
        }

        lifecycleScope.launch {
            viewModel.salvouNovoGanho.collectLatest {
                    salvou->
                if(salvou){
                    showToast("ganho foi salvo com sucesso")
                    sharedViewModel.listaDeGanhosFoiAtualizada()
                    sharedViewModel.atualizarSaldoDepoisDeGanhoOuDespesa()
                }else{
                    showToast("erro ao salvar ganho")
                }
            }
        }


    }



    private fun showToast(msg: String) {
        Toast.makeText(this@MainActivity, msg, Toast.LENGTH_LONG).show()
    }


    private fun bottomNavClickListeners() {
        binding.bottomNavigation.setOnItemSelectedListener {
            item->
            when(item.itemId){
                R.id.addDespesaGasto->{
                    showDialog()

                }
                R.id.navigationHome->{
                    navController.navigate(R.id.navigationHome)
                }
                R.id.navigationOrganizacaoMudanca->{
                    navController.navigate(R.id.navigationOrganizacaoMudanca)


                }
            }

            true
        }
    }

    private fun showDialog() {
        val dialog = Dialog(this)

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.bottomsheetlayout)

        nomeDespesaOuGanhoEditText = dialog.findViewById<EditText>(R.id.et_nome_despesa)
        categoriaRecyclerView = dialog.findViewById<RecyclerView>(R.id.rv_categoria)
        custoOuGanhoEditText = dialog.findViewById<EditText>(R.id.et_custo)
        btnVoltarCategoria = dialog.findViewById<ImageView>(R.id.btn_voltar_categoria_bs)
        btnSalvarDespesaGanho = dialog.findViewById<Button>(R.id.btn_salvar_despesa_ganho)
        radioGroup = dialog.findViewById<RadioGroup>(R.id.radioGroup)



        dialog.show()
        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.attributes?.windowAnimations = R.style.DialogAnimation
        dialog.window?.setGravity(Gravity.BOTTOM)


        initBtnSalvarDespesaGanhoClick(btnSalvarDespesaGanho, dialog)
        initBtnVoltarCategoriaClick(btnVoltarCategoria, dialog)
        initCategoriaRecyclerView(categoriaRecyclerView, dialog)
        initRadioGroupClickListener(radioGroup, dialog)
    }

    private fun initBtnVoltarCategoriaClick(btnVoltarCategoria: ImageView?, dialog: Dialog) {
        btnVoltarCategoria?.setOnClickListener {
            setSwitchMostrarBotaoDeCategoria(false)
            setBackBtnCategoriesVisibility(false)
            mostrarCategoriasIniciais()
        }
    }

    private fun setSwitchMostrarBotaoDeCategoria(b: Boolean) {
        viewModel.switchBotaoVoltarCategorias = b
    }


    private fun initCategoriaRecyclerView(categoriaRecyclerView: RecyclerView?, dialog: Dialog) {

        categoriaRecyclerView?.let {

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
            it.adapter = adapter
        }
    }

    private fun mostrarCategoriasIniciais() {
        categoriaRecyclerView?.let {

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
            it.adapter = adapter
        }
    }

    private fun initRadioGroupClickListener(radioGroup: RadioGroup?, dialog: Dialog) {


        radioGroup?.let {
            it.setOnCheckedChangeListener { radioGroup, checkedId ->
                when(checkedId){
                    R.id.radio_btn_despesas->{
                        nomeDespesaOuGanhoEditText?.hint = "nome despesa"
                        custoOuGanhoEditText?.hint = "custo"
                        btnSalvarDespesaGanho?.text = "salvar despesa"
                        categoriaRecyclerView?.visibility = View.VISIBLE
                    }
                    R.id.radio_btn_ganhos->{
                        nomeDespesaOuGanhoEditText?.hint = "nome ganho"
                        categoriaRecyclerView?.visibility = View.GONE
                        custoOuGanhoEditText?.hint = "valor ganho"
                        btnSalvarDespesaGanho?.text = "salvar ganho"
                    }
                }
            }
        }

    }

    private fun initBtnSalvarDespesaGanhoClick(btnSalvarDespesa: Button?, dialog: Dialog) {
        btnSalvarDespesa?.let {
            it.setOnClickListener {
                if (btnSalvarDespesa.text == "salvar despesa"){
                    //salvar despesa
                    viewModel.salvarNovaDespesa(nomeDespesaOuGanhoEditText, custoOuGanhoEditText)

                }else{
                    //salvar ganho
                    viewModel.salvarNovoGanho(nomeDespesaOuGanhoEditText, custoOuGanhoEditText)

                }
            }
        }
    }


    private fun configNavigation() {
        val navHostFragment = (supportFragmentManager.findFragmentById(binding.fragmentContainerView.id)) as NavHostFragment
        navController = navHostFragment.navController


        binding.bottomNavigation.setupWithNavController(navController)

    }

    private fun customizeBottomNavigationView() {


        binding.bottomNavigation.setBackgroundResource(R.drawable.bottom_navigation_background)

//        val bottomMenuView = binding.bottomNavigation.getChildAt(0) as BottomNavigationMenuView
//        val view = bottomMenuView.getChildAt(1)
//        val itemView = view as BottomNavigationItemView

        binding.bottomNavigation.itemIconTintList = null

        //val viewCustom = LayoutInflater.from(this).inflate(R.layout.nav_custom_btn, bottomMenuView, false)
        //itemView.addView(viewCustom)



    }

    override fun clique(destino: String) {
        //o destino é na vdd o nome da categoria
        viewModel.categoriaEscolhida = destino
        Toast.makeText(this@MainActivity, "categoria selecionada: $destino", Toast.LENGTH_SHORT).show()
        abrirSubCategoria(destino)
        viewModel.switchBotaoVoltarCategorias = true

        setBackBtnCategoriesVisibility(viewModel.switchBotaoVoltarCategorias)


    }

    private fun setBackBtnCategoriesVisibility(podeMostrar: Boolean) {
        if(podeMostrar){
            btnVoltarCategoria?.visibility = View.VISIBLE

        }else{
            btnVoltarCategoria?.visibility = View.GONE

        }
    }

    private fun abrirSubCategoria(nomeCategoria: String) {
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
        categoriaRecyclerView?.adapter = adapter

    }
}