fun main() {

    data class Hand(val cards: List<Card>, val bid: Int)

    fun Hand(input: String): Hand {
        val (cardsStr, bidStr) = input.split(" ")

        return Hand(
            cards = cardsStr.map { Card(it) },
            bid = bidStr.toInt()
        )
    }

    fun part1(input: List<String>): Int {

        val hands = input.map { Hand(it) }

        val winnings = hands.sortedWith(
            compareBy<Hand> { HandType(it.cards).strength }
                .thenBy { it.cards[0].strengthP1 }
                .thenBy { it.cards[1].strengthP1 }
                .thenBy { it.cards[2].strengthP1 }
                .thenBy { it.cards[3].strengthP1 }
                .thenBy { it.cards[4].strengthP1 }
        ).mapIndexed { index, hand ->
            val rank = index + 1
            rank * hand.bid
        }.sum()


        return winnings
    }


    fun part2(input: List<String>): Int {

        val hands = input.map { Hand(it) }

        val winnings = hands.sortedWith(
            compareBy<Hand> { HandType(it.cards, Part.TWO).strength }
                .thenBy { it.cards[0].strengthP2 }
                .thenBy { it.cards[1].strengthP2 }
                .thenBy { it.cards[2].strengthP2 }
                .thenBy { it.cards[3].strengthP2 }
                .thenBy { it.cards[4].strengthP2 }
        ).mapIndexed { index, hand ->
            val rank = index + 1
            rank * hand.bid
        }.sum()


        return winnings
    }


    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day07_test")
    check(part2(testInput) == 5905)

    val input = readInput("Day07")
    part1(input).println()
    part2(input).println()
}

enum class Card(val strengthP1: Int, val strengthP2: Int) {
    A(strengthP1 = 13, strengthP2 = 13),
    K(strengthP1 = 12, strengthP2 = 12),
    Q(strengthP1 = 11, strengthP2 = 11),
    J(strengthP1 = 10, strengthP2 = -1), // The joker on part2 has the lowest strength
    T(strengthP1 = 9, strengthP2 = 9),
    NINE(strengthP1 = 8, strengthP2 = 8),
    EIGHTH(strengthP1 = 7, strengthP2 = 7),
    SEVEN(strengthP1 = 6, strengthP2 = 6),
    SIX(strengthP1 = 5, strengthP2 = 5),
    FIVE(strengthP1 = 4, strengthP2 = 4),
    FOUR(strengthP1 = 3, strengthP2 = 3),
    THREE(strengthP1 = 2, strengthP2 = 2),
    TWO(strengthP1 = 1, strengthP2 = 1),

}

