package hu.pet.ExposedSample.model

import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.IntIdTable

object StarWarsFilms : IntIdTable() {
	val sequelId = integer("sequel_id").uniqueIndex()
	val name = varchar("name", 50)
	val director = varchar("director", 50)
}

class StarWarsFilm(id: EntityID<Int>) : IntEntity(id) {
	companion object : IntEntityClass<StarWarsFilm>(StarWarsFilms)

	var sequelId by StarWarsFilms.sequelId
	var name by StarWarsFilms.name
	var director by StarWarsFilms.director

	// one-to-many
	val ratings by UserRating referrersOn UserRatings.film

	// many-to-many
	val raters by User via UserRatings
}

object Users : IntIdTable() {
	val name = varchar("name", 50)
}

class User(id: EntityID<Int>) : IntEntity(id) {
	companion object : IntEntityClass<User>(Users)

	var name by Users.name

	// one-to-many
	val ratings by UserRating referrersOn UserRatings.user

	// many-to-many
	var ratedFilms by StarWarsFilm via UserRatings
}

object UserRatings : IntIdTable() {
	val value = long("value")
	// many-to-one
	val film = reference("film", StarWarsFilms)
	// many-to-one
	val user = reference("user", Users)
}

class UserRating(id: EntityID<Int>) : IntEntity(id) {
	companion object : IntEntityClass<UserRating>(UserRatings)

	var value by UserRatings.value
	var film by StarWarsFilm referencedOn UserRatings.film // use referencedOn for normal references
	var user by User referencedOn UserRatings.user
}