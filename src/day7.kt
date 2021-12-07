import java.io.File
import java.lang.IllegalStateException
import kotlin.math.abs

fun main(args: Array<String>) {
    if (args.isEmpty()) {
        println("Please enter the file name.")
        return
    }
    val fileName = args[0]
    val input: List<String> = File("resources/${fileName}").readLines().filter { it != "" }
    val horizontalPositions: List<Int> = input.first().split(",").map { it.toInt() }
    val range = calculateRange(horizontalPositions)

    day7part1(horizontalPositions, range)
    day7part2(horizontalPositions, range)
}

private fun day7part1(horizontalPositions: List<Int>, range: IntRange) {
    println("\n--- Part 1 ---\n")

    val fuelCosts: MutableMap<Int, Long> = mutableMapOf()
    for (destination in range) {
        horizontalPositions.forEach { fuelCosts[destination] = (fuelCosts[destination] ?: 0) + abs(it - destination) }
    }
    val minFuel: Pair<Int, Long> = fuelCosts.minByOrNull { it.value }!!.toPair()

    println("The minimum fuel to use is '${minFuel.second}', by converging on position '${minFuel.first}'.")
}

private fun day7part2(horizontalPositions: List<Int>, range: IntRange) {
    println("\n--- Part 2 ---\n")

    val fuelCosts: MutableMap<Int, Long> = mutableMapOf()
    for (destination in range) {
        horizontalPositions.forEach { fuelCosts[destination] = (fuelCosts[destination] ?: 0) + calculateFuel(it, destination) }
    }
    val minFuel: Pair<Int, Long> = fuelCosts.minByOrNull { it.value }!!.toPair()

    println("The minimum fuel to use is '${minFuel.second}', by converging on position '${minFuel.first}'.")
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

fun calculateRange(positions: List<Int>): IntRange {
    val minPosition: Int = positions.minOrNull() ?: throw IllegalStateException("No min value.")
    val maxPosition: Int = positions.maxOrNull() ?: throw IllegalStateException("No max value.")
    return minPosition .. maxPosition
}
