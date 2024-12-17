package com.github.treeBuilder.dsl

import com.github.treeBuilder.Tree
import com.github.treeBuilder.TreeException
import com.github.treeBuilder.TreeNode
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

typealias NodeList<T> = List<TreeNode<T>>

@DslMarker
annotation class TreeDslMarker

@DslMarker
annotation class RootDslMarker

@DslMarker
annotation class NodeContextMarker

fun <T> tree(init: TreeBuilderContext<T>.() -> Unit): Tree<T> = TreeBuilderContext<T>(Tree<T>()).run {
    init()
    getBuiltTree()
}

@TreeDslMarker
class TreeBuilderContext<T> internal constructor(private val treeRef: Tree<T>) {

    var treeName: String? = null
        set(value) {
            treequire(treeName == null) { "treeName has already been specified" }
            field = value
            // update the original treeRef
            treeRef.treeName = field!!
        }

    var rootNode: TreeNode<T>? = null
        private set(value) {
            treequire(field == null) { "The root was declared more than once in the tree {} builder!" }
            treequire(value != null) { "Can't assign a null to the root node!" }
            field = value.also { it.treeRef = treeRef }
            // update the original treeRef because it had a separate rootNode
            treeRef.root = field!!
        }

    fun root(name: String, attributes: List<T> = emptyList()) {
        rootNode = TreeNode(name, attributes)
    }

    fun root(init: RootNodeContext<T>.() -> Unit) {
        rootNode = RootNodeContext<T>().apply(init).getNewRoot()
    }

    fun node(init: TreeNodeContext<T>.() -> Unit) = node(name = "", attributes = emptyList(), init = init)

    /**
     *  Add a new child node to the root. Supports nested nodes.
     * */
    fun node(name: String, attributes: List<T> = emptyList(), init: TreeNodeContext<T>.() -> Unit = {}) {
        // rootNode should have been assigned in the builder by this point
        if (rootNode == null) {
            throw TreeException(
                "Can't add children to root, initialize the root first. Declare root {} inside the tree {} block.")
        }
        rootNode!!.asContext {
            node(name = name, attributes = attributes, init = init)
        }
    }

    /**
     *  Add a child with no attributes to the root
     * */
    fun emptyNode(name: String) = node(name)

    /**
     * Add empty nodes (nodes with no attributes) to current Root context.
     * @return the `names` quantity for convenience and later use
     * */
    fun emptyNodes(name1: String, vararg names: String): Int = listOf(name1, *names).run {
        forEach { emptyNode(it) }
        size
    }

    /**
     * Add empty nodes (nodes with no attributes) to current, Root context.
     * @return `names` size for convenience and later use
     * */
    fun emptyNodes(names: List<String>): Int = names.run {
        treequire(isNotEmpty()) { "Should not call emptyNodes() with an empty list." }
        forEach { emptyNode(it) }
        size
    }

    /**
     * Change current context to the path from the root node.
     *
     * The root node has an empty path.
     * */
    fun atPath(index1: Int, vararg indices: Int, init: TreeNodeContext<T>.() -> Unit) {
        val fullPath = listOf(index1, *indices.toTypedArray())
        val nodeAtPath = treeRef.getNodeOrNull(fullPath) ?: throw TreeException(
            "Tried adding nodes in builder to a non-existent path. Try changing the order of blocks." + "\nProvided path: $fullPath . Failed at the index in path: ${
                treeRef.checkPath(fullPath)
            }")
        nodeAtPath.asContext(init)
    }

    internal fun getBuiltTree(): Tree<T> {
        validate()
        return treeRef.also {
            it.treeName = this.treeName ?: "Tree"
        }
    }

    internal fun validate() {
        treequire(rootNode != null) { "No root node added to the tree! Use root {} inside the tree {} !" }
        treeRef.getAllNodes().forEach {
            treequire(it.treeRef == treeRef) { "A node in the created tree references a wrong tree instance." }
        }
    }
}

@TreeDslMarker
@RootDslMarker
class RootNodeContext<T> internal constructor() {
    var name: String = "Root"
    var attributes: List<T> = emptyList()

    internal fun getNewRoot() = TreeNode(name, attributes)
}

@NodeContextMarker
@TreeDslMarker
abstract class NodeContext<T> {
    abstract val name: String
    abstract val attributes: List<T>

    internal abstract fun getBuiltNode(): TreeNode<T>
}

private inline fun <T> TreeNode<T>.asContext(init: TreeNodeContext<T>.() -> Unit = {}) = TreeNodeContext<T>(this).init()

class TreeNodeContext<T> internal constructor(internal val contextOwnerNode: TreeNode<T>) : NodeContext<T>() {
    override var name: String = contextOwnerNode.name
    override var attributes: List<T> = contextOwnerNode.attributes

    /** Add an empty node to configure inside the builder afterward.*/
    fun node(init: TreeNodeContext<T>.() -> Unit) = node(name = "", attributes = emptyList(), init = init)

    /**
     *  Add a new named node with a builder
     * */
    fun node(name: String, attributes: List<T> = emptyList(), init: TreeNodeContext<T>.() -> Unit) {
        val newNode = TreeNode<T>(name, attributes).also {
            it.treeRef = contextOwnerNode.treeRef
        }
        val childContext = TreeNodeContext(newNode)
        childContext.init()
        // child contextOwnerNode has been changed by this time, possibly having been applied other child nodes.
        appendNode(childContext.getBuiltNode())
    }

    /**
     *  Add a child with no attributes to this node
     * */
    fun emptyNode(name: String) {
        contextOwnerNode.appendChild(name)
    }

    /**
     *  Add named children with empty attributes to this node
     *  @return the `names` quantity for convenience and later use
     * */
    fun emptyNodes(name1: String, vararg names: String): Int = listOf(name1, *names).let {
        contextOwnerNode.appendEmptyChildren(it)
        it.size
    }

    /**
     * Add empty nodes (nodes with no attributes) to current context.
     * @return `names` size for convenience and later use
     * */
    fun emptyNodes(names: List<String>): Int = names.run {
        treequire(isNotEmpty()) { "Should not call emptyNodes() with an empty list." }
        forEach { emptyNode(it) }
        size
    }

    /**
     * Move to a context relative to the current context.
     * */
    fun atRelativePath(index1: Int, vararg indices: Int, init: TreeNodeContext<T>.() -> Unit) {
        val fullPath = listOf(index1, *indices.toTypedArray())
        val nodeAtPath = contextOwnerNode.getNodeOrNull(*fullPath.toIntArray()) ?: throw TreeException(
            "Tried adding nodes in builder to a non-existent relative path. Try changing the order of blocks." + "\nProvided path: $fullPath . Failed at the index in path: ${
                contextOwnerNode.checkPath(fullPath)
            }")
        nodeAtPath.asContext(init)
    }

    internal fun appendNode(child: TreeNode<T>) {
        contextOwnerNode.appendChild(child)
    }

    override fun getBuiltNode(): TreeNode<T> = contextOwnerNode.also {
        // These properties could've been changed from within the builder, so this should be reflected
        it.name = name
        it.attributes = attributes
    }
}

@OptIn(ExperimentalContracts::class)
internal fun treequire(value: Boolean, lazyMessage: () -> Any = {}) {
    contract {
        returns() implies value
    }
    if (!value) {
        val message = lazyMessage()
        throw TreeException(message.toString())
    }
}