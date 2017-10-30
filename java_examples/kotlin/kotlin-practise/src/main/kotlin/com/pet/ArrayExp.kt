package com.pet

import java.util.*

class Card(val number: Int, var type: String) {

	constructor() : this(0, "") {

	}

	constructor(name: String) : this(0, "") {

	}

	init {
		// can read only from primary ctor
	}
}

class Deck {

	val cards: Array<Card> = Array(52, this::createCard)

	var cardsInDeck: MutableList<Card> = this.cards.toMutableList()
	//var cardsInDeck2: List<Card> = this.cards.asList()

	fun shuffle(): Unit {
		Collections.shuffle(this.cardsInDeck)
	}

	fun removeCard(tappedIndex: Int) {
		for (i in tappedIndex..this.cards.lastIndex)
			this.cardsInDeck.removeAt(tappedIndex)

		this.cardsInDeck.forEachIndexed { index, card -> println("$index, $card") }

		val card = this.cardsInDeck[0]
		this.cardsInDeck.subList(0, 10)
		this.cardsInDeck = mutableListOf()

		for (i in 4 downTo 0)
			println(i)
	}

	private fun createCard(number: Int): Card = Card(number, "")

	fun contains(number: Int): Boolean = if (this.cards.any { it.number == number }) true else false
}