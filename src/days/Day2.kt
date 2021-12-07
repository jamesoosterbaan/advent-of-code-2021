package days

import java.io.File

class Day2(private val inputFile: File): AdventSolution(inputFile) {
    override fun part1() {
        val instructions = parseInputFile(inputFile)
        val currentPosition = Position(0,0)
        for (instruction in instructions) {
            when (instruction.direction) {
                "forward" -> currentPosition.increaseHorizontal(instruction.distance)
                "down" -> currentPosition.increaseDepth(instruction.distance)
                "up" -> currentPosition.decreaseDepth(instruction.distance)
                else -> throw java.lang.IllegalArgumentException("Unknown direction '${instruction.direction}'.")
            }
        }
        println("Final position is: $currentPosition.")
        println("Product of horizontal and depth is: ${currentPosition.getProduct()}.")
    }

    override fun part2() {
        val instructions = parseInputFile(inputFile)
        val currentPosition = AimAdjustedPosition(0,0, 0)
        for (instruction in instructions) {
            when (instruction.direction) {
                "forward" -> currentPosition.increaseHorizontal(instruction.distance)
                "down" -> currentPosition.increaseAim(instruction.distance)
                "up" -> currentPosition.decreaseAim(instruction.distance)
                else -> throw java.lang.IllegalArgumentException("Unknown direction '${instruction.direction}'.")
            }
        }
        println("Final aim-adjusted position is: $currentPosition.")
        println("Product of horizontal and depth is: ${currentPosition.getProduct()}.")
    }

    private fun parseInputFile(file: File): List<Instruction> {
        return file
            .readLines()
            .map{ line -> line.split(" ") }
            .map{ Instruction(it[0], it[1].toInt()) }
    }

    data class Instruction(val direction: String, val distance: Int)

    /**
     * Class to track the position of the submarine, using only horizontal and depth modifiers.
     */
    open inner class Position(
        initialHorizontal: Int,
        initialDepth: Int
    ) {
        private var horizontal: Int = initialHorizontal
        private var depth: Int = initialDepth

        open fun increaseHorizontal(distance: Int) {
            this.horizontal += distance
        }

        fun increaseDepth(distance: Int) {
            this.depth += distance
        }

        fun decreaseDepth(distance: Int) {
            this.depth -= distance
        }

        /**
         * Returns the product of the horizontal and depth positions.
         */
        fun getProduct(): Int {
            return horizontal * depth
        }

        override fun toString(): String {
            return "Horizontal: $horizontal, Depth: $depth"
        }
    }

    /**
     * Class to track the position of the submarine, using horizontal and aim modifiers (the depth is modified through a combination of horizontal
     * movement and the current aim of the submarine).
     *
     * @param initialHorizontal The initial horizontal position of the submarine
     * @param initialDepth The initial depth of the submarine
     * @param initialAim The initial aim of the submarine
     */
    inner class AimAdjustedPosition(
        private val initialHorizontal: Int,
        private val initialDepth: Int,
        private val initialAim: Int
    ): Position(initialHorizontal, initialDepth) {
        private var aim: Int = initialAim

        override fun increaseHorizontal(distance: Int) {
            super.increaseHorizontal(distance)
            super.increaseDepth(distance * aim)
        }

        fun increaseAim(amount: Int) {
            this.aim += amount
        }

        fun decreaseAim(amount: Int) {
            this.aim -= amount
        }

        override fun toString(): String {
            return "${super.toString()}, Aim: $aim"
        }
    }
}
