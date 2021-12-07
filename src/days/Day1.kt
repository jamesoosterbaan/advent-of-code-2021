package days

import java.io.File

class Day1(private val inputFile: File): AdventSolution(inputFile) {
    override fun part1() {
        val depths = parseInputFile(inputFile)
        println("The depth of the floor increased ${singleMeasurementWindowIncrease(depths)} times.")
    }

    override fun part2() {
        val depths = parseInputFile(inputFile)
        val slidingWindows = calculateThreeWindowSlidingDistances(depths)
        println("The three-window-sliding measurements increased ${singleMeasurementWindowIncrease(slidingWindows)} times.")
    }

    /**
     * Given a list of depths, calculate how many times the sequential numbers increase.
     */
    private fun singleMeasurementWindowIncrease(depths: List<Int>): Int {
        var numberOfIncreases = 0
        for (i in 0..depths.size-2) {
            if (depths[i+1] > depths[i]) numberOfIncreases++
        }
        return numberOfIncreases
    }

    /**
     * Given a list of depths, calculate the three-sliding-window sums of the depths.
     */
    private fun calculateThreeWindowSlidingDistances(depths: List<Int>): List<Int> {
        val windows: MutableList<Int> = mutableListOf()

        for (i in 0..depths.size-3) {
            windows.add(depths[i] + depths[i+1] + depths[i+2])
        }

        return windows
    }

    private fun parseInputFile(file: File): List<Int> {
        return file.readLines().map { it.toInt() }
    }
}






