package days

import java.io.File

class Day6(private val inputFile: File): AdventSolution(inputFile) {
    override fun part1() {
        val fish: MutableList<LanternFish> = mutableListOf()
        parseInputFile(inputFile).forEach { fish.add(LanternFish(it)) }
        val daysToAge = 80

        for (day in 1 .. daysToAge) {
            println("Simulating day: $day")
            val newFish: MutableList<LanternFish> = mutableListOf()
            for (element in fish) {
                if (element.ageFish()) newFish.add(LanternFish(0))
            }
            fish.addAll(newFish)
        }

        println("\nTotal number of fish is: ${fish.size}")
    }

    /**
     * Given the rate at which they multiply, for this many days there are too many [LanternFish] to track individually - we eventually OOM. To get
     * around this issue, we can just track the number of fish at each age and update as the days go on.
     */
    override fun part2() {
        val fish: MutableList<LanternFish> = mutableListOf()
        parseInputFile(inputFile).forEach { fish.add(LanternFish(it)) }
        val daysToAge = 256

        val fishAges = mutableMapOf(*(0 .. 8).map { it to 0.toLong() }.toTypedArray())
        fish.forEach{ fishAges[it.getAge()] = (fishAges[it.getAge()] ?: 0) + 1 }

        // Can't think of a terser way to iterate through each value, shift down, and sum all "new fish" on the 6th day.
        for (day in 1 .. daysToAge) {
            println("Simulating day: $day")
            val new8 = fishAges[0] ?: 0
            val new7 = fishAges[8] ?: 0
            val new6 = fishAges.filterKeys { it == 0 || it == 7 }.map { it.value }.sum()
            val new5 = fishAges[6] ?: 0
            val new4 = fishAges[5] ?: 0
            val new3 = fishAges[4] ?: 0
            val new2 = fishAges[3] ?: 0
            val new1 = fishAges[2] ?: 0
            val new0 = fishAges[1] ?: 0
            fishAges[8] = new8
            fishAges[7] = new7
            fishAges[6] = new6
            fishAges[5] = new5
            fishAges[4] = new4
            fishAges[3] = new3
            fishAges[2] = new2
            fishAges[1] = new1
            fishAges[0] = new0
        }

        println("\nTotal number of fish is: ${fishAges.map { it.value }.sum()}")
    }

    private fun parseInputFile(file: File): List<Int> {
        return file.readLines().first { it != "" }.split(",").map { it.toInt() }
    }

    private inner class LanternFish(private var age: Int) {
        fun ageFish(): Boolean {
            return if (age == 0) {
                age = 6
                true
            } else {
                age--
                false
            }
        }

        fun getAge(): Int {
            return age
        }

        override fun toString(): String {
            return "Age: $age"
        }
    }
}
