package com.dzl.gymloggingapp.utils

fun Float?.smartFormat(): String {
    return when {
        this == null -> ""
        this % 1.0 == 0.0 -> this.toInt().toString()
        else -> String.format("%.1f", this)
    }
}