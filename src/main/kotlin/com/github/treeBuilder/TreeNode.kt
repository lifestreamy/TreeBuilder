package com.github.treeBuilder

import com.github.treeBuilder.dsl.NodeList
import kotlin.collections.forEach

/**
 * @param name - name of a node
 * @param attributes - Attributes of a node, null by default
 * @property children - list with all children nodes of this node, pass an object with type T
 * @property parent - [TreeNode] that is a parent of this TreeNode. If [TreeNode] is the root node, parent = null
 * @property depth - depth of this node
 * @property index - index of this node in the childrenList it belongs to
 * @property childLowestIndex - the smallest unoccupied index in the [children] that is assigned to a new child
 */
data class TreeNode<T>(var name: String = "", var attributes: List<T> = listOf()) {
    var parent: TreeNode<T>? = null
        private set

    var children: NodeList<T> = listOf()
        private set

    var depth = 0
        private set

    var index = 0
        private set

    var childLowestIndex = 0
        private set

    lateinit var treeRef : Tree<T>
        internal set

    /**
     * Add a new single TreeNode to this TreeNode
     * @return this [TreeNode] for chaining calls
     */
    fun appendChild(node: TreeNode<T>) = apply {
        node.parent = this
        node.treeRef = treeRef
        node.depth = depth + 1
        node.index = childLowestIndex
        childLowestIndex++
        children = children + node
    }

    /**
     * Add a new single TreeNode to this TreeNode.
     *
     * A node with no attributes is called "empty node".
     * @return this [TreeNode] for chaining calls
     */
    fun appendChild(name: String, attributes: List<T> = listOf()) = appendChild(TreeNode(name,attributes))

    /**
     * Add empty children nodes to this node, pass a  List with nodes' names
     * @return this [TreeNode] for chaining calls
     */
    @JvmName("appendChildrenNames") // otherwise clashes with the fun appendChildren(names: List<TreeNode<T>>), both have the same signature with Ljava/util/List parameter type
    fun appendEmptyChildren(names: List<String>) = apply {
        names.forEach(::appendChild)
    }

    /**
     * Add empty children nodes to this node, pass names
     * @return this [TreeNode] for chaining calls
     */
    fun appendEmptyChildren(vararg names: String) = appendEmptyChildren(names.toList())

    /**
     * Add children nodes to this node, pass a NodeList
     * @return this [TreeNode] for chaining calls
     */
    @JvmName("appendChildrenNodes")
    fun appendChildren(children: NodeList<T>) = apply {
        children.forEach(::appendChild)
    }

    /**
     * Change the name of this node.
     * @return this [TreeNode] for chaining calls
     */
    fun setName(name : String) = apply {
        this.name = name
    }

    /**
     * Change/add attributes (as List<T>) to this node. Create Tree with type T that you want your attributes to have.
     * @return this [TreeNode] for chaining calls
     */
    fun setAttributes(vararg attributes: T) = apply {
        this.attributes = attributes.toList()
    }

    /**
     * Check if this node has children
     */
    fun hasChildren(): Boolean = children.isNotEmpty()

    /**
     * Check if this node has a child at the index
     */
    fun hasChildAt(index: Int): Boolean = index in children.indices

    /**
     * Check if this node has parent
     */
    fun hasParent(): Boolean = parent !== null

    /**
     * Get the root node of the tree that this node belongs to.
     *
     * Alternative to the `treeRef.root` so as not to be using the tree reference.
     */
    fun getRoot(): TreeNode<T> {
        var node = this
        while (node.hasParent()) {
            node = node.parent!!
        }
        return node
    }

    /**
     * Get the path from root node to this node in a List<Int>, no recursion
     */
    fun getPath(): List<Int> = mutableListOf<Int>().also { path ->
        var node = this
        while (node.hasParent()) {
            path.add(0, node.index)
            node = node.parent!!
        }
    }

    /**
     * Get the path from root node to this node, uses recursion
     */
    fun getPathRec(): List<Int> = parent?.run { getPathRec() + index } ?: listOf()

    /**
     * Get a TreeNode object located at a certain path in hierarchy, pass path as Int indices
     * @throws [TreeException] if the path does not exist
     */
    fun tryGetNode(vararg path: Int): TreeNode<T> = if (path.isEmpty()) this
    else {
        try {
            var node = children[path[0]]
            for (i in 1 until path.size) {
                node = node.children[path[i]]
            }
            node
        } catch (e: IndexOutOfBoundsException) {
            throw TreeException("The path does not exist.\n${e.message}")
        }
    }

    /**
     * Get a TreeNode at path starting from this node
     * @return `null` if the path does not exist
     */
    fun getNodeOrNull(vararg path: Int): TreeNode<T>? {
        return if (path.isEmpty()) this
        else {
            var node = children.getOrElse(path[0]) { return null }
            for (i in 1 until path.size) {
                node = node.children.getOrElse(path[i]) { return null }
            }
            node
        }
    }

    /**
     * Return a new independent instance of this TreeNode (deep copy), uses recursion
     */
    fun clone(): TreeNode<T> = TreeNode(name, attributes).also { newNode ->
        newNode.depth = depth
        newNode.treeRef = treeRef
        children.forEach { child ->
            newNode.appendChild(child.clone())
        }
    }

    /**
     *  Check if a path can be traversed from this node
     *  @return * 0 for success
     *          * -1 if the path was empty
     *          * index in the path at which the traversal failed
     * */
    fun checkPath(vararg path: Int): Int = checkPath(path.toList())

    /**
     *  Check if a path can be traversed from this node
     *  @return * 0 for success
     *          * -1 if the path was empty
     *          * index in the path at which the traversal failed
     * */
    fun checkPath(path: List<Int>): Int {
        return if (path.isNotEmpty()) {
            var node = this
            //return the position in the path array at which the childrenList[path[i]] does not exist
            for (i in path.indices) node = node.children.getOrElse(path[i]) { return i }
            0
        } else -1
    }

    /**
     * @return `true` if path was empty or on success
     * */
    fun isValidPath(vararg path: Int) = isValidPath(path.toList())

    /**
     * @return `true` if path was empty or on success
     * */
    fun isValidPath(path: List<Int>) = checkPath(path) in listOf(-1, 0)

}