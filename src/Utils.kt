import kotlin.io.path.Path
import kotlin.io.path.readLines


const val WHITESPACE_DELIMITER = " "

/**
 * Reads lines from the given input txt file.
 */
fun readInput(yearPackageName: String, dayPackageName: String, fileName: String) =
    Path("src/$yearPackageName/$dayPackageName/$fileName.txt").readLines()
