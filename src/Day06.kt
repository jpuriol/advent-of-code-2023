fun main() {


    fun part1(input: List<String>): Int {
        return input.size
    }


    fun part2(input: List<String>): Int {
        return input.size
    }


    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day06_test")
    check(part2(testInput) == 288)

    val input = readInput("Day06")
    part1(input).println()
    part2(input).println()
}
