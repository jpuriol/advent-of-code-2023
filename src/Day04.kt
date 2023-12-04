fun main() {

    data class Card(val id: Int, val winning: List<Int>, val actual: List<Int>)

    fun parseCards(input: List<String>): List<Card> {
        val regex = Regex("""Card *(\d+):""")

        return input.map { line ->
            val match = regex.find(line) ?: error("unable to extract id from: $input")
            val id = match.groupValues[1].toInt()
            val (winning, actual) = line.removePrefix("Card $id:").split('|')
            Card(
                id,
                winning.split(' ').mapNotNull { it.toIntOrNull() },
                actual.split(' ').mapNotNull { it.toIntOrNull() },
            )
        }
    }


    fun part1(input: List<String>): Int {

        val cards = parseCards(input)

        return cards.sumOf { c ->
            var points = 0

            for (n in c.actual) {
                if (n in c.winning) {
                    points = if (points == 0) 1 else points * 2
                }
            }

            points
        }
    }

    fun part2(input: List<String>): Int {
        return input.size
    }


    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day04_test")
    check(part1(testInput) == 13)

    val input = readInput("Day04")
    part1(input).println()
    // part2(input).println()
}
