package hu.pet.ExposedSample

import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.transactions.transaction
import java.sql.Connection

inline fun <T> withTransaction(crossinline statement: Transaction.() -> T): T =
	withTransaction(Connection.TRANSACTION_READ_COMMITTED, statement)

inline fun <T> withTransaction(transactionIsolation: Int, crossinline statement: Transaction.() -> T): T =
	transaction(transactionIsolation, 1) {
		statement()
	}
