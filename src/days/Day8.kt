package days

import java.io.File
import java.lang.IllegalStateException

class Day8(private val inputFile: File): AdventSolution(inputFile) {
    override fun part1() {
        val input = parseInput(inputFile)
        var counter = 0
        input.map { it.outputValues }.flatten().forEach {
            if (arrayOf(TWO_SEGMENTS, THREE_SEGMENTS, FOUR_SEGMENTS, SEVEN_SEGMENTS).contains(it.length)) counter++
        }

        println("Found '$counter' elements that have a unique number of segments.")
    }

    override fun part2() {
        val input = parseInput(inputFile)
        val decodedOutputs: MutableList<Int> = mutableListOf()
        input.forEach {
            val inputValues = it.inputValues
            val mappings = decodeMapping(inputValues)
            var output = ""
            it.outputValues.forEach { code ->
                output = "$output${determineDigit(code, mappings)}"
            }
            decodedOutputs.add(output.toInt())
        }
        println("Sum of decoded outputs is: ${decodedOutputs.sum()}")
    }

    private fun parseInput(file: File): List<PuzzleInput> {
        val input = file.readLines().filter { it != "" }
        val result = mutableListOf<PuzzleInput>()
        input.forEach {
            val inputValues = it.split("|")[0].trim().split(" ")
            val outputValues = it.split("|")[1].trim().split(" ")
            result.add(PuzzleInput(inputValues, outputValues))
        }
        return result
    }

    private data class PuzzleInput(val inputValues: List<String>, val outputValues: List<String>)

    /**
     * This method follows an algorithm to determine the mis-matched signals. It requires that each number
     * is passed as an input once, and slowly iterates through and solves them.
     */
    private fun decodeMapping(input: List<String>): Map<String, String> {
        val potentialOptions: MutableMap<String, MutableSet<String>> = mutableMapOf()
        val result: MutableMap<String, String> = mutableMapOf()

        val one: Set<String> = input.first { it.length == TWO_SEGMENTS }.toCharArray().map { it.toString() }.toSet()
        val four: Set<String> = input.first { it.length == FOUR_SEGMENTS }.toCharArray().map { it.toString() }.toSet()
        val seven: Set<String> = input.first { it.length == THREE_SEGMENTS }.toCharArray().map { it.toString() }.toSet()
        val eight: Set<String> = input.first { it.length == SEVEN_SEGMENTS }.toCharArray().map { it.toString() }.toSet()

        // We know that ONE and SEVEN share two segments, and the third for the SEVEN will be the 'a' segment.
        result["a"] = seven.subtract(one).first()
        // We know the two possible options for segments 'c' and 'f' (it's the two ONE segments).
        potentialOptions["c"] = one.toMutableSet()
        potentialOptions["f"] = one.toMutableSet()
        // We also know the two options for segments 'b' and 'd' - it's the FOUR minus the ONE segments.
        potentialOptions["b"] = four.subtract(one).toMutableSet()
        potentialOptions["d"] = four.subtract(one).toMutableSet()
        // Then we know the options for 'e' and 'g' - it's the EIGHT minus all the other known numbers.
        val intersect = one.union(four).union(seven)
        potentialOptions["e"] = eight.subtract(intersect).toMutableSet()
        potentialOptions["g"] = eight.subtract(intersect).toMutableSet()

        // We can then guarantee which the 'f' segment is - it's the intersection of the '6' segments (ZERO, SIX, NINE) and ONE.
        val zerosAndSixesAndNines: List<Set<String>> = input
            .filter { it.length == SIX_SEGMENTS }
            .map { it.toCharArray() }
            .map { chars -> chars.map { it.toString() }.toSet() }
        var sixDigitUnion: MutableSet<String> = zerosAndSixesAndNines.first().toMutableSet()
        input.filter { it.length == SIX_SEGMENTS }
            .map { it.toCharArray() }
            .forEach { charArray ->
                val set: Set<String> = charArray.map { it.toString() }.toSet()
                sixDigitUnion = sixDigitUnion.intersect(set).toMutableSet()
            }
        result["f"] = one.intersect(sixDigitUnion).first()
        if (result["f"] == null) throw IllegalStateException("Values for 'f' are not confirmed when they should be.")
        // This leaves us with a single option for 'c'
        result["c"] = one.minus(result["f"]!!).first()
        if (result["c"] == null) throw IllegalStateException("Values for 'c' are not confirmed when they should be.")

        // By this point, we have guaranteed 'a', 'c', and 'f'.

        // Now if we filter out the SIXes (it will not contain the 'c'), we can get the ZEROs and NINEs
        val zerosAndNines: List<MutableSet<String>> = zerosAndSixesAndNines
            .filter { it.contains(potentialOptions["c"]!!.first()) }
            .map { it.toMutableSet() }

        var zeroAndNineIntersect = zerosAndNines.first().toSet()
        zerosAndNines.forEach { zeroAndNineIntersect = zeroAndNineIntersect.intersect(it) }
        // If we minus the SEVEN and FOUR, we can get the value for 'g'
        result["g"] = zeroAndNineIntersect.subtract(seven).subtract(four).first()
        if (result["g"] == null) throw IllegalStateException("Values for 'g' are not confirmed when they should be.")
        result["e"] = potentialOptions["e"]!!.minus(result["g"]!!).first()
        if (result["e"] == null) throw IllegalStateException("Values for 'e' are not confirmed when they should be.")

        // Lastly, if we take the intersection of the TWOs, THREEs, FOURs, and FIVEs, we will have the 'd', from which we can then infer 'b'.
        var twoThreeFourFiveIntersect = four
        input.filter { it.length == FIVE_SEGMENTS }
            .map { it.toCharArray() }
            .map { chars -> chars.map { it.toString() }.toSet() }
            .forEach { twoThreeFourFiveIntersect = twoThreeFourFiveIntersect.intersect(it) }

        result["d"] = twoThreeFourFiveIntersect.first()
        if (result["d"] == null) throw IllegalStateException("Values for 'd' are not confirmed when they should be.")
        result["b"] = potentialOptions["b"]!!.minus(result["d"]!!).first()
        if (result["b"] == null) throw IllegalStateException("Values for 'b' are not confirmed when they should be.")

        return result.entries.associateBy({ it.value }) { it.key }
    }

