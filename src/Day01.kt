fun main() {
    fun part1(input: List<String>): Int {
        var res = 0

        for (line in input) {
            val numbers = line.mapNotNull { it.digitToIntOrNull() }
            if (numbers.isEmpty()) continue

            val calibration = numbers.first()*10  + numbers.last()
            res += calibration
        }

        return res
    }


    val numbersMap = mapOf(
        "one" to "o1e",
        "two" to "t2o",
        "three" to "t3e",
        "four" to "f4r",
        "five" to "f5e",
        "six" to "s6x",
        "seven" to "s7n",
        "eight" to "e8t",
        "nine" to "n9e"
    )

    fun part2(input: List<String>): Int {
        var res = 0

        for (line in input) {

            val toReplace = numbersMap
                .keys.filter { line.contains(it) }
                .sortedBy { line.indexOf(it) }

            var lineCleaned = line
            for (key in toReplace) {
                lineCleaned = lineCleaned.replace(key, numbersMap[key].toString())
            }

            val numbers = lineCleaned.mapNotNull { it.digitToIntOrNull() }
            if (numbers.isEmpty()) continue

            val calibration = numbers.first()*10  + numbers.last()

            res += calibration
        }

        return res
    }


    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01_test")
    check(part2(testInput) == 281)

    val input = readInput("Day01")
    part1(input).println()
    part2(input).println()
}
