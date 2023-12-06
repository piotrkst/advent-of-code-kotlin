package y2023.d04

import WHITESPACE_DELIMITER
import readInput
import y2023.PuzzleAnswer

private const val answerPart1 = 25231
private const val answerPart2 = 9721255

private const val timeElapsedForPart1 = 39
private const val timeElapsedForPart2 = 31

private const val CARD_LABEL_DELIMITER = ":"
private const val CARD_WINNING_AND_LOTTERY_NUMBERS_DELIMITER = "|"

typealias Numbers = List<Int>

fun main() {
    val input = readInput(yearPackageName = "y2023", dayPackageName = "d04", fileName = "input")

    Scratchcards.part1(input)
        .also(::println)
        .let { check(it == answerPart1) }
    Scratchcards.part2(input)
        .also(::println)
        .let { check(it == answerPart2) }
}

object Scratchcards : PuzzleAnswer<Int, Int> {
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
