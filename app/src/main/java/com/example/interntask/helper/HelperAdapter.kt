package com.example.interntask.helper
import java.text.NumberFormat
import java.util.Locale


        fun Int.toINR(): String {
            val format = NumberFormat.getCurrencyInstance(Locale("en", "IN"))
            return format.format(this)
        }

        fun Double.toIntValue(): Int {
            return this.toInt()
        }

