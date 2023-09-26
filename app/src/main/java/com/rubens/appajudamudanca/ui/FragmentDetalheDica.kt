package com.rubens.appajudamudanca.ui

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.rubens.appajudamudanca.R
import com.rubens.appajudamudanca.data.models.Dica
import com.rubens.appajudamudanca.databinding.FragmentFragmentDetalheDicaBinding
import com.rubens.appajudamudanca.viewmodels.FragmentDetalheDicaViewModel


class FragmentDetalheDica : Fragment() {


    private lateinit var binding: FragmentFragmentDetalheDicaBinding
    private lateinit var viewModel: FragmentDetalheDicaViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentFragmentDetalheDicaBinding.inflate(inflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViewModel()
        changeStatusBar()
        pegarDica()
    }

    private fun pegarDica() {
        val args: FragmentDetalheDicaArgs by navArgs()
        val dica: Dica = args.dica
        setarViewsDaDica(dica)
    }

    private fun setarViewsDaDica(dica: Dica) {
        binding.tvTitleDica.text = dica.titulo_dica
        Glide.with(requireContext()).load(dica.foto_dica).into(binding.ivImagemDica)
        binding.tvTextoDica.text = dica.texto_dica

    }


    private fun initViewModel() {
        viewModel = ViewModelProvider(requireActivity())[FragmentDetalheDicaViewModel::class.java]

    }

    override fun onDestroyView() {
        super.onDestroyView()

        returnStatusBarToNormal()



    }

    private fun changeStatusBar() {
        // Tornar a barra de status transparente
        val window = requireActivity().window
        window.statusBarColor = Color.TRANSPARENT
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN


        // Ajustar a ImageView para ocupar o espaço até o topo da tela
        val layoutParams = binding.ivImagemDica.layoutParams as ViewGroup.MarginLayoutParams

        // Definir a margem superior para 0
        layoutParams.topMargin = 0

        // Aplicar os parâmetros de layout atualizados
        binding.ivImagemDica.layoutParams = layoutParams
    }

    private fun returnStatusBarToNormal() {
            val window = requireActivity().window
            window.statusBarColor = ContextCompat.getColor(requireContext(), R.color.light_gray)
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
    }


}