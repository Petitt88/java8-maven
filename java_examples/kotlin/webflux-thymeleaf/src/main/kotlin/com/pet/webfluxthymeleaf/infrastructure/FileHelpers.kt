package com.pet.webfluxthymeleaf.infrastructure

import com.fasterxml.jackson.databind.ObjectMapper
import kotlinx.coroutines.experimental.nio.aRead
import java.nio.ByteBuffer
import java.nio.channels.AsynchronousFileChannel


inline suspend fun <reified T> AsynchronousFileChannel.readToEnd(mapper: ObjectMapper): T {
	val bytes = ByteArray(this.size().toInt())

	val buf = ByteBuffer.allocate(4096)
	var pos = 0L
	while (true) {

		val bytesRead = this.aRead(buf, pos)
		if (bytesRead == -1)
			break;

		buf.array()
				.take(bytesRead)
				.forEachIndexed { index, value ->
					println(index + pos.toInt())
					bytes[index + pos.toInt()] = value
				}

		pos += bytesRead
	}

	val result = mapper.readValue(bytes, T::class.java)
	return result
}