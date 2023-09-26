package com.rubens.appajudamudanca.data.models

import java.io.Serializable

data class Dica(
    val foto_dica: String,
    val titulo_dica: String,
    val texto_dica: String
): Serializable
