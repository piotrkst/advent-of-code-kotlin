package y2023.d07

import WHITESPACE_DELIMITER
import y2023.PuzzleAnswer


/**
 * answerPart1 = 253910319
 * answerPart2 = 254083736
 *
 * timeElapsedForPart1 = 92
 * timeElapsedForPart2 = 52
 */
fun main() {
    CamelCards.checkParts<Int, Int>()
}
object CamelCards : PuzzleAnswer<Int, Int> {
    override val day = 7
    override val expectedTestAnswerPart1 = 6440
    override val expectedTestAnswerPart2 = 5905

    override fun part1(input: List<String>) = input.map {
        val (hand, bid) = it.split(WHITESPACE_DELIMITER)
        Input(
            hand = Hand(hand.toList()),
            bid = bid.toInt(),
        )
    }.sortAndCalculateWinnings()

    override fun part2(input: List<String>) = input.map {
        val (hand, bid) = it.split(WHITESPACE_DELIMITER)
        Input(
            hand = Hand(
                input = hand.toList(),
                isJokerRuleApplied = true,
            ),
            bid = bid.toInt(),
        )
    }.sortAndCalculateWinnings()

    private fun List<Input>.sortAndCalculateWinnings() = this
        .sortedBy { it.hand }
        .foldIndexed(0) { index, acc, item -> acc.accumulate(index, item) }

    private fun Int.accumulate(index: Int, input: Input): Int {
        val winning = ((index + 1) * input.bid)
        println("ID: $index, hand: ${input.hand.cards}, ${input.hand.type}, bid: ${input.bid}, winning: $winning")
        return this + winning
    }

    data class Input(
        val hand: Hand,
        val bid: Int,
    )

    data class Hand(
        private val input: List<Char>,
        private val isJokerRuleApplied: Boolean = false,
    ): Comparable<Hand> {
        override fun compareTo(other: Hand) = isStrongerByType(this, other)

        val cards: List<Int> = input.mapToCards()
        val type: HandType = cards.handType()

        private fun List<Char>.mapToCards() = map {
            when (it) {
                'A' -> 14
                'K' -> 13
                'Q' -> 12
                'J' -> if (isJokerRuleApplied) 1 else 11
                'T' -> 10
                else -> it.digitToInt()
            }
        }

        private fun List<Int>.handType(): HandType {
            val cardsCounter = IntArray(14) { _ -> 0 }
            forEach { card -> cardsCounter[card - 1]++ }
            var max = cardsCounter.drop(1).max()
            val indexOfMax = cardsCounter.drop(1).indexOf(max) + 1
            if (isJokerRuleApplied) {
                cardsCounter[indexOfMax] += cardsCounter[0]
                cardsCounter[0] = 0
            }
            max = cardsCounter.max()
            val firstSet = max.toHandType() ?: error("Invalid input")
            if (firstSet in listOf(HandType.FIVE_OF_A_KIND, HandType.FOUR_OF_A_KIND)) return firstSet
            cardsCounter[indexOfMax] = 0
            val secondSet = cardsCounter.max().toHandType()
            if (firstSet.repeatedCards != secondSet?.repeatedCards
                    && setOf(
                            firstSet.repeatedCards,
                            secondSet?.repeatedCards,
                    ).containsAll(setOf(2, 3))) return HandType.FULL_HOUSE
            return when {
                firstSet.repeatedCards == HandType.THREE_OF_A_KIND.repeatedCards -> HandType.THREE_OF_A_KIND
                firstSet.repeatedCards == HandType.TWO_PAIR.repeatedCards
                        && secondSet?.repeatedCards == HandType.TWO_PAIR.repeatedCards -> HandType.TWO_PAIR
                firstSet.repeatedCards == HandType.ONE_PAIR.repeatedCards -> HandType.ONE_PAIR
                else -> HandType.HIGH_CARD
            }
        }

        private fun Int.toHandType() = HandType.entries.find {
            it.repeatedCards == this
        }

        private fun isStrongerByType(first: Hand, second: Hand) = when {
            first.type.strength > second.type.strength -> 1
            first.type.strength < second.type.strength -> -1
            else -> isStrongerBySingleCard(first, second)
        }

        private fun isStrongerBySingleCard(first: Hand, second: Hand): Int {
            for (i in 0..4) {
                val firstHandCard = first.cards[i]
                val secondHandCard = second.cards[i]
                return when {
                    firstHandCard == secondHandCard -> continue
                    firstHandCard > secondHandCard -> 1
                    else -> -1
                }
            }
            return 0
        }
    }

    enum class HandType(val strength: Int, val repeatedCards: Int? = null) {
        FIVE_OF_A_KIND(strength = 7, repeatedCards = 5),
        FOUR_OF_A_KIND(strength = 6, repeatedCards = 4),
        FULL_HOUSE(strength = 5),
        THREE_OF_A_KIND(strength = 4, repeatedCards = 3),
        TWO_PAIR(strength = 3),
        ONE_PAIR(strength = 2, repeatedCards = 2),
        HIGH_CARD(strength = 1, repeatedCards = 1),
    }
}