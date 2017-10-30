package com.pet

import javax.swing.tree.TreeNode

// uses reflection
fun <T> TreeNode.findParentOfType(clazz: Class<T>): T? {
	var p = parent
	while (p != null && !clazz.isInstance(p)) {
		p = p.parent
	}

	println(clazz.name)

	@Suppress("UNCHECKED_CAST")
	return p as T?
}

// thanks to reified, no reflection has to be used
inline fun <reified T> TreeNode.findParentOfType(): T? {
	var p = parent
	while (p != null && p !is T) {
		p = p.parent
	}

	println(T::class.simpleName)
	println(T::class.java.simpleName)

	return p as T?
}