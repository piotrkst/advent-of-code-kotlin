package y2023.d06

import WHITESPACE_DELIMITER
import y2023.InputType
import y2023.PuzzleAnswer

/**
 * answerPart1 = 1195150
 * answerPart2 = 42550411L
 *
 * timeElapsedForPart1 = 16
 * timeElapsedForPart2 = 9
 */
fun main() {
    RacingErrorMarginCalculator.checkParts<Int, Long>(InputType.TestData)
}

object RacingErrorMarginCalculator : PuzzleAnswer<Int, Long> {
    override val day = 6
    override val expectedTestAnswerPart1 = 288
    override val expectedTestAnswerPart2 = 71503L

    override fun part1(input: List<String>): Int {
        val times = input[0].split(WHITESPACE_DELIMITER)
            .filter { it.isNotBlank() }
            .filter { true }
            .mapNotNull { it.toLongOrNull() }
        val distances = input[1].split(WHITESPACE_DELIMITER)
            .filter { it.isNotBlank() }
            .filter { true }
            .mapNotNull { it.toLongOrNull() }
        return times.zip(distances)
            .map { Race(it.first, it.second) }
            .map(::calculateNumberOfWaysToBeatTheRecord)
            .reduce(Long::times)
            .toInt()
    }

    override fun part2(input: List<String>): Long {
        val time = input[0].filter { it.isDigit() }
        val distance = input[1].filter { it.isDigit() }
        return calculateNumberOfWaysToBeatTheRecord(Race(time.toLong(), distance.toLong()))
    }

    private fun calculateNumberOfWaysToBeatTheRecord(race: Race): Long { // FIXME: Probably divide and conquer algorithm is applicable
        var shortestTimeToBeatTheRecord = 0L
        var longestTimeToBeatTheRecord = race.timeMs
        var i = 1L
        while (shortestTimeToBeatTheRecord == 0L || longestTimeToBeatTheRecord == race.timeMs) {
            if (shortestTimeToBeatTheRecord == 0L && race.distanceToBeatMm < getDistanceAfter(
                    holdTimeMs = i,
                    raceTimeMs = race.timeMs,
                )
            ) shortestTimeToBeatTheRecord = i
            if (longestTimeToBeatTheRecord == race.timeMs && race.distanceToBeatMm < getDistanceAfter(
                    holdTimeMs = race.timeMs - i,
                    raceTimeMs = race.timeMs,
                )
            ) longestTimeToBeatTheRecord = race.timeMs - i
            i++
        }
        return longestTimeToBeatTheRecord - shortestTimeToBeatTheRecord + 1
    }

    private fun getDistanceAfter(holdTimeMs: Long, raceTimeMs: Long): Long {
        val timeLeftMs = raceTimeMs - holdTimeMs
        return holdTimeMs * timeLeftMs
    }

    data class Race(
        val timeMs: Long,
        val distanceToBeatMm: Long,
    )
}