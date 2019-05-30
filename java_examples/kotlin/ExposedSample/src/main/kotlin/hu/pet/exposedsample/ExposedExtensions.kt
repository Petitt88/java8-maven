package hu.pet.exposedsample

import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IdTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.transactions.transaction
import java.sql.Connection

inline fun <T> withTransaction(crossinline statement: Transaction.() -> T): T =
	withTransaction(Connection.TRANSACTION_READ_COMMITTED, statement)

inline fun <T> withTransaction(transactionIsolation: Int, crossinline statement: Transaction.() -> T): T =
	transaction(transactionIsolation, 1) {
		statement()
	}


open class StringIdTable(length: Int, name: String = "", columnName: String = "id") : IdTable<String>(name) {
	override var id: Column<EntityID<String>> = varchar(columnName, length).primaryKey().entityId()
}

abstract class StringEntity(id: EntityID<String>) : Entity<String>(id)
abstract class StringEntityClass<out E : StringEntity>(table: IdTable<String>, entityType: Class<E>? = null) : EntityClass<String, E>(table, entityType)