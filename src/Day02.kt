fun main() {

    fun extractGameNumber(input: String): Int? {
        val regex = Regex("""Game (\d+)""")
        val matchResult = regex.find(input)

        return matchResult?.groupValues?.get(1)?.toIntOrNull()
    }

    fun extractNumber(input: String): Int? {
        val regex = Regex("""\d+""")
        val matchResult = regex.find(input)

        return matchResult?.value?.toIntOrNull()
    }

    fun part1(input: List<String>): Int {

        var res = 0
        for (game in input) {

            val id = extractGameNumber(game) ?: continue

            var validID = true

            val draws = game.removePrefix("Game $id:")
            for (draw in draws.split(';')) {
                val balls = draw.split(',')
                for (ball in balls) {
                    val n = extractNumber(ball) ?: error("unable to extract ball number: $ball")
                    val color = ball.removePrefix(" $n ")
                    val maxColor = when (color) {
                        "red" -> 12
                        "green" -> 13
                        "blue" -> 14
                        else -> error("unknown color: $color")
                    }
                    if (n > maxColor) {
                        validID = false
                        break
                    }
                }

                if (!validID) break

            }

            if (validID) res += id
        }

        return res
    }

    fun part2(input: List<String>): Int {
        return input.size
    }


    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day02_test")
    check(part1(testInput) == 8)

    val input = readInput("Day02")
    part1(input).println()
    // part2(input).println()
}
