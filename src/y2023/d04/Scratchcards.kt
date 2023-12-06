package y2023.d04

import WHITESPACE_DELIMITER
import y2023.PuzzleAnswer


private const val CARD_LABEL_DELIMITER = ":"
private const val CARD_WINNING_AND_LOTTERY_NUMBERS_DELIMITER = "|"

typealias Numbers = List<Int>

/**
 * answerPart1 = 25231
 * answerPart2 = 9721255
 *
 * timeElapsedForPart1 = 39
 * timeElapsedForPart2 = 31
 */
fun main() {
    Scratchcards.checkParts<Int, Int>()
}

object Scratchcards : PuzzleAnswer<Int, Int> {
    override val day = 4
    override val expectedTestAnswerPart1 = 25231
    override val expectedTestAnswerPart2 = 9721255

    override fun part1(input: List<String>) = input.sumOf { card -> card.inputToCard().points }

    override fun part2(input: List<String>): Int {
        val cards = input.map { card -> card.inputToCard() }
        return cards.mapIndexed { index, card ->
            cards.bumpNextCardsQuantities(index, card)
            card.quantity
        }.sum()
    }

    private fun List<Card>.bumpNextCardsQuantities(index: Int, card: Card) {
        for (i in 1..card.scores) {
            this[index + i].quantity += card.quantity
        }
    }

    private fun String.inputToCard() = substringAfter(CARD_LABEL_DELIMITER)
        .split(CARD_WINNING_AND_LOTTERY_NUMBERS_DELIMITER)
        .map { extractNumbers(it) }
        .toCard()

    private fun extractNumbers(numbersInput: String): List<Int> =
        numbersInput.split(WHITESPACE_DELIMITER).mapNotNull { it.toIntOrNull() }

    private fun List<Numbers>.toCard(): Card {
        require(size == 2) { "Invalid numbers list size for Card creation" }
        return Card(this[0], this[1])
    }

    data class Card(
        val winningNumbers: Numbers,
        val lotteryNumbers: Numbers,
        var quantity: Int = 1,
    ) {
        val points: Int
            get() = calculatePoints()

        val scores: Int
            get() = lotteryNumbers.count { winningNumbers.contains(it) }

        private fun calculatePoints(): Int {
            val power = lotteryNumbers.count { winningNumbers.contains(it) } - 1
            return if (power < 0) 0 else 1.shl(power)
        }
    }
}
