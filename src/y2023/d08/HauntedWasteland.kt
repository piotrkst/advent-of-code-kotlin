package y2023.d08

import y2023.PuzzleAnswer


private const val DIRECTIONS_INDEX = 0
private const val DIRECTION_HEADER_ROWS = 2
private const val DEFAULT_START = "AAA"
private const val DEFAULT_END = "ZZZ"
private const val SIMULTANEOUS_START_SUFFIX = "A"
private const val SIMULTANEOUS_END_SUFFIX = "Z"
private const val COMMAND_LEFT = 'L'
private const val COMMAND_RIGHT = 'R'

/**
 * answerPart1 = 20513
 * answerPart2 = 15995167053923
 *
 * timeElapsedForPart1 = 183
 * timeElapsedForPart2 = 189
 */
fun main() {
    HauntedWasteland.checkParts<Int, Long>()
}

object HauntedWasteland : PuzzleAnswer<Int, Long> {
    override val day = 8
    override val expectedTestAnswerPart1 = 6
    override val expectedTestAnswerPart2 = 6L

    override fun part1(input: List<String>): Int {
        val directions = input[DIRECTIONS_INDEX].toDirections()
        val graph = input.drop(DIRECTION_HEADER_ROWS)
            .mapToSteps()
            .toGraph()
        return calculateRequiredStepsToReachGoal(
            directions,
            graph,
        )
    }

    override fun part2(input: List<String>): Long {
        val directions = input[DIRECTIONS_INDEX].toDirections()
        val steps = input.drop(DIRECTION_HEADER_ROWS)
            .mapToSteps()
        val startKeys = steps.map { it.key }
            .filter { it.endsWith(SIMULTANEOUS_START_SUFFIX) }
        val graph = steps.toGraph()
        val counts = startKeys.map { startKey ->
            calculateRequiredStepsToReachGoal(
                directions = directions,
                graph = graph,
                start = startKey,
                endRule =  { it.endsWith(SIMULTANEOUS_END_SUFFIX) }
            )
        }
        return leastCommonMultiple(counts.map { it.toLong() })
    }

    private fun calculateRequiredStepsToReachGoal(
        directions: List<Direction>,
        graph: Graph,
        start: String = DEFAULT_START,
        endRule: (String) -> Boolean = { it == DEFAULT_END},
    ) = directions.sequence.runningFold(start, graph::next)
        .takeWhile { !endRule(it) }
        .count()

    private val List<Direction>.sequence: Sequence<Direction>
        get() = generateSequence { this }.flatten()

    private fun List<String>.mapToSteps() = associate { nodeInput ->
        val (key, left, right) = nodeInput
            .filter { it !in setOf(' ', '(', ')') }
            .split("=", ",")
        Pair(key, Step(left, right))
    }

    private fun Map<String, Step>.toGraph() = Graph().also { graph ->
        forEach { graph.addVertex(it.key) }
        forEach { graph.connect(it.key, it.value.left, it.value.right) }
    }

    data class Step(
        val left: String,
        val right: String,
    )

    enum class Direction(val command: Char) {
        LEFT(COMMAND_LEFT),
        RIGHT(COMMAND_RIGHT),
    }

    private fun String.toDirections() = mapNotNull {
        Direction.entries.find { direction ->
            direction.command == it
        }
    }

    private fun leastCommonMultiple(values: List<Long>): Long {
        var leastCommonMultiple = values.first()
        values.forEach {
            leastCommonMultiple = leastCommonMultiple(leastCommonMultiple, it)
        }
        return leastCommonMultiple
    }

    private fun leastCommonMultiple(
        a: Long,
        b: Long,
    ) = if (a == 0L || b == 0L) 0 else (a * b) / greatestCommonDivisor(a, b)

    private fun greatestCommonDivisor(
        a: Long,
        b: Long,
    ): Long = if (b == 0L) a else greatestCommonDivisor(b, a.rem(b))

    class Graph {
        private val vertices = mutableMapOf<String, Vertex>()

        operator fun get(name: String) = vertices[name] ?: error("No valid vertex")

        fun addVertex(name: String) {
            vertices[name] = Vertex(name)
        }

        fun connect(from: String, left: String, right: String) {
            connect(this[from], this[left], this[right])
        }

        fun next(
            from: String,
            direction: Direction,
        ) = when (direction) {
            Direction.LEFT -> get(from).left.name
            Direction.RIGHT -> get(from).right.name
        }

        private fun connect(from: Vertex, left: Vertex, right: Vertex) {
            from.left = left
            from.right = right
        }

        data class Vertex(
            val name: String,
        ) {
            lateinit var left: Vertex
            lateinit var right: Vertex
        }
    }
}