package y2023

import readInput

private const val YEAR: String = "2023"

interface PuzzleAnswer<T, R> {
    val day: Int
    val expectedTestAnswerPart1: T
    val expectedTestAnswerPart2: R

    fun readInput(inputType: InputType) = readInput(
        yearPackageName = "y$YEAR",
        dayPackageName = "d${day.dayToString()}",
        fileName = inputType.fileName,
    )

    fun part1(input: List<String>): T
    fun part2(input: List<String>): R

    fun <T, R> checkParts(inputType: InputType = InputType.RealData) {
        checkPart1<T, R>(inputType)
        checkPart2<T, R>(inputType)
    }

    fun <T, R> checkPart1(inputType: InputType = InputType.TestData) {
        part1(readInput(inputType))
            .also(::println)
            .let { if (inputType == InputType.TestData) check(it == expectedTestAnswerPart1) }
    }

    fun <T, R> checkPart2(inputType: InputType = InputType.TestData) {
        part2(readInput(inputType))
            .also(::println)
            .let { if (inputType == InputType.TestData) check(it == expectedTestAnswerPart2) }
    }
}

sealed class InputType(val fileName: String) {
    data object TestData : InputType("test_input")
    data object RealData : InputType("input")
    data class Custom(val customPath: String) : InputType(customPath)
}

private fun Int.dayToString() = if (this < 10) "0$this" else "$this"