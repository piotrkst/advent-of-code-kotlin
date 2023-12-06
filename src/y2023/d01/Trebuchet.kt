package y2023.d01

import y2023.InputType
import y2023.PuzzleAnswer
import kotlin.math.min


/**
 * answerPart1 = 54159
 * answerPart2 = 53866
 *
 * timeElapsedForPart1 = 8
 * timeElapsedForPart2 = 119
 */
fun main() {
    Trebuchet.checkParts<Int, Int>()
    Trebuchet.checkPart1<Int, Int>(InputType.TestData)
    Trebuchet.checkPart2<Int, Int>(InputType.Custom("test_input2"))
}

object Trebuchet : PuzzleAnswer<Int, Int> {
    override val day = 1
    override val expectedTestAnswerPart1 = 142
    override val expectedTestAnswerPart2 = 281

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