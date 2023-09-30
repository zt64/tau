package zt.tau.util

import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

val formatter: DateTimeFormatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.LONG)!!
fun String.contains(vararg strings: String) = strings.any { this in it }

fun Instant.humanFriendly(): String {
    val zdt = ZonedDateTime.ofInstant(this, ZoneId.systemDefault())
    return formatter.format(zdt)
}