package days

import java.io.File
import java.lang.IllegalStateException
import kotlin.math.abs

class Day7(private val inputFile: File): AdventSolution(inputFile) {
    override fun part1() {
        val input = parseInput(inputFile)
        val range = input.first
        val horizontalPositions = input.second

        val fuelCosts: MutableMap<Int, Long> = mutableMapOf()
        for (destination in range) {
            horizontalPositions.forEach { fuelCosts[destination] = (fuelCosts[destination] ?: 0) + abs(it - destination) }
        }
        val minFuel: Pair<Int, Long> = fuelCosts.minByOrNull { it.value }!!.toPair()

        println("The minimum fuel to use is '${minFuel.second}', by converging on position '${minFuel.first}'.")
    }

    override fun part2() {
        val input = parseInput(inputFile)
        val range = input.first
        val horizontalPositions = input.second

        val fuelCosts: MutableMap<Int, Long> = mutableMapOf()
        for (destination in range) {
            horizontalPositions.forEach { fuelCosts[destination] = (fuelCosts[destination] ?: 0) + calculateFuel(it, destination) }
        }
        val minFuel: Pair<Int, Long> = fuelCosts.minByOrNull { it.value }!!.toPair()

        println("The minimum fuel to use is '${minFuel.second}', by converging on position '${minFuel.first}'.")
    }

    private fun parseInput(file: File): Pair<IntRange, List<Int>> {
        val horizontalPositions: List<Int> = file.readLines().first { it != "" }.split(",").map { it.toInt() }
        val minPosition: Int = horizontalPositions.minOrNull() ?: throw IllegalStateException("No min value.")
        val maxPosition: Int = horizontalPositions.maxOrNull() ?: throw IllegalStateException("No max value.")

        return Pair(minPosition .. maxPosition, horizontalPositions)
    }

    /**
     * Given a current position and destination, calculate the fuel required to reach the position with the following constraints:
     * 1. The fuel cost starts at '1' per unit of movement.
     * 2. The fuel costs increases by '1' per unit of movement.
     */
    private fun calculateFuel(position: Int, destination: Int): Long {
        var fuel: Long = 0
        var fuelIncrement = 1

        val distanceToDestination = abs(position - destination)

        for (element in 1 .. distanceToDestination) {
            fuel += fuelIncrement
            fuelIncrement++
        }

        return fuel
    }
}
