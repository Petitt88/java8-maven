package com.pet.webfluxthymeleaf.infrastructure

import com.fasterxml.jackson.databind.ObjectMapper
import com.pet.webfluxthymeleaf.infrastructure.kotlin.aRead
import java.nio.ByteBuffer
import java.nio.channels.AsynchronousFileChannel


suspend inline fun <reified T> AsynchronousFileChannel.parseJson(mapper: ObjectMapper): T {

	val bytes = ByteArray(this.size().toInt())
	val buffer = ByteBuffer.allocate(4096)
	var pos = 0L

	while (true) {
		val bytesRead = this.aRead(buffer, pos)
		if (bytesRead == -1)
			break

		buffer.array()
			.take(bytesRead)
			.forEachIndexed { index, value -> bytes[index + pos.toInt()] = value }

		pos += bytesRead
		buffer.flip()
	}

	//mapper.readValue(bytes, Array<Movie>::class.java)
	//mapper.readValue(bytes, object : TypeReference<List<Movie>>() {})

	val result = mapper.readValue(bytes, T::class.java)
	return result
}