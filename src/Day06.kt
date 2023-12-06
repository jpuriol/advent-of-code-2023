fun main() {

    data class Race(
        val time: Int,
        val record: Int,
    )

    fun part1(input: List<String>): Int {
        val times = input[0].split(' ').mapNotNull { it.toIntOrNull() }
        val records = input[1].split(' ').mapNotNull { it.toIntOrNull() }

        val races = times.indices.map { i ->
            Race(times[i], records[i])
        }

        val waysToWin = races.map { race ->
            val holdTimes = (1..<race.time).mapNotNull { timePressing ->
                val remainingTime = race.time - timePressing
                val distance = timePressing * remainingTime
                if (distance > race.record) timePressing else null
            }

            holdTimes.size
        }

        return waysToWin.reduce(Int::times)
    }


    fun part2(input: List<String>): Long {
        val raceTime = input[0].replace(" ", "").split(':')[1].toLong()
        val raceRecord = input[1].replace(" ", "").split(':')[1].toLong()

        val waysToWin = (1..<raceTime).asSequence().map { timePressing ->
            val remainingTime = raceTime - timePressing
            val distance = timePressing * remainingTime
            if (distance > raceRecord) 1L else 0
        }.sum()

        return waysToWin
    }


    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day06_test")
    check(part2(testInput) == 71503L)

    val input = readInput("Day06")
    part1(input).println()
    part2(input).println()
}
