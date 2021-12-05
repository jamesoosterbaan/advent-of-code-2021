import java.io.File

fun main(args: Array<String>) {
    if (args.isEmpty()) {
        println("Please enter the file name.")
        return
    }
    val fileName = args[0]
    val input: List<String> = File("resources/${fileName}").readLines().filter { it != "" }
    var maxXPosition = 0
    var maxYPosition = 0
    val vents: List<HydrothermalVent> = input
        .asSequence()
        .map { it.split(" -> ") }
        .map { coordinateString -> coordinateString
            .flatMap { it.trim().split(",") }
            .map { it.toInt() }
        }.map { coordinates ->
            // Create the coordinate pairs
            val startingCoordinate = Coordinate(coordinates[0], coordinates[1])
            val endingCoordinate = Coordinate(coordinates[2], coordinates[3])
            Pair(startingCoordinate, endingCoordinate)
        }
        .map {
            //See if we have a new maximum board size
            if (it.first.xPosition > maxXPosition) maxXPosition = it.first.xPosition
            if (it.second.xPosition > maxXPosition) maxXPosition = it.second.xPosition
            if (it.first.yPosition > maxYPosition) maxYPosition = it.first.yPosition
            if (it.second.yPosition > maxYPosition) maxYPosition = it.second.yPosition

            // Add the hydrothermal vent to the list
            HydrothermalVent(it.first, it.second)
        }
        .toList()

    val mapSize = Coordinate(maxXPosition, maxYPosition)

    day5part1(vents, mapSize, 2)
    day5part2(vents, mapSize, 2)
}

private fun day5part1(vents: List<HydrothermalVent>, mapSize: Coordinate, dangerousLimit: Int) {
    println("\n--- Part 1 ---\n")
    val nonDiagonalVents = vents.filter { !it.isDiagonal() }
    val map = createVentMap(nonDiagonalVents, mapSize)
    printVentMap(map, dangerousLimit)
}

private fun day5part2(vents: List<HydrothermalVent>, mapSize: Coordinate, dangerousLimit: Int) {
    println("\n--- Part 2 ---\n")
    val map = createVentMap(vents, mapSize)
    printVentMap(map, dangerousLimit)
}

/**
 * Given a list of [HydrothermalVent]'s, and the size of the map, plot the vents on the map.
 *
 * @param vents the list of [HydrothermalVent]'s to plot
 * @param mapSize the upper-most [Coordinate] on the map
 */
private fun createVentMap(vents: List<HydrothermalVent>, mapSize: Coordinate): Array<Array<Int>> {
    val map: Array<Array<Int>> = Array(mapSize.xPosition+1) { Array(mapSize.yPosition+1) { 0 } }
    vents.forEach { vent ->
        vent.line.forEach {
            map[it.xPosition][it.yPosition]++
        }
    }
    return map
}

/**
 * Print out the resulting map of [HydrothermalVent]'s, and how many points are above the dangerous limit.
 *
 * @param map the map of [HydrothermalVent]'s
 * @param dangerousLimit the (inclusive) limit above which we need to mark as dangerous
 */
private fun printVentMap(map: Array<Array<Int>>, dangerousLimit: Int) {
    var dangerousSpots = 0
    println("Hydrothermal Vent map:\n")
    for (y in 0 until map[0].size) {
        for (element in map) {
            val positionalValue = element[y]
            if (positionalValue >= dangerousLimit) dangerousSpots++
            if (positionalValue == 0) print(". ")
            else print("$positionalValue ")
        }
        println()
    }
    println("\nNumber of dangerous spots is: $dangerousSpots")
}

private class HydrothermalVent(
    private val startingCoordinate: Coordinate,
    private val endingCoordinate: Coordinate
) {
    val line: MutableList<Coordinate> = mutableListOf()
    private val startX = startingCoordinate.xPosition
    private val startY = startingCoordinate.yPosition
    private val endX = endingCoordinate.xPosition
    private val endY = endingCoordinate.yPosition

    // Set up the ranges to work through when generating the lines.
    private val xRange = if (startX < endX) startX .. endX
    else startX.downTo(endX)
    private val yRange = if (startY < endY) startY .. endY
    else startY.downTo(endY)

    init {
        // Diagonal lines require some extra logic to add just those points on the line.
        if (isDiagonal()) {
            // We need to start off by adding the initial point on the line.
            var xPosition = xRange.first
            var yPosition = yRange.first
            line.add(Coordinate(xPosition, yPosition))

            // Then we iterate through each point along the line and add it.
            while(yPosition != yRange.last && xPosition != xRange.last) {
                xPosition += xRange.step
                yPosition += yRange.step
                line.add(Coordinate(xPosition, yPosition))
            }
        } else {
            for (x in xRange) {
                for (y in yRange) {
                    line.add(Coordinate(x, y))
                }
            }
        }
    }

    /**
     * Whether the line generated for this [HydrothermalVent] is diagonal or straight (vertical/horizontal).
     */
    fun isDiagonal(): Boolean {
        return !(startX == endX || startY == endY)
    }

    override fun toString(): String {
        return "Starting Coordinate: $startingCoordinate, Ending Coordinate: $endingCoordinate"
    }
}

private data class Coordinate(val xPosition: Int, val yPosition: Int)
