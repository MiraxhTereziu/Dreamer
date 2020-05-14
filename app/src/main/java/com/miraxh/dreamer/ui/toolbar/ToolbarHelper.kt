package com.miraxh.dreamer.ui.toolbar

import androidx.lifecycle.MutableLiveData
import com.miraxh.dreamer.data.Day
import com.miraxh.dreamer.data.dream.Dream
import com.miraxh.dreamer.util.HOWMANYDAYS
import java.text.SimpleDateFormat
import java.util.*

class ToolbarHelper {

    var days = MutableLiveData<MutableList<Day>>()

    fun getDaysData(dreamData: MutableLiveData<List<Dream>>) {
        val tmpList = mutableListOf<Day>()

        val dayNumberFormatter = SimpleDateFormat("dd", Locale.US)
        val dayOfWeekFormatter = SimpleDateFormat("E", Locale.US)

        for (i in 0..HOWMANYDAYS) {
            val calendar = Calendar.getInstance()
            calendar.add(Calendar.DAY_OF_MONTH, -i)


            val dayNumber = dayNumberFormatter.format(calendar.time)
            val dayOfWeek = dayOfWeekFormatter.format(calendar.time)

            val date =
                "${calendar.get(Calendar.DAY_OF_MONTH)}/${calendar.get(Calendar.MONTH) + 1}/${calendar.get(
                    Calendar.YEAR
                )}"
            tmpList.add(
                Day(
                    date,
                    dayNumber.toInt(),
                    dayOfWeek,
                    checkDay(date, dreamData) //randomActiveTMP()
                )
            )
        }
        days.value = tmpList
    }

    private fun checkDay(date: String, dreamData: MutableLiveData<List<Dream>>): Boolean {
        var toRtn = false
        dreamData.value?.forEach {
            if (date == it.date)
                toRtn = true
        }
        return toRtn
    }

    private fun setState(date: String, state: Boolean) {
        days.value?.forEach {
            if (it.date == date) {
                it.active = state
            }
        }
    }
}