package com.github.treeBuilder

import com.github.treeBuilder.dsl.NodeList
import com.github.treeBuilder.dsl.treequire

/**
 *  Set T to the type of attributes that you want to attach to a node
 *  For example: TreeNode<String> has its attributes in List<String>
 */
class Tree<T> {
    companion object {
        val RootPath = listOf<Int>()
    }

    constructor()

    constructor(root: TreeNode<T>) : this() {
        this.root = root
    }

    constructor(treeName: String) : this() {
        this.treeName = treeName
    }

    /**
     *  The root cannot be reassigned from outside the lib, but its properties can be changed (besides treeRef)
     * */
    var root: TreeNode<T> = TreeNode<T>("Default Root").also { it.treeRef = this }
        internal set(value) {
            field = value.also { it.treeRef = this }
        }

    /**
     * Made for convenience and chaining. Direct `tree.root.name = "someName"` is still possible.
     */
    fun setRootName(name: String) = apply { root.name = name }

    /**
     * Made for convenience and chaining. Direct `tree.root.attributes = someList()` is still possible.
     */
    fun setRootAttributes(attributes: List<T>) = apply { root.attributes = attributes }


    var treeName: String = ""

    /**
     * @property cursorPath - path to the node that the user is at currently.
     * Used to store your location in the tree.
     * The default location or `EmptyList` means the tree root
     */
    var cursorPath: List<Int> = emptyList()
        private set

    /**
     * Get the node located at the current cursor
     * */
    val cursorNode: TreeNode<T>
        get() = root.tryGetNode(*cursorPath.toIntArray())

    val cursorIsAtRoot: Boolean
        get() = cursorPath.isEmpty()

    init {
        treequire(cursorIsAtRoot) { "On init, the path to the cursor should be empty" }
    }

    /**
     * @return this `Tree` for chaining calls
     * */
    fun cursorMoveToRoot() = apply {
        cursorPath = emptyList()
    }

    /**
     * Set cursor relatively from current position.
     * Remain at the same place if the path does not exist.
     * @return [Tree] for chaining calls.
     * */
    fun cursorSetRelative(vararg path: Int) = apply {
        if (cursorNode.isValidPath(*path)) cursorPath = cursorPath + path.toList()
    }

    /**
     * Move your current position to parent node, if no parent exists, remain at the same place
     * @return `true` if successful, `false` otherwise
     */
    fun cursorMoveUp(): Boolean = if (cursorPath.isEmpty()) false else {
        cursorPath = cursorPath.dropLast(1)
        true
    }

    /**
     *  Move the cursor up n times, stop when the
     *  @return [Tree] for chaining calls
     * */
    fun cursorMoveUp(n: Int) = apply {
        treequire(n >= 0)
        cursorPath = cursorPath.dropLast(n)
    }

    /**
     *  Check if able to move the cursor up, up to n times.
     *  @return `false` if can move <n times, else `true`
     * */
    fun cursorCheckMoveUp(n: Int): Boolean = treequire(n >= 0).run { n in cursorPath.indices }

    /**
     * Update your current position to a route from root, if no such path, remain at the same place
     * @return `this` [Tree] to
     */
    fun cursorSet(vararg path: Int): Tree<T> = apply {
        when (checkPath(*path)) {
            0 -> cursorPath = path.toList()
            -1 -> cursorPath = emptyList()
            else -> {}
        }
    }

    /**
     * Update your current position to a route from root, if no such path, remain at the same place
     * @return the same as [checkPath]
     */
    fun cursorSetWithResult(vararg path: Int): Int {
        val result = checkPath(*path)
        when (result) {
            0 -> cursorPath = path.toList()
            -1 -> cursorPath = emptyList()
            else -> {}
        }
        return result
    }

    /**
     *  Append a node to the node at the cursor
     *  @return [Tree] for chaining calls
     * */
    fun cursorAppendNode(node: TreeNode<T>) = apply {
        cursorNode.appendChild(node)
    }

    /**
     *  Append a node to the node at the cursor
     *  @return [Tree] for chaining calls
     * */
    fun cursorAppendNode(name: String, attributes: List<T> = listOf()) = apply {
        cursorNode.appendChild(TreeNode(name, attributes))
    }

