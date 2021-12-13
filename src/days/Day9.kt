package days

import java.io.File
import java.util.Stack

class Day9(private val inputFile: File): AdventSolution(inputFile) {
    override fun part1() {
        val input = parseInput(inputFile)
        val lowPoints = determineLowPointLocations(input)
        val result = lowPoints.map { input[it.y][it.x] }.sumOf { it+1 }

        println("Sum of risk levels: $result")
    }

    override fun part2() {
        val input = parseInput(inputFile)
        val lowPointLocations = determineLowPointLocations(input)

        // Now we need to step through each low point and build up the basins.
        val basins: MutableList<Set<Coordinate>> = mutableListOf()
        lowPointLocations.forEach { lowPoint ->
            basins.add(determineBasin(input, lowPoint))
        }

        var result: Long

        basins.sortedBy { it.size }.reversed().let { result = it[0].size.toLong() * it[1].size.toLong() * it[2].size.toLong() }

        println("Product of the sizes of the three largest basins: $result")
    }

    private fun parseInput(file: File): List<List<Int>> {
        val input: List<List<Int>> = file
            .readLines()
            .map { it.toCharArray() }
            .map { chars ->
                chars.map {
                    Character.getNumericValue(it)
                }
            }

        return input
    }

    private fun determineLowPointLocations(map: List<List<Int>>): List<Coordinate> {
        val lowPoints = mutableListOf<Coordinate>()
        for (y in map.indices) {
            for (x in map[y].indices) {
                val pointToCompare = map[y][x]
                val neighbouringPoints = mutableListOf<Int>()
                if (x-1 >= 0) neighbouringPoints.add(map[y][x - 1])
                if (x+1 < map[y].size) neighbouringPoints.add(map[y][x + 1])
                if (y-1 >= 0) neighbouringPoints.add(map[y - 1][x])
                if (y+1 < map.size) neighbouringPoints.add(map[y + 1][x])
                if (neighbouringPoints.none { it <= pointToCompare }) {
                    lowPoints.add(Coordinate(x,y))
                }
            }
        }

        return lowPoints
    }

    private fun determineBasin(map: List<List<Int>>, lowPoint: Coordinate): Set<Coordinate> {
        val basin: MutableSet<Coordinate> = mutableSetOf()
        val stack = Stack<Coordinate>()
        stack.push(lowPoint)
        while (!stack.isEmpty()) {
            val coordinate = stack.pop()
            basin.add(coordinate)
            val x = coordinate.x
            val y = coordinate.y
            if (x-1 >= 0 && map[y][x-1] != 9 && !basin.contains(Coordinate(x-1, y))) {
                stack.push(Coordinate(x-1, y))
            }
            if (x+1 < map[y].size && map[y][x+1] != 9 && !basin.contains(Coordinate(x+1, y))) {
                stack.push(Coordinate(x+1, y))
            }
            if (y-1 >= 0 && map[y-1][x] != 9 && !basin.contains(Coordinate(x, y-1))) {
                stack.push(Coordinate(x, y-1))
            }
            if (y+1 < map.size && map[y+1][x] != 9 && !basin.contains(Coordinate(x, y+1))) {
                stack.push(Coordinate(x, y+1))
            }
        }

        return basin.toSet()
    }

    private fun printLowPoints(map: List<List<Int>>, lowPoints: List<Coordinate>) {
        for(y in map.indices) {
            for(x in map[y].indices) {
                if (lowPoints.contains(Coordinate(x,y))) print("*${map[y][x]}*")
                else print(" ${map[y][x]} ")
            }
            println()
        }
    }

    data class Coordinate(val x: Int, val y: Int)
}
