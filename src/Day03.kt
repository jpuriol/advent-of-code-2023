import java.lang.Character.isDigit

fun main() {

    data class Number(val value: Int, val line: Int, val startIndex: Int, val endIndex: Int)
    data class Symbol(val value: Char, val line: Int, val index: Int)

    fun extractNumbers(input: List<String>): List<Number> {
        val regex = Regex("""(\d+)*""")

        return input.flatMapIndexed { lineNum, line ->
            regex.findAll(line).mapNotNull { matchResult ->
                val n = matchResult.value.toIntOrNull() ?: return@mapNotNull null

                Number(n, lineNum, matchResult.range.first, matchResult.range.last)
            }.toList()
        }
    }

    fun extractSymbols(input: List<String>): List<Symbol> {

        return input.flatMapIndexed() { lineNum, line ->
            line.mapIndexedNotNull { index, c ->
                when {
                    c == '.' -> null
                    isDigit(c) -> null
                    else -> Symbol(c, lineNum, index)
                }
            }
        }
    }

    fun part1(input: List<String>): Int {
        val numbers = extractNumbers(input)
        val symbols = extractSymbols(input)

        val validNumbers = numbers.mapNotNull { n ->

            val sameLine = symbols.find { s ->
                s.line == n.line && (s.index == n.startIndex - 1 || s.index == n.endIndex + 1)
            }
            val lineAbove = symbols.find { s ->
                s.line - 1 == n.line && s.index in n.startIndex - 1..n.endIndex + 1
            }
            val lineBelow = symbols.find { s ->
                s.line + 1 == n.line && (s.index in n.startIndex - 1..n.endIndex + 1)
            }

            when {
                sameLine != null -> n
                lineAbove != null -> n
                lineBelow != null -> n
                else -> null
            }
        }


        return validNumbers.sumOf { it.value }
    }

    fun part2(input: List<String>): Int {
        return input.size
    }


    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day03_test")
    check(part1(testInput) == 4361)

    val input = readInput("Day03")
    part1(input).println()
    // part2(input).println()
}
