fun main() {

    fun parse(input: List<String>): Pair<List<Instruction>, Map<String, Node>> {
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

        return Pair(instructions, nodes)
    }

    fun part1(input: List<String>): Int {
        val (instructions, nodes) = parse(input)

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


    fun part2(input: List<String>): Long {

        val (instructions, nodes) = parse(input)

        val startNodes = nodes.filter { it.key.endsWith('A') }.map { it.value }

        val stepsToFinalNode = startNodes.associateWith { node ->
            var currentNode = node

            var currentInstruction = 0
            var steps = 0L

            while (!currentNode.name.endsWith('Z')) {
                val instruction = instructions[currentInstruction]

                currentNode = when (instruction) {
                    Instruction.LEFT -> nodes[currentNode.left]
                        ?: error("No node with name '${currentNode.left}' found")

                    Instruction.RIGHT -> nodes[currentNode.right]
                        ?: error("No node with name '${currentNode.right}' found")
                }

                currentInstruction = (currentInstruction + 1) % instructions.size
                steps++
            }

            steps
        }

        stepsToFinalNode.forEach {
            println("${it.key.name} -> ${it.value}")
        }

        return findLCM(stepsToFinalNode.values.toList())
    }


    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day08_test")
    check(part2(testInput) == 6L)

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

fun findLCM(numbers: List<Long>): Long {
    // Function to find the greatest common divisor (GCD)
    fun findGCD(a: Long, b: Long): Long = if (b == 0L) a else findGCD(b, a % b)

    // Function to find the least common multiple (LCM) of two numbers
    fun findLCMOfTwo(a: Long, b: Long): Long = if (a == 0L || b == 0L) 0L else (a * b) / findGCD(a, b)

    // Function to find the LCM of a list of numbers
    fun findLCMOfList(numbers: List<Long>): Long {
        if (numbers.isEmpty()) return 0L
        var lcm = numbers[0]

        for (i in 1 until numbers.size) {
            lcm = findLCMOfTwo(lcm, numbers[i])
        }

        return lcm
    }

    return findLCMOfList(numbers)
}