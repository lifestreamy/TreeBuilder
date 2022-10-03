package treeBuilder

import kotlin.math.max
import kotlin.properties.Delegates

class TreeBuilder<T> {

    /**
     * root node
     */
    lateinit var root: TreeNode<T>
        private set

    /**
     * @pathToCurrentNode - path to the node that the user is at currently
     * Used to store your location in the tree
     */
    var pathToCurrentNode = mutableListOf<TreeNode<T>>()

    /**
     * @name name of a node
     * @nodeAttribute Attribute of a node, null by default
     * @childrenList Mutable list with all children nodes of this node, pass an object with type T
     * @childrenNamesList Mutable list with all children nodes' names of this node
     * @parent TreeNode that is a parent of this TreeNode. If TreeNode is the root node, parent = null
     * @visited needed for tree traversal
     * @depth depth of a node
     */
    class TreeNode<T>(var name: String, private var nodeAttribute: T? = null) {
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
        fun addChildren(childrenNames: MutableList<String>) =
            childrenNames.forEach {
                addChild(TreeNode(it))
            }

        /**
         * Add an attribute (as Object) to this node
         */
        fun addAttribute(attribute: T) {
            this.nodeAttribute = attribute
        }

        /**
         *  Get attribute of this node
         */
        fun getAttribute(): T? {
            return this.nodeAttribute
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
            return if (nodesIndices.size == 1)
                childrenList[nodesIndices[0]]
            else {
                var node = childrenList[nodesIndices[0]]
                for (x in 1 until nodesIndices.size) {
                    node = node.childrenList[nodesIndices[x]]
                }
                node
            }
        }
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
     */
    fun addNodes(
        rootNode: TreeNode<T>, vararg pairList: Pair<IntArray, MutableList<String>>
    ) {
        pairList.forEach {
            var node: TreeNode<T> = rootNode
            val path = it.first
            val nodeList = it.second
            path.forEach { index ->
                node = node.childrenList[index]
            }
            node.addChildren(nodeList)
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
     * Add new root node
     * Set current location to root node
     */
    fun addRoot(root: TreeNode<T>) {
        this.root = root
        pathToCurrentNode.add(this.root)
    }

    /**
     * Finds the first leaf of the tree
     */
//    fun searchFirstLeaf(): TreeNode<T> {
//        var leafNode = this.root
//        while (leafNode.hasChildren()) {
//            leafNode = leafNode.childrenList[0]
//        }
//        return leafNode
//    }



    fun getAllLeafs(allNodesList : MutableList<TreeNode<T>>): MutableList<TreeNode<T>> {
        val leafList = mutableListOf<TreeNode<T>>()
        allNodesList.forEach {
            if (!it.hasChildren()) leafList.add(it)
        }
        return leafList
    }

    /**
     * Get all nodes in the tree
     */
    fun getAllNodes(): MutableList<TreeNode<T>> {
        val nodesList: MutableList<TreeNode<T>> = getAllChildren(root)
        nodesList.add(root)
        return nodesList
    }

    /**
     * Get all children nodes below this node
     */

    fun getAllChildren(node: TreeNode<T>): MutableList<TreeNode<T>> {
        val childrenList = mutableListOf<TreeNode<T>>()
        while (node.hasChildren() && !node.visited){
            node.childrenList.forEach {
                childrenList.let { list1 -> getAllChildren(it).let(list1::addAll) }
            }
            node.visited = true
        }
        childrenList.add(node)
        return childrenList
    }

    /**
     * Get visualizetion of this tree
     */

    fun visualizeTree() {
//        val firstLeaf = searchFirstLeaf()
//        val pathToLeaf = firstLeaf.getPath()
//        val listDepth = pathToLeaf.size
//        var depth: Int
//        var node: TreeNode<T>
//        val currentNode = firstLeaf
//        while (currentNode != root) {
//
//        }
    }

}


