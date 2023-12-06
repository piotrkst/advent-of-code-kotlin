package y2023.d05

import WHITESPACE_DELIMITER
import y2023.PuzzleAnswer


private const val SEEDS_HEADER = "seeds: "
private const val MAP_HEADER_SUFFIX = " map:"

/**
 * answerPart1 = 535088217L
 * answerPart2 = 51399228L
 *
 * timeElapsedForPart1 = 115
 * timeElapsedForPart2 = 40
 */
fun main() {
    println("Checking part 1...")
    Almanac.checkPart1<Long, Long>()
    println("Checking part 2...")
    Almanac.checkPart2<Long, Long>()
    println("Finished!")
}

object Almanac : PuzzleAnswer<Long, Long> {
    override val day = 5
    override val expectedTestAnswerPart1 = 35L
    override val expectedTestAnswerPart2 = 46L

    override fun part1(input: List<String>): Long {
        val seeds = input.getSeeds()
        val almanac = input.getAlmanac()
        return seeds.map { it.toLocation(almanac) }
            .minBy { it }
    }

    // FIXME: Fix efficiency - kids, do not copy/paste this at home, it's a bad code
    override fun part2(input: List<String>): Long {
        var lowestLocation: Long = Long.MAX_VALUE
        val seeds = input.getSeeds()
        val almanac = input.getAlmanac()
        seeds.chunked(2) { (seed, range) ->
            for (s in seed..<seed + range) {
                val seedLocation = s.toLocation(almanac)
                if (seedLocation <= lowestLocation) {
                    lowestLocation = seedLocation
                }
            }
        }
        return lowestLocation
    }

    private fun Long.toLocation(almanac: List<Set<List<Long>>>): Long {
        var currentNumber = this
        almanac.forEach { set ->
            currentNumber = set.find { mapping ->
                currentNumber in mapping[1]..<mapping[1] + mapping[2]
            }?.let { mapping ->
                mapping[0] + currentNumber - mapping[1]
            } ?: currentNumber
        }
        return currentNumber
    }

    private fun List<String>.getSeeds() = first()
        .substringAfter(SEEDS_HEADER)
        .split(WHITESPACE_DELIMITER)
        .mapNotNull { it.toLongOrNull() }

    private fun List<String>.getAlmanac(): List<Set<List<Long>>> {
        var mapEntries = mutableSetOf<List<Long>>()
        val almanac = mutableMapOf<String, Set<List<Long>>>()
        var mapKey = ""
        drop(2)
            .toMutableList()
            .also { it.add("") }
            .map { it.substringBefore(MAP_HEADER_SUFFIX) }
            .forEach {
                if (it.isBlank()) {
                    almanac[mapKey] = mapEntries
                    mapKey = ""
                    mapEntries = mutableSetOf()
                    return@forEach
                }
                if (!it.first().isDigit()) {
                    mapKey = it
                } else {
                    mapEntries.add(it.split(WHITESPACE_DELIMITER).map { number -> number.toLong() })
                }
            }
        return almanac.values.toList()
    }
}
