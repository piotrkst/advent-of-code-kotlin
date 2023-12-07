package y2023.d07

import WHITESPACE_DELIMITER
import y2023.PuzzleAnswer


private typealias ListWithoutEmptyEntries = List<Int>
private typealias EntriesSortedDescending = List<Int>

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

    private fun Int.accumulate(index: Int, input: Input) = this + ((index + 1) * input.bid)

    data class Input(
        val hand: Hand,
        val bid: Int,
    )

    data class Hand(
        private val input: List<Char>,
        private val isJokerRuleApplied: Boolean = false,
    ): Comparable<Hand> {
        override fun compareTo(other: Hand) = isStrongerByType(this, other)

        private val cards: List<Int> = input.mapToCards()
        private val type: HandType = cards.handType()

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
            val cardsCounter = IntArray(14) { _ -> 0 } // all possible values counter (1-14)
            forEach { card -> cardsCounter[card - 1]++ } // fill matrix
            val max = cardsCounter.drop(1).max() // find max excluding joker
            val indexOfMax = cardsCounter.drop(1).indexOf(max) + 1 // get index of max excluding joker
            if (isJokerRuleApplied) {
                cardsCounter[indexOfMax] += cardsCounter[0] // bump max with jokers
                cardsCounter[0] = 0 // remove joker from matrix
            }
            return cardsCounter.filterNot { it == 0 }.toHandType() ?: error("Undefined")
        }

        private fun ListWithoutEmptyEntries.toHandType() = HandType.entries.find {
            it.rule(sortedDescending())
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

    enum class HandType(val strength: Int, val rule: (EntriesSortedDescending) -> Boolean) {
        FIVE_OF_A_KIND(strength = 7, rule = { entries -> entries.size == 1 }),
        FOUR_OF_A_KIND(strength = 6, rule = { entries -> entries.size == 2 && entries.isExactly(listOf(4, 1)) }),
        FULL_HOUSE(strength = 5, rule = { entries -> entries.size == 2 && entries.isExactly(listOf(3, 2)) }),
        THREE_OF_A_KIND(strength = 4, rule = { entries -> entries.size == 3 && entries.isExactly(listOf(3, 1, 1)) }),
        TWO_PAIR(strength = 3, rule = { entries -> entries.size == 3 && entries.isExactly(listOf(2, 2, 1)) }),
        ONE_PAIR(strength = 2, rule = { entries -> entries.size == 4 }),
        HIGH_CARD(strength = 1, rule = { entries -> entries.size == 5 });
    }

    private fun List<Int>.isExactly(other: List<Int>) = zip(other).all { (first, second) -> first == second }
}
