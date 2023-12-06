package y2023

interface PuzzleAnswer<T, R> {
    fun part1(input: List<String>): T
    fun part2(input: List<String>): R
}