fun Card(char: Char): Card {
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

enum class Part {
    ONE,
    TWO
}

fun HandType(hand: List<Card>, part: Part = Part.ONE): HandType {
    val counts = hand.groupingBy { it }.eachCount()

    return when (part) {
        Part.ONE -> {
            when {
                counts.values.contains(5) -> HandType.FIVE_OF_A_KIND
                counts.values.contains(4) -> HandType.FOUR_OF_A_KIND
                counts.values.contains(3) && counts.values.contains(2) -> HandType.FULL_HOUSE
                counts.values.contains(3) -> HandType.THREE_OF_A_KIND
                counts.values.count { it == 2 } == 2 -> HandType.TWO_PAIR
                counts.values.contains(2) -> HandType.ONE_PAIR
                else -> HandType.HIGH_CARD
            }
        }

        Part.TWO -> {
            val jokers = counts[Card.J] ?: 0
            val newCounts = counts.filter { it.key != Card.J }

            val possibleType = when {
                newCounts.values.contains(5) -> HandType.FIVE_OF_A_KIND
                newCounts.values.contains(4) -> HandType.FOUR_OF_A_KIND
                newCounts.values.contains(3) && newCounts.values.contains(2) -> HandType.FULL_HOUSE
                newCounts.values.contains(3) -> HandType.THREE_OF_A_KIND
                newCounts.values.count { it == 2 } == 2 -> HandType.TWO_PAIR
                newCounts.values.contains(2) -> HandType.ONE_PAIR
                else -> HandType.HIGH_CARD
            }

            when(jokers) {
                0 -> possibleType
                1 -> when(possibleType) {
                    HandType.FIVE_OF_A_KIND -> error("Impossible to have 5 of a kind with a joker")
                    HandType.FOUR_OF_A_KIND -> HandType.FIVE_OF_A_KIND
                    HandType.FULL_HOUSE -> error("Impossible to have a full house with a joker")
                    HandType.THREE_OF_A_KIND -> HandType.FOUR_OF_A_KIND
                    HandType.TWO_PAIR -> HandType.FULL_HOUSE
                    HandType.ONE_PAIR -> HandType.THREE_OF_A_KIND
                    HandType.HIGH_CARD -> HandType.ONE_PAIR
                }
                2 -> when(possibleType) {
                    HandType.FIVE_OF_A_KIND -> error("Impossible to have 5 of a kind with a joker")
                    HandType.FOUR_OF_A_KIND -> error("Impossible to have 4 of a kind with 2 jokers")
                    HandType.FULL_HOUSE -> error("Impossible to have a full house with 2 jokers")
                    HandType.THREE_OF_A_KIND -> HandType.FIVE_OF_A_KIND
                    HandType.TWO_PAIR -> error("Impossible to have 2 pairs with 2 jokers")
                    HandType.ONE_PAIR -> HandType.FOUR_OF_A_KIND
                    HandType.HIGH_CARD -> HandType.THREE_OF_A_KIND
                }
                3 -> when(possibleType) {
                    HandType.FIVE_OF_A_KIND -> error("Impossible to have 5 of a kind with a joker")
                    HandType.FOUR_OF_A_KIND -> error("Impossible to have 4 of a kind with 3 jokers")
                    HandType.FULL_HOUSE -> error("Impossible to have a full house with 3 jokers")
                    HandType.THREE_OF_A_KIND -> error("Impossible to have 3 of a kind with 3 jokers")
                    HandType.TWO_PAIR -> error("Impossible to have 2 pairs with 3 jokers")
                    HandType.ONE_PAIR -> HandType.FIVE_OF_A_KIND
                    HandType.HIGH_CARD -> HandType.FOUR_OF_A_KIND
                }
                4 -> when(possibleType) {
                    HandType.FIVE_OF_A_KIND -> error("Impossible to have 5 of a kind with a joker")
                    HandType.FOUR_OF_A_KIND -> error("Impossible to have 4 of a kind with 4 jokers")
                    HandType.FULL_HOUSE -> error("Impossible to have a full house with 4 jokers")
                    HandType.THREE_OF_A_KIND -> error("Impossible to have 3 of a kind with 4 jokers")
                    HandType.TWO_PAIR -> error("Impossible to have 2 pairs with 4 jokers")
                    HandType.ONE_PAIR -> error("Impossible to have 1 pair with 4 jokers")
                    HandType.HIGH_CARD -> HandType.FIVE_OF_A_KIND
                }
                5 -> when(possibleType) {
                    HandType.FIVE_OF_A_KIND -> error("Impossible to have 5 of a kind with a joker")
                    HandType.FOUR_OF_A_KIND -> error("Impossible to have 4 of a kind with 5 jokers")
                    HandType.FULL_HOUSE -> error("Impossible to have a full house with 5 jokers")
                    HandType.THREE_OF_A_KIND -> error("Impossible to have 3 of a kind with 5 jokers")
                    HandType.TWO_PAIR -> error("Impossible to have 2 pairs with 5 jokers")
                    HandType.ONE_PAIR -> error("Impossible to have 1 pair with 5 jokers")
                    HandType.HIGH_CARD -> HandType.FIVE_OF_A_KIND
                }
                else -> error("Invalid number of jokers $jokers")
            }
        }
    }
}