    /**
     *  Append nodes to the node at the cursor
     *  @return [Tree] for chaining calls
     * */
    fun cursorAppendNodes(nodes: NodeList<T>) = apply {
        cursorNode.appendChildren(nodes)
    }

    /**
     *  Append empty named nodes to the node at the cursor
     *  @return [Tree] for chaining calls
     * */
    fun cursorAppendEmptyNodes(vararg names: String) = apply {
        cursorNode.appendEmptyChildren(*names)
    }

    /**
     *  Check if the path starting from root corresponds to a node
     *  @return * 0 for success
     *          * -1 if the path was empty
     *          * index in the path at which the traversal failed
     * */
    fun checkPath(vararg path: Int): Int = checkPath(path.toList())

    /**
     *  Check if the path starting from root corresponds to a node
     *  @return * 0 for success
     *          * -1 if the path was empty
     *          * index in the path at which the traversal failed
     * */
    fun checkPath(path: List<Int>): Int = root.checkPath(path)

    /**
     * Check if this path from root is valid.
     * @return `true` if path was empty or on success
     * */
    fun isValidPath(vararg path: Int) = checkPath(*path) in listOf(-1, 0)

    /**
     * Check if this path from root is valid.
     * @return `true` if path was empty or on success
     * */
    fun isValidPath(path: List<Int>) = isValidPath(*path.toIntArray())

    /**
     *  Get TreeNode at the certain path from root.
     * */
    fun getNodeOrNull(vararg path: Int): TreeNode<T>? = root.getNodeOrNull(*path)

    /**
     *  Get TreeNode at the certain path from root.
     * */
    fun getNodeOrNull(path: List<Int>): TreeNode<T>? = getNodeOrNull(*path.toIntArray())

    /**
     * @param namesPathPair consists of
     * 1) List<String> as a node list names with empty attributes to add to that path
     * 2) Path as an array
     *
     * This method will try to add all node lists to their respective paths from each pair.
     *
     * Empty path == root.
     * @return `failedCount`, `0` if none failed
     */
    fun appendEmptyNamedToPaths(vararg namesPathPair: Pair<List<String>, List<Int>>): Int {
        var failedCount = 0
        namesPathPair.forEach { (names, path) ->
            getNodeOrNull(path)?.appendEmptyChildren(names) ?: run {
                failedCount++; return@forEach
                /** acts as a `continue` */
            }
        }
        return failedCount
    }

    /**
     * @param namesPathPair consists of
     * 1) List<String> as a node list names with empty attributes to add to that path
     * 2) Path as an array
     *
     * If any of the paths do not exist, the whole method will have no effect.
     *
     * Empty path == root.
     */
    fun appendEmptyNamedToPathsStrict(vararg namesPathPair: Pair<List<String>, List<Int>>): Boolean {
        // if even one of the paths does not exist, do nothing and return false
        // save retrieved nodes to list to use later
        val targetNodes = mutableListOf<TreeNode<T>>()
        namesPathPair.forEach { (_, path) ->
            // if node exists at the path, add to the list
            getNodeOrNull(path)?.let(targetNodes::add) ?: return false
        }
        // All paths exist, add lists to targets
        targetNodes.forEachIndexed { index, target -> target.appendEmptyChildren(namesPathPair[index].first) }
        return true
    }

    /**
     * @param nodes list of nodes to append to a path
     * @param path path from root to append nodes to
     * @return `true` if successful
     */
    fun appendNodes(nodes: NodeList<T>, vararg path: Int): Boolean =
        getNodeOrNull(*path)?.let { it.appendChildren(nodes); true } ?: false

    /**
     * Append a node to a path from root
     * @return `true` if successful, `false` otherwise
     */
    fun appendNode(node: TreeNode<T>, vararg path: Int): Boolean =
        getNodeOrNull(*path)?.let { it.appendChild(node); true } ?: false

    /**
     *  Get all leaves (nodes that have no children) from this tree
     */
    fun getAllLeaves(): NodeList<T> = root.getAllLeaves()

    /**
     * Get all nodes in the tree.
     * The nodes in the list are sorted deep to shallow and right to left
     */
    fun getAllNodes(): NodeList<T> = root.getAllBelow()

    /**
     *  Use to make an independent instance of this tree (deep copy)
     */
    fun clone() = Tree(root.clone())
}