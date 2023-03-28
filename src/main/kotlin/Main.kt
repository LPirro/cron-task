import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Scanner

val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("H:mm")

fun main(args: Array<String>) {
    val simulatedCurrentTime = parseSimulatedCurrentTime(args[0]) ?: return
    val tasks = parseCronTasks()

    for (task in tasks) {
        val nextRunTime = getNextRuntime(task.hour, task.minute, simulatedCurrentTime)
        val dateText = if (simulatedCurrentTime.dayOfMonth == nextRunTime.dayOfMonth) "today" else "tomorrow"
        val formatted = nextRunTime.format(formatter)
        println("$formatted $dateText - ${task.command}")
    }
}

private fun parseCronTasks(): MutableList<CronTask> {
    val scanner = Scanner(System.`in`)
    val tasks = mutableListOf<CronTask>()
    while (scanner.hasNextLine()) {
        val line = scanner.nextLine()
        val fields = line.split(" ")
        val task = CronTask(minute = fields[0], hour = fields[1], command = fields[2])
        tasks.add(task)
    }
    return tasks
}

private fun parseSimulatedCurrentTime(input: String): LocalDateTime? {
    return try {
        val currentTime = LocalTime.parse(input, formatter)
        return LocalDateTime.now().withHour(currentTime.hour).withMinute(currentTime.minute)
    } catch (e: Exception) {
        println("Invalid Input: The input format should be a time in hours and minutes")
        null
    }
}


private fun getNextRuntime(hour: String, minute: String, simulatedCurrentTime: LocalDateTime): LocalDateTime {
    return when {
        hour == "*" && minute == "*" -> LocalDateTime.now().withHour(simulatedCurrentTime.hour).withMinute(simulatedCurrentTime.minute)
        minute == "*" && hour != "*" -> getWithMinuteWildCard(hour, simulatedCurrentTime)
        hour == "*" && minute != "*" -> getWithHourWildCard(minute, simulatedCurrentTime)
        else -> getWithoutWildcards(hour, minute, simulatedCurrentTime)
    }
}

private fun getWithMinuteWildCard(hour: String, simulatedCurrentTime: LocalDateTime): LocalDateTime {
    return if (simulatedCurrentTime.hour > hour.toInt()) {
        LocalDateTime.now().withHour(hour.toInt()).withMinute(0).plusDays(1)
    } else if (hour.toInt() == simulatedCurrentTime.hour) {
        LocalDateTime.now().withHour(hour.toInt()).withMinute(simulatedCurrentTime.minute).plusMinutes(1)
    } else {
        LocalDateTime.now().withHour(hour.toInt()).withMinute(0)
    }
}

private fun getWithHourWildCard(minute: String, simulatedCurrentTime: LocalDateTime): LocalDateTime {
    return if (minute.toInt() > simulatedCurrentTime.minute) {
        LocalDateTime.now().withHour(simulatedCurrentTime.hour).withMinute(minute.toInt())
    } else {
        LocalDateTime.now().withHour(simulatedCurrentTime.hour).withMinute(minute.toInt()).plusHours(1)
    }
}

private fun getWithoutWildcards(hour: String, minute: String, simulatedCurrentTime: LocalDateTime): LocalDateTime {
    val input = LocalDateTime.now().withHour(hour.toInt()).withMinute(minute.toInt())
    val config = LocalDateTime.now().withHour(simulatedCurrentTime.hour).withMinute(simulatedCurrentTime.minute)
    return if (input.isAfter(config)) {
        LocalDateTime.now().withHour(hour.toInt()).withMinute(minute.toInt())
    } else {
        LocalDateTime.now().withHour(hour.toInt()).withMinute(minute.toInt()).plusDays(1)
    }
}

data class CronTask(val minute: String, val hour: String, val command: String)
