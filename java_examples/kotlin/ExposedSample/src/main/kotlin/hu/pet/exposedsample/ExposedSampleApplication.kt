package hu.pet.exposedsample

import hu.pet.exposedsample.model.*
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.load
import org.jetbrains.exposed.dao.with
import org.jetbrains.exposed.sql.*
import org.slf4j.LoggerFactory
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import javax.sql.DataSource

@SpringBootApplication
class ExposedSampleApplication {

	val logger = LoggerFactory.getLogger(ExposedSampleApplication::class.java)

	@Bean
	fun databaseSamples(source: DataSource): CommandLineRunner = CommandLineRunner { _ ->
		Database.connect(source)

		withTransaction {
			SchemaUtils.drop(UserRatings, Users, StarWarsFilms, TariffCodes)
			SchemaUtils.create(StarWarsFilms, Users, UserRatings, TariffCodes)

			TariffCode.new {
				code = EntityID("HU", TariffCodes)
			}

			if (StarWarsFilm.count() == 0) {
				val movie = StarWarsFilm.new {
					name = "The Last Jedi"
					sequelId = 8
					director = "Rian Johnson"
				}
				val movie2 = StarWarsFilm.new {
					name = "Rise of the Skywalker"
					sequelId = 9
					director = "J. J. Abrams"
				}

				val user = User.new {
					name = "Peter"
				}
				val user2 = User.new {
					name = "Academia"
				}
				// flush - goes to the database and inserts the record
				// if not present, insert occurs at the end of the withTransaction block where commit happens
				user.flush()
				movie.flush()

				(0..10).forEach {
					val rating = UserRating.new {
						value = 10
						this.user = if (it.rem(2) == 0) user else user2
						this.film = if (it.rem(2) == 0) movie else movie2
					}
					rating.flush()
				}
			}

			val ratings = UserRating.all()

			for (rating in ratings) {
				// many-to-one
				val f = rating.film
				val u = rating.user
				println("${f.director}, ${u.name}")
			}
		}

		withTransaction {
			val huCode = TariffCode.findById("HU")
			logger.info("Hungarian code is: ${huCode?.code ?: "<not found>"}")
			val ratings = UserRating.all().toList()

			for (rating in ratings) {
				val f = rating.film
				val u = rating.user

				// performs new query - many-to-one then one-to-many test
				val userRatings = rating.user.ratings.toList()
				// performs new query - many-to-one then one-to-many test
				val filmRatings = rating.film.ratings.toList()

				println("${f.director}, ${u.name}")
			}
		}

		withTransaction {

			val user = User.find { Users.id.eq(1) }.first()
			// many-to-many test, does not query yet
			val films = user.ratedFilms

			// performs query, select count(*)
			println(films.count())

			// evaluate query now
			val filmList = films.toList()
			filmList.forEach(::println)
			// does not perform new query
			films.toList()
		}

		// DSL with DAO example
		// query in is DSL, and mapping it to DAO classes
		withTransaction {
			val query = Users.innerJoin(UserRatings).innerJoin(StarWarsFilms)
				.slice(Users.columns)
				.select {
					StarWarsFilms.sequelId.eq(8) and UserRatings.value.greaterEq(9L)
				}.withDistinct()

			// performs sql
			val users = User.wrapRows(query).toList()
			// performs sql
			val numberOfUsers = User.wrapRows(query).count()

			users.forEach {
				// performs query
				val ratings = it.ratings.toList()
				// performs query
				val ratingsCount = it.ratings.count()

				// performs query
				val ratedFilms = it.ratedFilms.toList()
				val ratedFilmsCount = it.ratedFilms.count()
			}
		}

		withTransaction {
			fun createQuery(): Query = Users.innerJoin(UserRatings).innerJoin(StarWarsFilms)
				.select {
					StarWarsFilms.sequelId.eq(8) and UserRatings.value.greaterEq(9L)
				}.withDistinct()

			val userQuery = createQuery().adjustSlice { slice(Users.columns) }
			val userRatingsQuery = createQuery().adjustSlice { slice(UserRatings.columns) }
			val ratedFilmsQuery = createQuery().adjustSlice { slice(StarWarsFilms.columns) }

			val users = User.wrapRows(userQuery).toList()
			val userRatings = UserRating.wrapRows(userRatingsQuery).toList()
			val ratedFilms = StarWarsFilm.wrapRows(ratedFilmsQuery).toList()

			println("Users: ${users.count()}, ratings: ${userRatings.count()}, rated films: ${ratedFilms.count()}")
		}

		// eager loading
		withTransaction {
			/*
			NOTE: References that are eagerly loaded are stored inside the Transaction Cache,
			this means that they are not available in other transactions and thus must be loaded and referenced inside the same transaction.
			*/

			// since Exposed uses the transaction cache, to following "performs 2 queries" are only valid if the
			// provided DAO statement is the 1st one in this transaction!

			// performs 2 queries (if this is the 1st statement to execute in this transaction)
			// - SELECT * FROM starwarsfilms WHERE starwarsfilms.id = 1
			// - SELECT * FROM users INNER JOIN userratings ON userratings.`user` = users.id WHERE userratings.film = 1
			val filWithRaters = StarWarsFilm.findById(1)?.load(StarWarsFilm::raters)

			// performs 2 queries (if this is the 1st statement to execute in this transaction)
			// - SELECT * FROM starwarsfilms
			// - SELECT * FROM users INNER JOIN userratings ON userratings.`user` = users.id WHERE userratings.film IN (1, 2)
			val filmsWithRaters = StarWarsFilm.all().with(StarWarsFilm::raters)

			// performs 2 queries (if this is the 1st statement to execute in this transaction)
			// - SELECT * FROM starwarsfilms
			// - SELECT * FROM userratings WHERE userratings.film IN (1, 2)
			val filmsWithRatings = StarWarsFilm.all().with(StarWarsFilm::ratings)
		}
	}
}

fun main(args: Array<String>) {
	runApplication<ExposedSampleApplication>(*args)
}
