import days.*
import java.io.File
import java.lang.IllegalArgumentException

object Main {
    @JvmStatic
    fun main(args: Array<String>) {
        if (args.isEmpty()) {
            println("Welcome to James' Advent of Code 2021 solutions!\n\n-----\n")
            println("Pass in the day you'd like to run as the argument and it'll print out the result.")
            return
        } else if (args.size > 1) {
            println("Too many arguments passed in - the only argument needed is the day number as an Integer.")
            return
        } else if (args[0].toIntOrNull()?.let { true } == false) {
            println("Input '${args[0]}' was not an Integer - please pass in only an Integer.")
            return
        }

        val dayToRun = args[0].toInt()
        val puzzleInput = File("resources/day$dayToRun.txt")

        val adventSolution: AdventSolution
        when (dayToRun) {
            1 -> adventSolution = Day1(puzzleInput)
            2 -> adventSolution = Day2(puzzleInput)
            3 -> adventSolution = Day3(puzzleInput)
            4 -> adventSolution = Day4(puzzleInput)
            5 -> adventSolution = Day5(puzzleInput)
            6 -> adventSolution = Day6(puzzleInput)
            7 -> adventSolution = Day7(puzzleInput)
            8 -> adventSolution = Day8(puzzleInput)
            9 -> adventSolution = Day9(puzzleInput)
            10 -> adventSolution = Day10(puzzleInput)
            11 -> adventSolution = Day11(puzzleInput)
            12 -> adventSolution = Day12(puzzleInput)
            13 -> adventSolution = Day13(puzzleInput)
            14 -> adventSolution = Day14(puzzleInput)
            15 -> adventSolution = Day15(puzzleInput)
            16 -> adventSolution = Day16(puzzleInput)
            17 -> adventSolution = Day17(puzzleInput)
            18 -> adventSolution = Day18(puzzleInput)
            19 -> adventSolution = Day19(puzzleInput)
            20 -> adventSolution = Day20(puzzleInput)
            21 -> adventSolution = Day21(puzzleInput)
            22 -> adventSolution = Day22(puzzleInput)
            23 -> adventSolution = Day23(puzzleInput)
            24 -> adventSolution = Day24(puzzleInput)
            25 -> adventSolution = Day25(puzzleInput)
            else -> throw IllegalArgumentException("Incorrect day provided: '$dayToRun'. Must be between 1-25 (inclusive).")
        }

        println("\n\n--- Part 1 ---\n\n")
        adventSolution.part1()
        println("\n\n--- Part 2 ---\n\n")
        adventSolution.part2()
    }
}
