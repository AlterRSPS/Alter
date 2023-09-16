import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class Logger(val name: String = "") {
    fun info(input: Any) {
        log(LogLevel.INFO, input)
    }

    fun error(input: Any) {
        log(LogLevel.ERROR, input)
    }

    fun debug(input: Any) {
        log(LogLevel.DEBUG, input)
    }

    fun warning(input: Any) {
        log(LogLevel.WARNING, input)
    }

    private fun log(level: LogLevel, input: Any) {
        val insert = if (name.isEmpty()) "" else "[$name]"
        val time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"))
        println("[$level][$time]$insert: $input")
    }

    enum class LogLevel {
        INFO,
        ERROR,
        DEBUG,
        WARNING
    }
}
