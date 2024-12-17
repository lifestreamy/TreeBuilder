package com.github.treeBuilder

/** An immutable representation of a path, supporting convenient concatenation
 *  Todo not used right now
 * */
@ConsistentCopyVisibility
data class Path internal constructor(val path: IntArray) {
    companion object {
        val ROOT: Path = Path(intArrayOf())
    }

    infix operator fun plus(other: Path): Path = Path(path + other.path)

    override fun equals(other: Any?): Boolean = when {
        (this === other) -> true
        (javaClass != other?.javaClass) -> false
        else -> path.contentEquals((other as Path).path)
    }

    override fun hashCode(): Int = path.contentHashCode()

}

@Deprecated("Not yet useful", level = DeprecationLevel.HIDDEN)
fun path(vararg indices: Int): Path = Path(indices)

typealias IntPath = List<Int> // can use this typealias instead of List<Int> where needed