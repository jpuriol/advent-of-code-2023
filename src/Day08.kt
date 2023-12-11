fun main() {

    fun part1(input: List<String>): Int {
        val instructions = input[0].map { c ->
            when (c) {
                'L' -> Instruction.LEFT
                'R' -> Instruction.RIGHT
                else -> error("Unknown instruction: $c")
            }
        }

        val nodes = input.slice(2..<input.size).map { line ->
            val (name, left, right) = line.split("=", ",")
            Node(
                name = name.trim(),
                left = left.trim().removePrefix("("),
                right = right.trim().removeSuffix(")")
            )
        }.associateBy { it.name }

        var currentNode = nodes["AAA"] ?: error("No node with name 'AAA' found")
        var currentInstruction = 0

        var steps = 0

        while (currentNode.name != "ZZZ") {
            val instruction = instructions[currentInstruction]

            currentNode = when (instruction) {
                Instruction.LEFT -> nodes[currentNode.left] ?: error("No node with name '${currentNode.left}' found")
                Instruction.RIGHT -> nodes[currentNode.right] ?: error("No node with name '${currentNode.right}' found")
            }

            currentInstruction = (currentInstruction + 1) % instructions.size
            steps++
        }

        return steps
    }


    fun part2(input: List<String>): Int {
        return input.size
    }


    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day08_test")
    check(part1(testInput) == 6)

    val input = readInput("Day08")
    part1(input).println()
    part2(input).println()
}


enum class Instruction {
    LEFT, RIGHT
}

data class Node(
    val name: String,
    val left: String,
    val right: String
)