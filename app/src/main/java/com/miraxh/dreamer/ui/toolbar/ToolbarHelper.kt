package com.miraxh.dreamer.ui.toolbar

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.miraxh.dreamer.data.Day
import com.miraxh.dreamer.data.dream.Dream
import com.miraxh.dreamer.util.HOWMANYDAYS
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class ToolbarHelper {

    var days = MutableLiveData<MutableList<Day>>()

    fun getDaysData(dreamData: MutableLiveData<List<Dream>>) {
        var tmpList = mutableListOf<Day>()

        val dayNumberFormatter = SimpleDateFormat("dd")
        val dayOfWeekFormatter = SimpleDateFormat("E")

        for (i in 0..HOWMANYDAYS) {
            var calendar = Calendar.getInstance()
            calendar.add(Calendar.DAY_OF_MONTH, -i)


            var dayNumber = dayNumberFormatter.format(calendar.time)
            var dayOfWeek = dayOfWeekFormatter.format(calendar.time)

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


    fun getDay(day: Day) {
        var tmpList = days.value
        tmpList?.forEach {
            if (day.day == it.day) {
                it.active == it.active.not()
            }
        }
        days.value = tmpList
    }

    fun setActiveDays(dreamData: MutableLiveData<List<Dream>>) {
        val calendar = Calendar.getInstance()
        val dateFormatter = SimpleDateFormat("dd/MM/yyyy")
        val todayStr =
            "${calendar.get(Calendar.DAY_OF_MONTH)}/${calendar.get(Calendar.MONTH) + 1}/${calendar.get(
                Calendar.YEAR
            )}"
        val today = dateFormatter.parse(todayStr)

        dreamData.value?.forEach {
            val tomorrowStr = it.date
            val tomorrow = dateFormatter.parse(tomorrowStr)
            val difference = (today.time - tomorrow.time) / (1000 * 60 * 60 * 24)
            if (difference < HOWMANYDAYS)
                setState(tomorrowStr, true)
            else
                setState(tomorrowStr, false)
        }
    }

    fun checkSingleDay(tomorrowStr: String): Boolean {
        var toRtn = false
        val calendar = Calendar.getInstance()
        val dateFormatter = SimpleDateFormat("dd/MM/yyyy")
        val todayStr =
            "${calendar.get(Calendar.DAY_OF_MONTH)}/${calendar.get(Calendar.MONTH) + 1}/${calendar.get(
                Calendar.YEAR
            )}"
        val today = dateFormatter.parse(todayStr)
        val tomorrow = dateFormatter.parse(tomorrowStr)
        val difference = (today.time - tomorrow.time) / (1000 * 60 * 60 * 24)
        toRtn = difference < HOWMANYDAYS
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