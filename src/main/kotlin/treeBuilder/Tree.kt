package treeBuilder


/**
 *  Set T to the type of attributes that you want to attach to a node
 *  For example: TreeNode<String> has its attributes in MutableList<String>
 */
class Tree<T>(var root: TreeNode<T>) {

    /**
     * @property pathToCurrentNode - path to the node that the user is at currently
     * Used to store your location in the tree
     */
    var pathToCurrentNode = mutableListOf<Int>()

    /**
     * Set current location to root node on initialization
     */
    init {
        pathToCurrentNode = mutableListOf(0)
    }

    /**
     * @param name - name of a node
     * @param nodeAttributes - Attributes of a node, null by default
     * @property childrenList - Mutable list with all children nodes of this node, pass an object with type T
     * @property childrenNamesList - Mutable list with all children nodes' names of this node
     * @property parent - TreeNode that is a parent of this TreeNode. If TreeNode is the root node, parent = null
     * @property depth - depth of this node
     * @property index - index of this node in the childrenList it belongs to
     */
    data class TreeNode<T>(var name: String, private var nodeAttributes: MutableList<T> = mutableListOf()) {
        var parent: TreeNode<T>? = null
        val childrenList: MutableList<TreeNode<T>> = mutableListOf()
        val childrenNamesList: MutableList<String> = mutableListOf()
        var depth = 0
            private set
        var index = 0
            private set
        var childLowestIndex = 0
            private set

        /**
         * Add a new single TreeNode to this TreeNode
         */
        fun addChild(node: TreeNode<T>) {
            node.parent = this
            node.index = this.childLowestIndex
            this.childLowestIndex++
            node.depth = this.depth + 1
            childrenList.add(node)
            childrenNamesList.add(node.name)
        }

        /**
         * Add children nodes to this node, pass a Mutable List with nodes' names
         */
        fun addChildren(childrenNames: MutableList<String>) = childrenNames.forEach {
            addChild(TreeNode(it))
        }

        /**
         * Change/add attributes (as MutableList<T>) to this node. Create Tree with type T that you want your attributes to have
         */
        fun changeAttributes(vararg attributes: T) {
            this.nodeAttributes = attributes.toMutableList()
        }

        /**
         *  Get attributes of this node
         */
        fun getAttributes(): MutableList<T> {
            return this.nodeAttributes.map { it }.toMutableList()
        }

        /**
         * Check if this node has children
         */
        fun hasChildren(): Boolean = childrenList.isNotEmpty()

        /**
         * Check if this node has parent
         */
        fun hasParent(): Boolean = parent !== null

        /**
         * Get path in Mutable List of Int from root node to this node, uses recursion
         */
        fun getPath(): MutableList<Int> {
            val path = mutableListOf<Int>()
            if (this.hasParent()) {
                path.let { this.parent!!.getPath().let(it::addAll) }
                path.add(this.index)
            }
            return path
        }

        /**
         * Get a TreeNode object located at certain index in hierarchy, pass path as a MutableList<Int>
         */
        fun getNode(vararg nodesIndices: Int): TreeNode<T> {
            if (nodesIndices.isEmpty()) return this
            return if (nodesIndices.size == 1) childrenList[nodesIndices[0]]
            else {
                var node = childrenList[nodesIndices[0]]
                for (x in 1 until nodesIndices.size) {
                    node = node.childrenList[nodesIndices[x]]
                }
                node
            }
        }

        /**
         * Return a new independent instance of this TreeNode (deep copy), uses recursion
         */
        fun clone(): TreeNode<T> {
            val newNode = TreeNode(this.name, this.nodeAttributes.map { it }.toMutableList())
            newNode.depth = this.depth
            this.childrenList.forEach {
                val newChild = it.clone()
                newNode.addChild(newChild)
            }
            return newNode
        }
    }

    /**
     *  Use to make an independent instance of this tree (deep copy)
     */
    fun clone() = Tree(this.root.clone())

    /**
     * Move your current position to parent node, if no parent exists, remain at the same place
     */
    fun movePointerUp() {
        if (pathToCurrentNode.isNotEmpty()) {
            pathToCurrentNode.removeLast()
        }
    }

    /**
     * @param rootNode node from which the relative path is constructed
     * @param pairList consists of
     * 1) Path as an array
     * 2)MutableList<String> as a node list to add to that path
     * This method will add all node lists to their respective paths from each pair
     * If no path is provided in a pair then the nodes will be added to rootNode (any node passed as a @rootNode parameter)
     */

    fun addNodes(
        rootNode: TreeNode<T>, vararg pairList: Pair<IntArray, MutableList<String>>
    ) {
        pairList.forEach {
            if (it.first.isNotEmpty()) {
                var node: TreeNode<T> = rootNode
                val path = it.first
                val nodeList = it.second
                path.forEach { index ->
                    node = node.childrenList[index]
                }
                node.addChildren(nodeList)
            } else {
                rootNode.addChildren(it.second)
            }
        }
    }

    /**
     * Get String representation of a path
     */
    fun getStringPath(root: TreeNode<T>, vararg path: Int): String {
        var s = ""
        var currentNode = root
        s += currentNode.name
        for (i in 0 until path.size) {
            currentNode = currentNode.childrenList[path[i]]
            s += " -> ${currentNode.name}"
        }
        return s
    }

    /**
     *  Get all leaves (nodes that have no children) from this treeBuilder
     */
    fun getAllLeaves(): MutableList<TreeNode<T>> {
        val allNodesList = this.getAllNodes()
        val leafList = mutableListOf<TreeNode<T>>()
        allNodesList.forEach {
            if (!it.hasChildren()) leafList.add(it)
        }
        return leafList
    }

    /**
     * Get all nodes in the tree
     * The nodes in the list are sorted deep to shallow and right to left
     */
    fun getAllNodes(): MutableList<TreeNode<T>> = getAllChildren(root)

    /**
     * Get all children nodes below the passed node
     */
    fun getAllChildren(node: TreeNode<T>): MutableList<TreeNode<T>> {
        val childrenList = mutableListOf<TreeNode<T>>()
        node.childrenList.forEach {
            childrenList.let { list1 -> getAllChildren(it).let(list1::addAll) }
        }
        childrenList.add(node)
        return childrenList
    }

    /**
     * Print all nodes in a node list, each node shifted by 20 * its depth
     */
    fun printNodesWithDepth(nodeList: MutableList<TreeNode<T>>) {
        nodeList.forEach {
            println(" ".repeat(20 * it.depth) + "(${it.depth}) ${it.name}")
        }
    }

    /**
     * Prints out all leaves, each shifted by 20 * its depth
     */
    fun printAllLeafNodes() {
        val allLeafNodes = this.getAllLeaves()
        println("All leaf nodes (amount = ${allLeafNodes.size}) are:")
        printNodesWithDepth(allLeafNodes)
    }

    /**
     *  Get visualization of the whole tree
     *  How to read visualization:
     *  All nodes with (x) = 1 + (x) of the node left to it
     *  and that are above that node are its children
     */
    fun visualizeTree() {
        val allTreeNodes = this.getAllNodes()
        println("All nodes (amount = ${allTreeNodes.size}) are:")
        printNodesWithDepth(allTreeNodes)
    }

}

