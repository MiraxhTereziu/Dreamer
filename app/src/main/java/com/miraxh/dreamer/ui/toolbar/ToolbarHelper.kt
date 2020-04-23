package com.miraxh.dreamer.ui.toolbar

import androidx.lifecycle.MutableLiveData
import com.miraxh.dreamer.data.Day
import com.miraxh.dreamer.util.HOWMANYDAYS
import java.text.SimpleDateFormat
import java.util.*

class ToolbarHelper {

    var days = MutableLiveData<MutableList<Day>>()

    fun getDaysData() {

        var tmpList = mutableListOf<Day>()

        val dayNumberFormatter = SimpleDateFormat("dd")
        val dayOfWeekFormatter = SimpleDateFormat("E")

        for (i in 0..HOWMANYDAYS) {
            var calendar = Calendar.getInstance()
            calendar.add(Calendar.DAY_OF_MONTH, -i)

            var dayNumber = dayNumberFormatter.format(calendar.time)
            var dayOfWeek = dayOfWeekFormatter.format(calendar.time)

            tmpList.add(
                Day(
                    dayNumber.toInt(),
                    getDayOfTheWeekShort(dayOfWeek),
                   false //randomActiveTMP()
                )
            )
        }
        days.value = tmpList
    }

    fun getDay(day: Day){
        var tmpList = days.value
        tmpList?.forEach {
            if(day.day == it.day){
                it.active == it.active.not()
            }
        }
        days.value = tmpList
    }

    fun getDayOfTheWeekShort(dayOfWeek: String): String {
        var dayOfWeekShort = when (dayOfWeek) {
            "Mon" -> "Mo"
            "Tue" -> "Tu"
            "Wed" -> "We"
            "Thu" -> "Th"
            "Fri" -> "Fr"
            "Sat" -> "Sa"
            "Sun" -> "Su"
            else -> "ff"
        }
        return dayOfWeekShort
    }

    fun randomActiveTMP(): Boolean {
        val random = Random()
        val value = random.nextInt(100)
        val toRtn = value > 50
        return toRtn
    }

}