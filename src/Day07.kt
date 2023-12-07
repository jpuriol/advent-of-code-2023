fun main() {

    data class Hand(val cards: List<Card>, val bid: Int)

    fun Hand(input: String) : Hand {
        val (cardsStr, bidStr) = input.split(" ")

        return Hand(
            cards = cardsStr.map { Card(it) },
            bid = bidStr.toInt()
        )
    }

    fun part1(input: List<String>): Int {

        val hands = input.map { Hand(it) }

        val winnings = hands.sortedWith(
            compareBy<Hand> {HandType(it.cards).strength}
                .thenBy { it.cards[0].strength }
                .thenBy { it.cards[1].strength }
                .thenBy { it.cards[2].strength }
                .thenBy { it.cards[3].strength }
                .thenBy { it.cards[4].strength }
        ).mapIndexed { index, hand ->
            val rank = index + 1
            rank * hand.bid
        }.sum()


        return winnings
    }


    fun part2(input: List<String>): Int {
        return input.size
    }


    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day07_test")
    check(part1(testInput) == 6440)

    val input = readInput("Day07")
    part1(input).println()
    part2(input).println()
}

enum class Card(val strength: Int) {
    A(13),
    K(12),
    Q(11),
    J(10),
    T(9),
    NINE(8),
    EIGHTH(7),
    SEVEN(6),
    SIX(5),
    FIVE(4),
    FOUR(3),
    THREE(2),
    TWO(1),

}

fun Card(char: Char) : Card {
    return when (char) {
        'A' -> Card.A
        'K' -> Card.K
        'Q' -> Card.Q
        'J' -> Card.J
        'T' -> Card.T
        '9' -> Card.NINE
        '8' -> Card.EIGHTH
        '7' -> Card.SEVEN
        '6' -> Card.SIX
        '5' -> Card.FIVE
        '4' -> Card.FOUR
        '3' -> Card.THREE
        '2' -> Card.TWO
        else -> throw Exception("Invalid card $char")
    }
}

enum class HandType(val strength: Int) {
    FIVE_OF_A_KIND(7),
    FOUR_OF_A_KIND(6),
    FULL_HOUSE(5),
    THREE_OF_A_KIND(4),
    TWO_PAIR(3),
    ONE_PAIR(2),
    HIGH_CARD(1)
}

fun HandType(hand: List<Card>) : HandType {
    val counts = hand.groupingBy { it }.eachCount()

    return when {
        counts.values.contains(5) -> HandType.FIVE_OF_A_KIND
        counts.values.contains(4) -> HandType.FOUR_OF_A_KIND
        counts.values.contains(3) && counts.values.contains(2) -> HandType.FULL_HOUSE
        counts.values.contains(3) -> HandType.THREE_OF_A_KIND
        counts.values.count { it == 2 } == 2 -> HandType.TWO_PAIR
        counts.values.contains(2) -> HandType.ONE_PAIR
        else -> HandType.HIGH_CARD
    }
}
