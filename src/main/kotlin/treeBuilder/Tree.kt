package treeBuilder

/**
 *  Set T to the type of attributes that you want to attach to a node
 *  For example: TreeNode<String> has its attributes in MutableList<String>
 */

data class Tree<T>(var root: TreeNode<T>) {


    /**
     * @pathToCurrentNode - path to the node that the user is at currently
     * Used to store your location in the tree
     */
    var pathToCurrentNode = mutableListOf<TreeNode<T>>()

    /**
     * Set current location to root node on initialization
     */
    init {
        pathToCurrentNode = mutableListOf(this.root)
    }

    /**
     * @name name of a node
     * @nodeAttribute Attribute of a node, null by default
     * @childrenList Mutable list with all children nodes of this node, pass an object with type T
     * @childrenNamesList Mutable list with all children nodes' names of this node
     * @parent TreeNode that is a parent of this TreeNode. If TreeNode is the root node, parent = null
     * @visited needed for tree traversal
     * @depth depth of a node
     */
    data class TreeNode<T>(var name: String, private var nodeAttributes: MutableList<T>? = null) {
        private var parent: TreeNode<T>? = null

        val childrenList: MutableList<TreeNode<T>> = mutableListOf()
        val childrenNamesList: MutableList<String> = mutableListOf()
        var visited = false
        var depth = 0

        /**
         * Add a new single TreeNode to this TreeNode
         */
        private fun addChild(node: TreeNode<T>) {
            node.parent = this
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
         * Add attributes (as MutableList<T>) to this node. Create Tree with type T that you want your attributes to have
         */
        fun addAttributes(attribute: MutableList<T>) {
            this.nodeAttributes = attribute
        }

        /**
         *  Get attributes of this node
         */
        fun getAttributes(): MutableList<T>? {
            return this.nodeAttributes
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
         * Get path in Mutable List of TreeNode<T> from root node to this node, uses recursion
         */
        fun getPath(): MutableList<TreeNode<T>> {
            val path = mutableListOf<TreeNode<T>>()
            if (this.hasParent()) {
                path.let { list1 -> this.parent!!.getPath().let(list1::addAll) }
            }
            path.add(this)
            return path
        }

        /**
         * Get a TreeNode object located at certain index in hierarchy, pass path as a MutableList<Int>
         */
        fun getNode(nodesIndices: MutableList<Int>? = null): TreeNode<T> {
            if (nodesIndices.isNullOrEmpty()) return this
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
         * return a new independent instance of this TreeNode
         */
        fun clone() = this.copy()
    }

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
     * @param pair consists of 1) Path as an array 2)MutableList<String> as a node list to add to that path
     * will add all node lists in pairs to their respective paths in pairs
     * If no path is provided in a pair then the nodes will be added to rootNode (any node passed as root in a pair)
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
    fun getStringPath(path: MutableList<TreeNode<String>>): String {
        var s = ""
        path.forEach {
            s += " -> ${it.name}"
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
     * The node is the list are sorted deep to shallow and right to left
     */
    fun getAllNodes(): MutableList<TreeNode<T>> {
        val clone = this.clone()
        return getAllChildren(clone.root)
    }

    /**
     * Get all children nodes below the passed node
     * after passing all nodes' parameter VISITED is set to true, so we should use this operation on a treeNode.clone() !
     */
    fun getAllChildren(node: TreeNode<T>): MutableList<TreeNode<T>> {
        val childrenList = mutableListOf<TreeNode<T>>()
        while (node.hasChildren() && !node.visited) {
            node.childrenList.forEach {
                childrenList.let { list1 -> getAllChildren(it).let(list1::addAll) }
            }
            node.visited = true
        }
        childrenList.add(node)
        return childrenList
    }

    /**
     *  Use to make an independent instance of this treeBuilder
     */
    fun clone() = this.copy()

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
    fun printAllLeafNodes(){
        val allLeafNodes = this.getAllLeaves()
        println("All leaf nodes (amount = ${allLeafNodes.size}) are:")
        printNodesWithDepth(allLeafNodes)
    }


    /**
     *  Get visualization of the whole tree
     *  How to read visualization:
     *  All nodes with (x) > (x) of the node left to it
     *  and that are above that node are its children
     */
    fun visualizeTree() {
        val allTreeNodes = this.getAllNodes()
        println("All nodes (amount = ${allTreeNodes.size}) are:")
        printNodesWithDepth(allTreeNodes)
    }

}


