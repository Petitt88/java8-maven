package com.pet

fun main(args: Array<String>) {
	val numBackUpPairsByRegion = mapOf("Africa" to 4, "Antarctica" to 7, "Asia" to 3, "Australia" to 2, "Europe" to 2, "North America" to 3, "South America" to 4)
	val region = "Antarctica"
	val area = 14000000
	val numPantsPerPanda = 1 + numBackUpPairsByRegion[region] as Int

	// variable to function
	val howManyPants = fun(numPandas: Int) = numPandas + numPantsPerPanda
	// variable to function
	val howManyPants2 = fun(numPandas: Int) { numPandas + numPantsPerPanda }
	// variable to lambda
	val howManyPantsLambda = { numPandas: Int -> numPandas + numPantsPerPanda }

	// local function
	fun countPandas(area: Int): Int = if (area > 3000) area / 25 else area / 18
	// anonymous object
	val pantsCounter = object {
		fun count(numPandas: Int, convertPandasToPants: (Int) -> Int): Int = convertPandasToPants(numPandas)
	}
	println(pantsCounter.count(countPandas(area), howManyPants))

	println(pantsCounter::class.java.simpleName)
}


fun play(args: Array<String>) {
	val favNums = arrayOf(11, 38, 74, 15)
	println("${favNums.size}")

	for (i in 3 downTo 0) {
		println(i)
	}

	val list = mutableListOf(3, 4, 7, 2, 4, 9, 0, 1).subList(3, 4)

	// true
	println("aa" == "aa")
	// false
	println("aa" === "aa")

	val p1 = Person("Peter", 28)
	val p2 = Person("Peter", 28)
	// false
	println(p1 == p2)
	// false
	println(p1 === p2)

	val dp1 = DataPerson("Peter", 28)
	val dp2 = DataPerson("Peter", 28)
	// true - equals method is overridden by Kotlin for data classes and checks whether all properties are the same
	println(dp1 == dp2)
	// false - checks reference equality
	println(dp1 === dp2)
}

class Person(val name: String, val age: Int)
data class DataPerson(val name: String, val age: Int) {

	override fun toString(): String = if (this.age > 0) "$name-$age" else "${name.toString()}"
}

fun countPandas(area: Int): Int = if (area > 3000) area / 25 else area / 18