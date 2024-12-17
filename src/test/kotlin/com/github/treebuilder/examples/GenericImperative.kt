package com.github.treebuilder.examples

import com.github.treeBuilder.*
import com.github.treeBuilder.Tree.Companion.RootPath
import org.junit.jupiter.api.Test

/**
 * An example of building a generic [Tree] imperatively, i.e. by executing commands in sequence.
 *
 * This might be unsafe, so the new DSL approach is recommended.
 * @see [GenericMixed]
 * */
class GenericImperative {

    @Test
    fun main() {
        val tree = Tree<String>(treeName = "Generic Tree").apply { root.name = "Generic Root" }
        val root = tree.root
        val rootList = listOf("a0", "a1", "a2", "a3")
        /**
         * Add nodes to root
         */
        tree.appendEmptyNamedToPaths(rootList to listOf())
        val lvl1List1 = listOf("b0", "b1", "b2")
        val lvl1List3 = listOf("d0", "d1", "d2")
        val lvl1List4 = listOf("e0", "e1", "e2")
        val lvl1List2 = listOf("c0", "c1", "c2")
        val lvl2List1 = listOf("f0", "f1")
        val lvl2List2 = listOf("g0", "g1")
        val lvl2List3 = listOf("h0", "h1")
        val lvl3List1 = listOf("i0")
        val lvl3List2 = listOf("j0")
        val lvl3List3 = listOf("k0")
        val lvl3List4 = listOf("l0")
        val lvl3List5 = listOf("m0")
        val lvl3List6 = listOf("n0")
        val lvl3List7 = listOf("o0")
        val lvl3List8 = listOf("p0")
        /**
         * Adding each list to its respective path (passed in pairs)
         * check documentation
         */
        tree.appendEmptyNamedToPaths(
            lvl1List1 to listOf(0), lvl1List2 to listOf(1), lvl1List3 to listOf(2), lvl1List4 to listOf(3),
            lvl2List1 to listOf(0, 0), lvl2List2 to listOf(0, 1), lvl2List3 to listOf(0, 2),
            lvl3List1 to listOf(0, 0, 0), lvl3List2 to listOf(0, 0, 1), lvl3List3 to listOf(0, 1, 0),
            lvl3List4 to listOf(0, 1, 1), lvl3List5 to listOf(0, 2, 0), lvl3List6 to listOf(0, 2, 1),
            lvl3List7 to listOf(0, 2, 0), lvl3List8 to listOf(0, 2, 1)
                                    )

        /**
         * one way to reference a node in a tree
         */
        val node = root.children[0].children[2].children[0]

        /**
         * Another way to reference a node in a tree
         */
        val node2 = root.tryGetNode(0, 2, 1)

        val attributes = arrayOf<String>()
        /**
         * add attributes to a node
         */
        node.setAttributes(*attributes)
        /**
         * get attributes from a node
         */
        node.attributes

        tree.cursorSet(*node.getPath().toIntArray())
        println(tree.cursorPath)
        println(tree.root.getPath())
        println(node2.getPath())
        println("Path to ${node.name} from root is ${tree.getStringPath(*node.getPath().toIntArray())}")
        println("Path to ${node.name} from root is ${tree.getStringPath(*tree.cursorPath.toIntArray())}") //the same

        // Some more methods. Check the docs.
        with(tree) {
            cursorSet(*node2.getPath().toIntArray())
            cursorSetRelative(0, 0, 0, 0, 0)
            val result = cursorSetWithResult(0, 0, 0, 0, 0, 0)
            cursorCheckMoveUp(1)
//            cursorAppendNode()
//            cursorAppendEmptyNodes()
            cursorIsAtRoot
//            cursorMoveToRoot()
        }

        println("Path to ${node2.name} is ${tree.getStringPath(*tree.cursorPath.toIntArray())}")
        /**
         * Move your position in the tree up, if at the top, remain at the same place
         */
        tree.cursorMoveUp()
        println("Path to parent of ${node2.name} is ${tree.getStringPath(*tree.cursorPath.toIntArray())}")
        /**
         * Prints out all leaves, each shifted by 20 * its depth
         */
        tree.printLeavesWithDepth()
        /**
         *  Get a visualization of the whole tree.
         *
         *  How to read the visualization:
         *  All nodes with depth
         *  `(x) == (x + 1)` of a node to the left
         *  and below that node are its children
         */
        tree.visualizeTree()
        /**
         * clone the tree (return a deep copy of it)
         */
        val clonedTree = tree.clone()
        /**
         * Adding new node "New Node" to the cloned tree as a last child of clonedTree.root and visualizing the cloned tree
         */
        clonedTree.appendEmptyNamedToPaths(listOf("New Node") to RootPath)
        clonedTree.visualizeTree()
    }
}