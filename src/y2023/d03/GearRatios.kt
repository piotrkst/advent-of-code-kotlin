package y2023.d03

import readInput

private const val expectedAnswerPart1 = 514969
private const val expectedAnswerPart2 = 78915902

private const val timeElapsedForPart1 = 71
private const val timeElapsedForPart2 = 30

private const val GEAR_NEIGHBOURING_PART_NUMBERS = 2

fun main() {
    val input = readInput(yearPackageName = "y2023", dayPackageName = "d03", fileName = "input")

    GearRatios.part1(input)
        .also(::println)
        .let { check(it == expectedAnswerPart1) }
    GearRatios.part2(input)
        .also(::println)
        .let { check(it == expectedAnswerPart2) }
}

object GearRatios {
    fun part1(input: List<String>): Int {
        return input.addTrailingDots()
            .findNumbers()
            .filter { it.hasAdjacentSymbol(input) }
            .sumOf { it.value }
    }

    fun part2(input: List<String>): Int {
        val numbers = input.addTrailingDots()
            .findNumbers()
        return input.findPossibleGears()
            .map { it.neighbouringNumbers(numbers) }
            .filter { it.size == GEAR_NEIGHBOURING_PART_NUMBERS }
            .sumOf { it.calculateGearRatio() }
    }

    private fun List<String>.findPossibleGears(): List<Coordinates> {
        val coordinates = mutableListOf<Coordinates>()
        forEachIndexed { yIndex, input ->
            input.forEachIndexed { xIndex, char ->
                if (char == '*') coordinates.add(Coordinates(x = xIndex, y = yIndex))
            }
        }
        return coordinates
    }

    private fun List<Number>.calculateGearRatio(): Int {
        require(size == GEAR_NEIGHBOURING_PART_NUMBERS) {
            "Not a gear"
        }
        return first().value * last().value
    }

    private fun Coordinates.neighbouringNumbers(numbers: List<Number>) = numbers
        .filterVerticalNeighbours(y)
        .filterHorizontalNeighbours(x)

    private fun List<Number>.filterVerticalNeighbours(y: Int) = filter { it.yIndex in y - 1..y + 1 }
    private fun List<Number>.filterHorizontalNeighbours(x: Int) =
        filter { IntRange(it.xRange.first - 1, it.xRange.last + 1).contains(x) }

    private fun List<String>.addTrailingDots() = map { "$it." }

    private fun List<String>.findNumbers(): List<Number> {
        val numbers = mutableListOf<Number>()
        forEachIndexed { yIndex, input ->
            var number = ""
            var xRange = IntRange.EMPTY
            input.forEachIndexed { xIndex, char ->
                if (char.isDigit()) {
                    number += char
                    xRange = IntRange(
                        start = if (xRange.isEmpty()) xIndex else xRange.first,
                        endInclusive = if (xRange.isEmpty()) xIndex else xRange.last + 1,
                    )
                } else number.toIntOrNull()?.let {
                    numbers.add(Number(it, xRange, yIndex))
                    number = ""
                    xRange = IntRange.EMPTY

                }
            }

        }
        return numbers
    }

    private fun Number.hasAdjacentSymbol(input: List<String>) =
        input.filterToSurroundings(xRange, yIndex).any { it != '.' && !it.isDigit() }

    private fun List<String>.filterToSurroundings(
        xRange: IntRange,
        yIndex: Int,
    ) = subList(
        maxOf(0, yIndex - 1),
        minOf(lastIndex + 1, yIndex + 2),
    ).map {
        it.substring(
            maxOf(0, xRange.first - 1),
            minOf(it.lastIndex + 1, xRange.last + 2),
        )
    }.joinToString("") { it }

    data class Coordinates(
        val x: Int,
        val y: Int,
    )

    data class Number(
        val value: Int,
        val xRange: IntRange,
        val yIndex: Int,
    )
}
