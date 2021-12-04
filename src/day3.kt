import java.io.File
import kotlin.math.*

fun main(args: Array<String>) {
    if (args.isEmpty()) {
        println("Please enter the file name.")
        return
    }
    val fileName = args[0]
    val powerLevels: List<String> = File("resources/${fileName}")
        .readLines()

    day3part1(powerLevels)
    day3part2(powerLevels)
}

fun day3part1(powerLevels: List<String>) {
    println("\n--- Part 1 ---\n")

    val gammaPowerLevel: MutableList<Int> = mutableListOf()
    val epsilonPowerLevel: MutableList<Int> = mutableListOf()
    for (index in 0 until powerLevels[0].length) {
        val bits: List<Int> = powerLevels.map{ Character.getNumericValue(it[index]) }
        val mostCommonBit = getMostCommonBit(bits, mostCommonlyOccurring = true)

        if (mostCommonBit == 0) {
            gammaPowerLevel.add(0)
            epsilonPowerLevel.add(1)
        } else {
            gammaPowerLevel.add(1)
            epsilonPowerLevel.add(0)
        }
    }

    val gammaResult = decodeBinary(gammaPowerLevel.joinToString(""))
    val epsilonResult = decodeBinary(epsilonPowerLevel.joinToString(""))

    println("Gamma level: $gammaResult")
    println("Epsilon level: $epsilonResult")
    println("Resulting power level is: ${gammaResult*epsilonResult}")
}

fun day3part2(powerLevels: List<String>) {
    println("\n--- Part 2 ---\n")

    val oxygenGeneratorRating = getOxygenGeneratorRating(powerLevels)
    val co2ScrubberRating = getCO2ScrubberRating(powerLevels)

    println("Oxygen Generator Rating: $oxygenGeneratorRating")
    println("CO2 Scrubber Rating: $co2ScrubberRating")
    println("Resulting Life Support Rating: ${oxygenGeneratorRating*co2ScrubberRating}")
}

/**
 * Find the Oxygen Generator rating.
 *
 * This is accomplished through the following process:
 *  - Starting at index 0, find the most common bit in all the numbers.
 *  - Filter out all the numbers that do not have this bit at the index.
 *  - If there are equal numbers of 0's and 1's, act as if the 1 was most common.
 *  - Increment the index.
 *
 *  Continue the above until only one number remains - this is the rating.
 *
 *  @param powerLevels a [List<String>] of the power levels to sort through
 */
fun getOxygenGeneratorRating(powerLevels: List<String>): Int {
    var result: List<String> = powerLevels
    var index = 0

    // Loop through until we only have a single item remaining.
    while (result.size != 1) {
        val bits: List<Int> = result.map{ Character.getNumericValue(it[index]) }
        val commonBit = getMostCommonBit(input = bits, mostCommonlyOccurring = true, default = 1)
        result = result.filter{Character.getNumericValue(it[index]) == commonBit}
        index++
    }

    return decodeBinary(result[0])
}

/**
 * Find the CO2 Scrubber rating.
 *
 * This is accomplished through the following process:
 *  - Starting at index 0, find the least common bit in all the numbers.
 *  - Filter out all the numbers that do not have this bit at the index.
 *  - If there are equal numbers of 0's and 1's, act as if the 0 was most common.
 *  - Increment the index.
 *
 *  Continue the above until only one number remains - this is the rating.
 *
 *  @param powerLevels a [List<String>] of the power levels to sort through
 */
fun getCO2ScrubberRating(powerLevels: List<String>): Int {
    var result: List<String> = powerLevels
    var index = 0

    // Loop through until we only have a single item remaining.
    while (result.size != 1) {
        val bits: List<Int> = result.map{ Character.getNumericValue(it[index]) }
        val commonBit = getMostCommonBit(input = bits, mostCommonlyOccurring = false, default = 0)
        result = result.filter{Character.getNumericValue(it[index]) == commonBit}
        index++
    }

    return decodeBinary(result[0])
}

/**
 * Given an input of a [List<Int>], return the most commonly-occurring (or least commonly-occurring) [Int]. If the number of occurrences are
 * equal, return a default if supplied.
 *
 * @param input the [List<Int>] to search through for the most (or least) common bit
 * @param mostCommonlyOccurring a [Boolean] value indicating whether we should look for the most or least common bit
 * @param default a default [Int] to use if there are equals numbers of each bit
 * @return the most (or least) commonly occurring bit
 * @throws [IllegalStateException] is thrown if no [default] is supplied and there are even numbers of each bit
 */
fun getMostCommonBit(
    input: List<Int>,
    mostCommonlyOccurring: Boolean,
    default: Int? = null
): Int {
    val numZeroes = input.filter{it == 0}.size
    val numOnes = input.filter{it == 1}.size

    return if (numZeroes > numOnes)
        if (mostCommonlyOccurring) 0 else 1
    else if (numOnes > numZeroes)
        if (mostCommonlyOccurring) 1 else 0
    else
        default ?: throw java.lang.IllegalStateException("Occurrences are equal and no default was supplied.")
}

/**
 * Helper method to convert from Binary to Decimal.
 *
 * @param binary the binary [String]
 * @return the decimal [Int] value of the [binary]
 */
fun decodeBinary(binary: String): Int {
    val endIndex = binary.length-1
    var result = 0

    for (i in 0..endIndex) {
        val power = endIndex-i
        val charValue = Character.getNumericValue(binary[i])

        result += (2.toDouble().pow(power)).toInt()*charValue
    }

    return result
}
