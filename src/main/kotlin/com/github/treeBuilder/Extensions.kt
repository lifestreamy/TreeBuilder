package com.github.treeBuilder

import com.github.treeBuilder.dsl.NodeList

/**
 *  Get all leaves (nodes that have no children) from below this node.
 *  @return `this` node if it has no children.
 */
fun <T> TreeNode<T>.getAllLeaves(): NodeList<T> {
    var leafList = listOf<TreeNode<T>>()
    children.ifEmpty { return listOf(this) }.forEach { child ->
        leafList = leafList + child.getAllLeaves()
    }
    return leafList
}

/**
 * Get all children nodes below the passed node, including this node.
 * @return `this` node and all its inheritors.
 */
fun <T> TreeNode<T>.getAllBelow(): NodeList<T> {
    var childrenList = listOf<TreeNode<T>>()
    children.forEach { child ->
        childrenList = childrenList + child.getAllBelow()
    }
    return listOf(this) + childrenList
}

/**
 * Print all nodes in a node list, each node shifted by `20 * depth`
 */
fun <T> NodeList<T>.printNodesWithDepth() {
    forEach {
        println(" ".repeat(20 * it.depth) + "(${it.depth}) ${it.name}")
    }
}

/**
 * Get String representation of a path from this node.
 * @return `"Invalid Path"` if the path does not exist
 */
fun <T> TreeNode<T>.getStringPath(vararg path: Int): String {
    return if (isValidPath(*path)) {
        var currentNode = this
        var s = currentNode.name
        for (element in path) {
            currentNode = currentNode.children[element]
            s += " -> ${currentNode.name}"
        }
        s
    } else "Invalid Path"
}

/**
 * Get String representation of a path from the tree root.
 * @return `"Invalid Path"` if the path does not exist
 */
fun <T> Tree<T>.getStringPath(vararg path: Int): String = root.getStringPath(*path)

/**
 * Get a [String] representation of the path from the tree root to this node.
 * @return `"Invalid Path"` if the path does not exist
 */
fun <T> TreeNode<T>.getStringPathFromRoot(): String {
    val pathFromRoot = this.getPath().toIntArray()
    return treeRef.getStringPath(*pathFromRoot)
}

/**
 * Prints out all leaves, each shifted by `20 * depth`
 */
fun <T> Tree<T>.printLeavesWithDepth() {
    val allLeafNodes = getAllLeaves().asReversed() // Looks better reversed for some reason
    println("All leaf nodes (amount = ${allLeafNodes.size}) are:")
    allLeafNodes.printNodesWithDepth()
}

/**
 *  Get a visualization of the whole tree.
 *
 *  How to read the visualization:
 *  All nodes with depth
 *  `(x) == (x + 1)` of a node to the left
 *  and below that node are its children
 */
fun <T> Tree<T>.visualizeTree() {
    getAllNodes().run {
        println("Visualizing a tree:" +
                "\n$this" +
                "\nTree name = \"$treeName\"")
        println("All nodes (amount = ${size}) are:")
        printNodesWithDepth()
    }
}