    private fun determineDigit(code: String, mapping: Map<String, String>): String {
        val codeChars = code.toCharArray().map { it.toString() }
        val decodedNumber: MutableSet<String> = mutableSetOf()
        codeChars.forEach { decodedNumber.add(mapping[it]!!) }
        return when (decodedNumber.toSet()) {
            SEGMENTS_FOR_EIGHT -> "8"
            SEGMENTS_FOR_ZERO -> "0"
            SEGMENTS_FOR_SIX -> "6"
            SEGMENTS_FOR_NINE -> "9"
            SEGMENTS_FOR_TWO -> "2"
            SEGMENTS_FOR_THREE -> "3"
            SEGMENTS_FOR_FIVE -> "5"
            SEGMENTS_FOR_FOUR -> "4"
            SEGMENTS_FOR_SEVEN -> "7"
            SEGMENTS_FOR_ONE -> "1"
            else -> throw IllegalStateException("Could not find digit for $code")
        }
    }

    companion object {
        private const val TWO_SEGMENTS = 2
        private const val THREE_SEGMENTS = 3
        private const val FOUR_SEGMENTS = 4
        private const val FIVE_SEGMENTS = 5
        private const val SIX_SEGMENTS = 6
        private const val SEVEN_SEGMENTS = 7

        private val SEGMENTS_FOR_ZERO = setOf("a", "b", "c", "e", "f", "g")
        private val SEGMENTS_FOR_ONE = setOf("c", "f")
        private val SEGMENTS_FOR_TWO = setOf("a", "c", "d", "e", "g")
        private val SEGMENTS_FOR_THREE = setOf("a", "c", "d", "f", "g")
        private val SEGMENTS_FOR_FOUR = setOf("b", "c", "d", "f")
        private val SEGMENTS_FOR_FIVE = setOf("a", "b", "d", "f", "g")
        private val SEGMENTS_FOR_SIX = setOf("a", "b", "d", "e", "f", "g")
        private val SEGMENTS_FOR_SEVEN = setOf("a", "c", "f")
        private val SEGMENTS_FOR_EIGHT = setOf("a", "b", "c", "d", "e", "f", "g")
        private val SEGMENTS_FOR_NINE = setOf("a", "b", "c", "d", "f", "g")
    }
}
