package examples

import treeBuilder.Tree

class GeneralExample {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val tree = Tree<String>(Tree.TreeNode("root"))
            val root = tree.root
            val rootList = mutableListOf(
                "a0", "a1", "a2", "a3"
            )
            /**
             * Add nodes to root
             */
            tree.addNodes(root, Pair(intArrayOf(), rootList))
            val level1List1 = mutableListOf(
                "b0", "b1", "b2"
            )
            val level1List3 = mutableListOf(
                "d0", "d1", "d2"
            )
            val level1List4 = mutableListOf(
                "e0", "e1", "e2"
            )
            val level1List2 = mutableListOf(
                "c0", "c1", "c2"
            )
            val level2List1 = mutableListOf(
                "f0", "f1"
            )
            val level2List2 = mutableListOf(
                "g0", "g1"
            )
            val level2List3 = mutableListOf(
                "h0", "h1"
            )
            val level3List1 = mutableListOf(
                "i0"
            )
            val level3List2 = mutableListOf(
                "j0"
            )
            val level3List3 = mutableListOf(
                "k0"
            )
            val level3List4 = mutableListOf(
                "l0"
            )
            val level3List5 = mutableListOf(
                "m0"
            )
            val level3List6 = mutableListOf(
                "n0"
            )
            val level3List7 = mutableListOf(
                "o0"
            )
            val level3List8 = mutableListOf(
                "p0"
            )
            /**
             * Adding each list to its respective path (passed in pairs)
             * check addNodes() documentation
             */
            tree.addNodes(
                root,
                Pair(intArrayOf(0), level1List1),
                Pair(intArrayOf(1), level1List2),
                Pair(intArrayOf(2), level1List3),
                Pair(intArrayOf(3), level1List4),
                Pair(intArrayOf(0, 0), level2List1),
                Pair(intArrayOf(0, 1), level2List2),
                Pair(intArrayOf(0, 2), level2List3),
                Pair(intArrayOf(0, 0, 0), level3List1),
                Pair(intArrayOf(0, 0, 1), level3List2),
                Pair(intArrayOf(0, 1, 0), level3List3),
                Pair(intArrayOf(0, 1, 1), level3List4),
                Pair(intArrayOf(0, 2, 0), level3List5),
                Pair(intArrayOf(0, 2, 1), level3List6),
                Pair(intArrayOf(0, 2, 0), level3List7),
                Pair(intArrayOf(0, 2, 1), level3List8),
            )

            /**
             * one way to reference a node in the tree
             */
            val node = root.childrenList[0].childrenList[2].childrenList[0]
            /**
             * Another way to reference node in a tree
             */
            val node2 = root.getNode(mutableListOf(0, 2, 1))

            val attributes = mutableListOf<String>()
            /**
             * add attributes to node
             */
            node.addAttributes(attributes)
            /**
             * get attributes from a node
             */
            node.getAttributes()

            tree.pathToCurrentNode = node.getPath()
            println("Path to ${node.name} is ${tree.getStringPath(tree.pathToCurrentNode)}")

            tree.pathToCurrentNode = node2.getPath()
            println("Path to ${node2.name} is ${tree.getStringPath(tree.pathToCurrentNode)}")
            /**
             * Move your position in the tree up, if at the top, remain at the same place
             */
            tree.movePointerUp()
            println("Path to parent of ${node2.name} is ${tree.getStringPath(tree.pathToCurrentNode)}")
            /**
             * Prints out all leaves, each shifted by 20 * its depth
             */
            tree.printAllLeafNodes()
            /**
             *  Get visualization of the whole tree
             *  How to read visualization:
             *  All nodes with (x) = 1 + (x) of the node left to it
             *  and that are above that node are its children
             */
            tree.visualizeTree()
            /**
             * clone tree (return a deep copy of it)
             */
            val clonedTree = tree.clone()
            /**
             * Adding new node "New Node" to the cloned tree as a last child of clonedTree.root and visualizing the cloned tree
             */
            clonedTree.addNodes(clonedTree.root, Pair(intArrayOf(), mutableListOf("New Node")))
            clonedTree.visualizeTree()
        }
    }
}