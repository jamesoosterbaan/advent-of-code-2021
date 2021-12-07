import java.io.File

fun main(args: Array<String>) {
    if (args.isEmpty()) {
        println("Please enter the file name.")
        return
    }
    val fileName = args[0]
    val input: List<Int> = File("resources/${fileName}").readLines().first { it != "" }.split(",").map { it.toInt() }

    val fishPart1: MutableList<LanternFish> = mutableListOf()
    val fishPart2: MutableList<LanternFish> = mutableListOf()
    input.forEach { fishPart1.add(LanternFish(it)) }
    input.forEach { fishPart2.add(LanternFish(it)) }

    day6part1(fishPart1)
    day6part2(fishPart2)
}

private fun day6part1(fish: MutableList<LanternFish>) {
    val daysToAge = 80
    println("\n--- Part 1 ---\n")
    for (day in 1 .. daysToAge) {
        val newFish: MutableList<LanternFish> = mutableListOf()
        for (element in fish) {
            if (element.ageFish()) newFish.add(LanternFish.newFish())
        }
        fish.addAll(newFish)
    }
    println("\nTotal number of fish is: ${fish.size}")
}

/**
 * Given the rate at which they multiply, for this many days there are too many [LanternFish] to track individually - we eventually OOM. To get
 * around this issue, we can just track the number of fish at each age and update as the days go on.
 */
private fun day6part2(fish: MutableList<LanternFish>) {
    println("\n--- Part 2 ---\n")
    val daysToAge = 256
    val fishAges = mutableMapOf(*(0 .. 8).map { it to 0.toLong() }.toTypedArray())
    fish.forEach{ fishAges[it.getAge()] = (fishAges[it.getAge()] ?: 0) + 1 }

    // Can't think of a terser way to iterate through each value, shift down, and sum all "new fish" on the 6th day.
    for (day in 1 .. daysToAge) {
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

private class LanternFish(private var age: Int) {
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

    companion object {
        fun newFish(): LanternFish = LanternFish(8)
    }
}
