package y2023.d09

import WHITESPACE_DELIMITER
import y2023.InputType
import y2023.PuzzleAnswer

/**
 * answerPart1 = 1834108701
 * answerPart2 =
 *
 * timeElapsedForPart1 = 20
 * timeElapsedForPart2 =
 */
fun main() {
//    MirageMaintenance.checkPart1<Int, Int>()
    MirageMaintenance.checkPart1<Int, Int>(InputType.RealData)
//    MirageMaintenance.checkPart2<Int, Int>()
}

object MirageMaintenance : PuzzleAnswer<Int, Int> {
    override val day = 9
    override val expectedTestAnswerPart1 = 114
    override val expectedTestAnswerPart2 = 0

    override fun part1(input: List<String>) = input
        .toSequences()
        .sumOf { it.extrapolate() }

    override fun part2(input: List<String>): Int {
        TODO("Not yet implemented")
    }

    private fun Sequence<Int>.extrapolate(): Int {
        val subsequence = zipWithNext { a, b -> b - a }
        return last() + if (subsequence.all { it == 0 }) 0 else subsequence.extrapolate()
    }

    private fun List<String>.toSequences() = map { line ->
        line.split(WHITESPACE_DELIMITER)
            .filter { it.isNotEmpty() }
            .mapNotNull { it.toIntOrNull() }
            .asSequence()
    }
}