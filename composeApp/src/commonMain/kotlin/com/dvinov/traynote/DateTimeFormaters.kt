package com.dvinov.traynote

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.Month
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime


fun Month.mothFormat(): String {
    return when (this.ordinal) {
        0 -> "янв"
        1 -> "фев"
        2 -> "мар"
        3 -> "апр"
        4 -> "мая"
        5 -> "июн"
        6 -> "июл"
        7 -> "авг"
        8 -> "сен"
        9 -> "окт"
        10 -> "ноя"
        11 -> "дек"
        else -> ""
    }
}

//"d MMMM yyyy"
fun LocalDateTime?.dateFormat(): String {
    if (this == null) {
        return ""
    }
    val timeZonedDate = this.toInstant(TimeZone.UTC).toLocalDateTime(TimeZone.currentSystemDefault())
    val day = if (timeZonedDate.dayOfMonth < 10) "0${timeZonedDate.dayOfMonth}" else "${timeZonedDate.dayOfMonth}"
    val month = timeZonedDate.month.mothFormat()
    val year = timeZonedDate.year.toString()
    return "$day $month $year"
}

//"d MMMM yyyy"
fun LocalDateTime?.dateFormatWithEnter(): String {
    if (this == null) {
        return ""
    }
    val timeZonedDate = this.toInstant(TimeZone.UTC).toLocalDateTime(TimeZone.currentSystemDefault())
    val day = if (timeZonedDate.dayOfMonth < 10) "0${timeZonedDate.dayOfMonth}" else "${timeZonedDate.dayOfMonth}"
    val month = timeZonedDate.month.mothFormat()
    val year = timeZonedDate.year.toString()
    return "$day $month\n$year"
}


//"HH:mm"
fun LocalDateTime?.timeFormat(): String {
    if (this == null) return ""
    val timeZonedDate = this.toInstant(TimeZone.UTC).toLocalDateTime(TimeZone.currentSystemDefault())
    val hour = if (timeZonedDate.hour < 10) "0${timeZonedDate.hour}" else "${timeZonedDate.hour}"
    val minute = if (timeZonedDate.minute < 10) "0${timeZonedDate.minute}" else "${timeZonedDate.minute}"
    return "$hour:$minute"
}

//"d MMM yyy в HH:mm"
fun LocalDateTime?.dateTimeFormat(): String {
    if (this == null) {
        return ""
    }
    val timeZonedDate = this.toInstant(TimeZone.UTC).toLocalDateTime(TimeZone.currentSystemDefault())
    val day = if (timeZonedDate.dayOfMonth < 10) "0${timeZonedDate.dayOfMonth}" else "${timeZonedDate.dayOfMonth}"
    val month = timeZonedDate.month.mothFormat()
    val year = timeZonedDate.year.toString()
    val hour = if (timeZonedDate.hour < 10) "0${timeZonedDate.hour}" else "${timeZonedDate.hour}"
    val minute = if (timeZonedDate.minute < 10) "0${timeZonedDate.minute}" else "${timeZonedDate.minute}"

    return "$day $month $year в $hour:$minute"
}