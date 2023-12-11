fun main() {

    fun List<Long>.allEqual(): Boolean {
        val n = this[0]

        for (i in 1 until size) {
            if (this[i] != n)
                return false
        }

        return true
    }

    fun nextPrediction(history: List<Long>): Long {
        if (history.allEqual())
            return history.last()

        val next = nextPrediction(history.zipWithNext { current, next -> next - current })
        return history.last() + next
    }


    fun part1(input: List<String>): Long {
        val histories = input.map { line ->
            line.split(' ').map { it.toLong() }
        }.asSequence()

        val predictions = histories.map { history ->
            nextPrediction(history)
        }


        return predictions.sum()
    }


    fun previousPrediction(history: List<Long>): Long {
        if (history.allEqual())
            return history.first()

        val previous = previousPrediction(history.zipWithNext { current, next -> next - current })
        return history.first() - previous
    }

    fun part2(input: List<String>): Long {
        val histories = input.map { line ->
            line.split(' ').map { it.toLong() }
        }.asSequence()

        val predictions = histories.map { history ->
            previousPrediction(history)
        }

        return predictions.sum()
    }


    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day09_test")
    check(part2(testInput) == 2L)

    val input = readInput("Day09")
    part1(input).println()
    part2(input).println()
}
