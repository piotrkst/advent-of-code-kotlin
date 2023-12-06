package y2023.d01

import readInput
import y2023.PuzzleAnswer
import kotlin.math.min

private const val expectedTestAnswerPart1 = 142
private const val expectedTestAnswerPart2 = 281

private const val answerPart1 = 54159
private const val answerPart2 = 53866

private const val timeElapsedForPart1 = 8
private const val timeElapsedForPart2 = 119


fun main() {
    val input = readInput(yearPackageName = "y2023", dayPackageName = "d01", fileName = "input")

    Trebuchet.part1(input)
        .also(::println)
        .let { check(it == answerPart1) }
    Trebuchet.part2(input)
        .also(::println)
        .let { check(it == answerPart2) }
}

object Trebuchet : PuzzleAnswer<Int, Int> {
    override fun part1(input: List<String>) = input.mapNotNull {
        val firstDigit = it.first { char -> char.isDigit() }
        val lastDigit = it.last { char -> char.isDigit() }
        "$firstDigit$lastDigit".toIntOrNull()
    }.sum()

    override fun part2(input: List<String>) = input.mapNotNull {
        val firstDigit = it.firstNumber
        val lastDigit = it.lastNumber
        "$firstDigit$lastDigit".toIntOrNull()
    }.sum()

    private val String.firstNumber
        get() = findNumber(isInputReversed = false)

    private val String.lastNumber
        get() = with(reversed()) {
            findNumber(isInputReversed = true)
        }

    private fun String.findNumber(isInputReversed: Boolean) = withIndex().firstNotNullOfOrNull { (index, char) ->
        if (char.isDigit()) char.digitToInt()
        else numbers.firstNotNullOfOrNull { number ->
            val substring = substring(index, min(index + number.key.length, length))
            val key = if (isInputReversed) number.key.reversed() else number.key
            if (key == substring) number.value else null
        }
    }

    private val numbers = mapOf(
        "one" to 1,
        "two" to 2,
        "three" to 3,
        "four" to 4,
        "five" to 5,
        "six" to 6,
        "seven" to 7,
        "eight" to 8,
        "nine" to 9,
    )
}