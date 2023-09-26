package com.rubens.appajudamudanca.ui.utils

import java.math.BigDecimal
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.NumberFormat

object ConversorValoresDouble {

    fun formatarDouble(valor: Double): String {
        if(valor.toString() == "0.0"){
            return "0"
        }
        val valorConvertido = valor.toBigDecimal()
        val valorFormatado = valorConvertido.setScale(2, RoundingMode.DOWN)
        return if (valorFormatado.stripTrailingZeros().scale() == 0) {
            valorFormatado.toBigInteger().toString()
        } else {
            valorFormatado.stripTrailingZeros().toPlainString()
        }
    }

    fun formatarDouble2(valor: Double): String {
        if (valor == 0.0) {
            return "0"
        }

        val valorFormatado = String.format("%.2f", valor)

        return if (valorFormatado.endsWith(".00")) {
            valorFormatado.substring(0, valorFormatado.length - 3) // Remove os dois últimos caracteres (.00)
        } else {
            valorFormatado
        }
    }
}