package com.example.whatdidyoudo.utils

import android.icu.util.Calendar
import java.util.*

private const val ZERO = 0
private const val MILLIS_IN_DAY = 24 * 60 * 60 * 1000 - 1

fun getStartDateMillis(date: Date): Date = Calendar.getInstance().apply {
    time = date
    set(Calendar.MILLISECONDS_IN_DAY, ZERO)
}.time

fun getEndDateMillis(date: Date): Date = Calendar.getInstance().apply {
    time = date
    set(Calendar.MILLISECONDS_IN_DAY, MILLIS_IN_DAY)
}.time