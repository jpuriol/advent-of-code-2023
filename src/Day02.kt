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
        var res = 0

        for (game in input) {

            val id = extractGameNumber(game) ?: error("unable to extract game number: $game")

            var maxRed = 0
            var maxGreen = 0
            var maxBlue = 0

            val draws = game.removePrefix("Game $id:").split(";")
            for (draw in draws) {
                val balls = draw.split(',')
                val colors = balls.map { ball ->
                    val n = extractNumber(ball) ?: error("unable to extract ball number: $ball")
                    val color = ball.removePrefix(" $n ")
                    Pair(n, color)
                }

                for ((n, color) in colors) {
                    when (color) {
                        "red" -> if (n > maxRed) maxRed = n
                        "green" -> if (n > maxGreen) maxGreen = n
                        "blue" -> if (n > maxBlue) maxBlue = n
                        else -> error("unknown color: $color")
                    }
                }
            }

            res += (maxRed * maxGreen * maxBlue)
        }



        return res
    }


    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day02_test")
    check(part2(testInput) == 2286)

    val input = readInput("Day02")
    part1(input).println()
    part2(input).println()
}
