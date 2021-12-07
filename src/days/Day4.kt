package days

import java.io.File
import java.util.LinkedList

class Day4(private val inputFile: File): AdventSolution(inputFile) {
    override fun part1() {
        val input = parseInput(inputFile)
        val draws = input.first
        val gameBoards = input.second

        outer@ for (number in draws) {
            for (gameBoard in gameBoards) {
                gameBoard.markValue(number)
            }

            for (gameBoard in gameBoards) {
                if (gameBoard.isComplete()) {
                    println("We found the winning board! Last draw value was '$number' and the sum of the unmarked " +
                            "slots was '${gameBoard.sumOfUnmarkedSlots()}'.\nThe answer is '${number*gameBoard.sumOfUnmarkedSlots()}'.")
                    break@outer
                }
            }
        }
    }

    override fun part2() {
        val input = parseInput(inputFile)
        val draws = input.first
        val gameBoards = input.second

        val completedBoards: LinkedList<GameBoard> = LinkedList()
        val initialBoards: MutableList<GameBoard> = gameBoards.toMutableList()

        outer@ for (number in draws) {
            for (gameBoard in gameBoards) {
                gameBoard.markValue(number)
            }

            for(gameBoard in gameBoards.filter { !completedBoards.contains(it) }) {
                if (gameBoard.isComplete()) {
                    initialBoards.remove(gameBoard)
                    completedBoards.addLast(gameBoard)
                }
            }

            if (initialBoards.isEmpty()) {
                val sumOfLast = completedBoards.last().sumOfUnmarkedSlots()
                println("We found the last winning board! Last draw value was '$number' and the sum of the unmarked slots was '$sumOfLast'.\nThe answer" +
                        " is '${number * sumOfLast}'.")
                break@outer
            }
        }
    }

    private fun parseInput(file: File) : Pair<List<Int>, List<GameBoard>> {
        val input: List<String> = file.readLines().filter { it != "" }
        val draws: List<Int> = input[0].split(",").map { it.toInt() }
        val gameBoardInputs: List<List<Int>> = input.slice(1 until input.size).map { line -> line.trim().split("\\s+".toRegex()).map { it.toInt() } }
        val gameBoards: MutableList<GameBoard> = mutableListOf()

        for (index in 0 until (gameBoardInputs.size)/5) {
            val gameBoard = GameBoard(gameBoardInputs.slice(index*5 until index*5 + 5))
            gameBoards.add(gameBoard)
        }

        return Pair(draws, gameBoards)
    }

    /**
     * A class to model the bingo game boards. It's a nested list of [GameBoardSlot]'s which contain the value of each slot and whether that
     * number has been called (whether it is "marked" or not).
     *
     * @param initialState a [List<List<Int>>] of the initial values to populate the game board with
     */
    inner class GameBoard(initialState: List<List<Int>>) {
        private val slots: MutableList<MutableList<GameBoardSlot>> = initialState
            .map { it.map { value -> GameBoardSlot(value) }.toMutableList() }.toMutableList()

        /**
         * Given an input value of [Int], mark all slots that have this value.
         *
         * @param value the [Int] to look for
         */
        fun markValue(value: Int) {
            for (row in slots) {
                for (slot in row) {
                    if (slot.value == value) {
                        slot.markSlot()
                    }
                }
            }
        }

        /**
         * Return the sum of all the [GameBoardSlot]'s on the [GameBoard] that are unmarked.
         */
        fun sumOfUnmarkedSlots(): Int {
            return slots.flatten().filter { !it.isMarked() }.map { it.value }.fold(initial = 0) {accumulator, item -> accumulator + item }
        }

        /**
         * Check if the current [GameBoard] is completed (i.e. either a full row or column is marked).
         */
        fun isComplete(): Boolean {
            return searchForCompleteRow() || searchForCompleteColumn()
        }

        fun printGameBoard() {
            println("--- Start of GameBoard ---")
            for (row in slots) {
                for (slot in row) {
                    print("${slot.value} ")
                }
                print("\n")
            }
            println("--- End of GameBoard ---")
        }

        fun printMarkedValues() {
            val markedItems: MutableList<GameBoardSlot> = mutableListOf()
            for (row in slots) {
                for (slot in row) {
                    if (slot.isMarked()) markedItems.add(slot)
                }
            }

            println(markedItems.joinToString(", "))
        }

        /**
         * Search through the [GameBoard] and check if a row of [GameBoardSlot]'s are all marked.
         */
        private fun searchForCompleteRow(): Boolean {
            for (row in slots) {
                if (!row.map { it.isMarked() }.contains(false)) return true
            }

            return false
        }

        /**
         * Search through the [GameBoard] and check if a column of [GameBoardSlot]'s are all marked.
         */
        private fun searchForCompleteColumn(): Boolean {
            for (index in 0 until slots[0].size) {
                if (!slots.map { it[index].isMarked() }.contains(false)) return true
            }

            return false
        }

        /**
         * A class to model each slot on the [GameBoard] - it's instantiated with a value, and it's initially not marked.
         *
         * @param value the [Int] value of the slot
         */
        inner class GameBoardSlot(val value: Int) {
            private var marked = false

            fun markSlot() {
                marked = true
            }

            fun isMarked(): Boolean {
                return marked
            }

            override fun toString(): String {
                return "$value $marked"
            }
        }
    }
}
