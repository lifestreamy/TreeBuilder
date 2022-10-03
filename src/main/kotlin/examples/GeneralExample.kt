package examples

import treeBuilder.TreeBuilder

class GeneralExample {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val treeBuilder = TreeBuilder<String>()
            treeBuilder.addRoot(TreeBuilder.TreeNode("root"))
            println(treeBuilder.root.name)
            val root = treeBuilder.root
            val rootList = mutableListOf(
                "a0",
                "a1",
                "a2",
                "a3"
            )
            /**
             * for the root you have to use addChildren() not addNodes()
             */
            root.addChildren(rootList)
            val level1List1 = mutableListOf(
                "b0",
                "b1",
                "b2"
            )
            val level1List3 = mutableListOf(
                "d0",
                "d1",
                "d2"
            )
            val level1List4 = mutableListOf(
                "e0",
                "e1",
                "e2"
            )
            val level1List2 = mutableListOf(
                "c0",
                "c1",
                "c2"
            )
            val level2List1 = mutableListOf(
                "f0",
                "f1"
            )
            val level2List2 = mutableListOf(
                "g0",
                "g1"
            )
            val level2List3 = mutableListOf(
                "h0",
                "h1"
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
            treeBuilder.addNodes(root,
                Pair(intArrayOf(0),level1List1),
                Pair(intArrayOf(1),level1List2),
                Pair(intArrayOf(2),level1List3),
                Pair(intArrayOf(3),level1List4),
                Pair(intArrayOf(0,0),level2List1),
                Pair(intArrayOf(0,1),level2List2),
                Pair(intArrayOf(0,3),level2List3),
                Pair(intArrayOf(0,0,0),level3List1),
                Pair(intArrayOf(0,0,1),level3List2),
                Pair(intArrayOf(0,1,0),level3List3),
                Pair(intArrayOf(0,1,1),level3List4),
                Pair(intArrayOf(0,2,0),level3List5),
                Pair(intArrayOf(0,2,1),level3List6),
                Pair(intArrayOf(0,3,0),level3List7),
                Pair(intArrayOf(0,3,1),level3List8),
            )

            /**
             * one way to reference a node in the tree
             */
            val node =
                treeBuilder.root.childrenList[1].childrenList[1].childrenList[1]
            /**
             * Another way to reference node in a tree
             */
            val node2 = treeBuilder.root.getNode(mutableListOf(2, 3, 1))
            /**
             * change current position in the tree
             */
            treeBuilder.pathToCurrentNode = node.getPath()
            println("Path to ${node.name} is ${treeBuilder.getStringPath(treeBuilder.pathToCurrentNode)}")
            /**
             * change current position in the tree
             */
            treeBuilder.pathToCurrentNode = node2.getPath()
            println("Path to ${node2.name} is ${treeBuilder.getStringPath(treeBuilder.pathToCurrentNode)}")
            /**
             * move pointer up
             */
            treeBuilder.movePointerUp()
            println("Patn to parent of ${node2.name} is ${treeBuilder.getStringPath(treeBuilder.pathToCurrentNode)}")
            /**
             *  Visualizing the whole tree
             */
            treeBuilder.visualizeTree()
        }
    }
}