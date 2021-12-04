import java.io.File

fun main(args: Array<String>) {
    if (args.isEmpty()) {
        println("Please enter the file name.")
        return
    }
    val fileName = args[0]
    val instructions: List<Instruction> = File("resources/${fileName}")
        .readLines()
        .map{ line -> line.split(" ") }
        .map{ Instruction(it[0], it[1].toInt()) }

    day2part1(instructions)
    day2part2(instructions)
}

fun day2part1(instructions: List<Instruction>) {
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

fun day2part2(instructions: List<Instruction>) {
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

data class Instruction(val direction: String, val distance: Int)

/**
 * Class to track the position of the submarine, using only horizontal and depth modifiers.
 */
open class Position(
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
class AimAdjustedPosition(
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
