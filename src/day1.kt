import java.io.File

fun main(args: Array<String>) {
    if (args.isEmpty()) {
        println("Please enter the file name.")
        return
    }
    val fileName = args[0]
    val depths = File("resources/${fileName}").readLines().map{s -> s.toInt()}

    part1(depths)
    part2(depths)
}

fun part1(depths: List<Int>) {
    println("The depth of the floor increased ${singleMeasurementWindowIncrease(depths)} times.")
}

fun part2(depths: List<Int>) {
    val slidingWindows = calculateThreeWindowSlidingDistances(depths)
    println("The three-window-sliding measurements increased ${singleMeasurementWindowIncrease(slidingWindows)} times.")
}

/**
 * Given a list of depths, calculate how many times the sequential numbers increase.
 */
fun singleMeasurementWindowIncrease(depths: List<Int>): Int {
    var numberOfIncreases = 0
    for (i in 0..depths.size-2) {
        if (depths[i+1] > depths[i]) numberOfIncreases++
    }
    return numberOfIncreases
}

/**
 * Given a list of depths, calculate the three-sliding-window sums of the depths.
 */
fun calculateThreeWindowSlidingDistances(depths: List<Int>): List<Int> {
    val windows: MutableList<Int> = mutableListOf()

    for (i in 0..depths.size-3) {
        windows.add(depths[i] + depths[i+1] + depths[i+2])
    }

    return windows
}
