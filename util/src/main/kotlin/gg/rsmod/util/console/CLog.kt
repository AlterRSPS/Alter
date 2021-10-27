package gg.rsmod.util.console

import java.util.*

object CLog {

    fun log(name:String, vararg data: Any) {
        println("[$name]:[${getTime()}] => ${getColor(Colors.GREEN_BRIGHT)} $data ${Colors.RESET} ")
    }

    private fun getColor(color: Colors): String {
        return color.toString()
    }

    private fun getTime(): String {
        val date: Date = Date()
        var cal = Calendar.getInstance()
        cal.time = date
        val day = cal.get(Calendar.DAY_OF_MONTH)
        val month = cal.get(Calendar.MONTH)
        val year = cal.get(Calendar.YEAR)

        val hours = cal.get(Calendar.HOUR_OF_DAY)
        val minutes = cal.get(Calendar.MINUTE)
        val seconds = cal.get(Calendar.SECOND)
        return "$day-$month-$year - $hours:$minutes:$seconds"
    }
}