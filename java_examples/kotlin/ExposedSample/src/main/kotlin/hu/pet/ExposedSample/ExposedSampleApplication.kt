package hu.pet.ExposedSample

import hu.pet.ExposedSample.model.*
import org.jetbrains.exposed.sql.*
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import javax.sql.DataSource

@SpringBootApplication
class ExposedSampleApplication {

	@Bean
	fun databaseSamples(source: DataSource): CommandLineRunner = CommandLineRunner { _ ->
		Database.connect(source)

		transactionWithLogging {
			SchemaUtils.drop(UserRatings, Users, StarWarsFilms)
			SchemaUtils.create(StarWarsFilms, Users, UserRatings)

			if (StarWarsFilm.count() == 0) {
				val movie = StarWarsFilm.new {
					name = "The Last Jedi"
					sequelId = 8
					director = "Rian Johnson"
				}
				val user = User.new {
					name = "Peter"
				}
				val user2 = User.new {
					name = "Academia"
				}
				user.flush()
				movie.flush()

				(0..10).forEach {
					val rating = UserRating.new {
						value = 10
						this.user = if (it.rem(2) == 0) user else user2
						this.film = movie
					}
					rating.flush()
				}
			}

			val ratings = UserRating.all()

			for (rating in ratings) {
				val f = rating.film
				val u = rating.user
				println("${f.director}, ${u.name}")
			}
		}

		transactionWithLogging {
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

		transactionWithLogging {

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
		transactionWithLogging {
			val query = Users.innerJoin(UserRatings).innerJoin(StarWarsFilms)
				.slice(Users.columns)
				.select {
					StarWarsFilms.sequelId.eq(8) and UserRatings.value.greaterEq(9L)
				}.withDistinct()

			val users = User.wrapRows(query).toList()
			// performs sql
			val nmberOFUsers = User.wrapRows(query).count()

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

		transactionWithLogging {
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
	}
}

fun main(args: Array<String>) {
	runApplication<ExposedSampleApplication>(*args)
}