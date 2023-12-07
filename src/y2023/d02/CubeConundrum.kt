package y2023.d02

import WHITESPACE_DELIMITER
import y2023.InputType
import y2023.PuzzleAnswer


private const val GAME_LABEL_DELIMITER = ":"
private const val CUBE_DELIMITER = ','
private const val DRAW_DELIMITER = ";"

/**
 * answerPart1 = 2331
 * answerPart2 = 71585
 *
 * timeElapsedForPart1 = 47
 * timeElapsedForPart2 = 22
 */
fun main() {
    CubeConundrum.checkParts<Int, Int>(InputType.TestData)
}
object CubeConundrum : PuzzleAnswer<Int, Int> {
    override val day = 2
    override val expectedTestAnswerPart1 = 8
    override val expectedTestAnswerPart2 = 2286

    override fun part1(input: List<String>) = input.sumOf { gameInput ->
        Game.fromGameInputText(gameInput)
            .run {
                if (draws.all { it.isValid }) this.id else 0
            }
    }

    override fun part2(input: List<String>) = input.sumOf { gameInput ->
        Game.fromGameInputText(gameInput)
            .powerOfSet()
    }

    data class Game(
        val id: Int,
        val draws: List<Draw>,
    ) {
        private val cubes
            get() = draws.flatMap { it.cubes }

        fun powerOfSet() = powerOfSet(
            redMax = cubes.maxAmountOf(CubeType.RED),
            blueMax = cubes.maxAmountOf(CubeType.BLUE),
            greenMax = cubes.maxAmountOf(CubeType.GREEN),
        )

        private fun List<Cube>.maxAmountOf(type: CubeType) = filter { it.cubeType == type }
            .maxOf { it.amount }

        private fun powerOfSet(
            redMax: Int,
            blueMax: Int,
            greenMax: Int,
        ) = redMax * blueMax * greenMax

        companion object {
            fun fromGameInputText(input: String): Game {
                val (gameLabel, drawText) = input.split(GAME_LABEL_DELIMITER)
                val gameId = gameLabel.filter { it.isDigit() }
                    .toInt()
                val draws = drawText.filterNot { it == CUBE_DELIMITER }
                    .split(DRAW_DELIMITER)
                    .map {
                        it.split(WHITESPACE_DELIMITER)
                            .filter(String::isNotBlank)
                            .chunked(2)
                            .map { singleText ->
                                Cube(
                                    cubeType = CubeType.valueOf(singleText[1].uppercase()),
                                    amount = singleText[0].toInt(),
                                )
                            }
                    }
                    .map { Draw(it) }
                return Game(gameId, draws)
            }
        }
    }

    data class Draw(val cubes: List<Cube>) {
        val isValid
            get() = cubes.all { it.isValid }
    }

    data class Cube(
        val cubeType: CubeType,
        val amount: Int,
    ) {
        val isValid
            get() = amount <= cubeType.max
    }

    enum class CubeType(val max: Int) {
        RED(12),
        BLUE(14),
        GREEN(13),
    }
}