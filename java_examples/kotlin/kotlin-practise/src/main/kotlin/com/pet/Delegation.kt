package com.pet

import kotlin.reflect.KProperty

class DelegateExample {
	var p: String by Delegate()
	val p2: Int by Delegate2 { 3 }
}

class Delegate {
	operator fun getValue(thisRef: Any?, property: KProperty<*>): String {
		return "$thisRef, thank you for delegating '${property.name}' to me!"
	}

	operator fun setValue(thisRef: Any?, property: KProperty<*>, value: String) {
		println("$value has been assigned to '${property.name} in $thisRef.'")
	}
}

class Delegate2<out T : Int>(val initializer: () -> T) {
	val value: T
		get() = this.initializer()
	//set(value) = { field = value }

	//operator fun getValue(thisRef: Any?, property: KProperty<*>): T = value
}

private inline operator fun <T : Int> Delegate2<T>.getValue(thisRef: Any?, property: KProperty<*>): Int = value