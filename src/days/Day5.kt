package days

import java.io.File

class Day5(private val inputFile: File): AdventSolution(inputFile) {
    override fun part1() {
        val inputs = parseInput(inputFile)
        val nonDiagonalVents = inputs.second.filter { !it.isDiagonal() }
        val map = createVentMap(nonDiagonalVents, inputs.first)
        calculateNumberOfDangerousSpots(map, DANGEROUS_LIMIT)
    }

    override fun part2() {
        val inputs = parseInput(inputFile)
        val map = createVentMap(inputs.second, inputs.first)
        calculateNumberOfDangerousSpots(map, DANGEROUS_LIMIT)
    }

    private fun parseInput(file: File): Pair<Coordinate, List<HydrothermalVent>> {
        var maxXPosition = 0
        var maxYPosition = 0

        val vents = file.readLines().filter { it != "" }
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

        return Pair(mapSize, vents)
    }

    companion object {
        private const val DANGEROUS_LIMIT = 2
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
     * Print out the resulting map of [HydrothermalVent]'s.
     *
     * @param map the map of [HydrothermalVent]'s
     */
    private fun printVentMap(map: Array<Array<Int>>) {
        println("Hydrothermal Vent map:\n")
        for (y in 0 until map[0].size) {
            for (element in map) {
                val positionalValue = element[y]
                if (positionalValue == 0) print(". ")
                else print("$positionalValue ")
            }
            println()
        }
    }

    /**
     * Calculate the number of dangerous points from the map of [HydrothermalVent]'s.
     *
     * @param map the map of [HydrothermalVent]'s
     * @param dangerousLimit the (inclusive) limit above which we need to mark as dangerous
     */
    private fun calculateNumberOfDangerousSpots(map: Array<Array<Int>>, dangerousLimit: Int) {
        var dangerousSpots = 0
        for (y in 0 until map[0].size) {
            for (element in map) {
                if (element[y] >= dangerousLimit) dangerousSpots++
            }
        }
        println("\nNumber of dangerous spots is: $dangerousSpots")
    }


    private inner class HydrothermalVent(
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
}
