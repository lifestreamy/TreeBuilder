package com.github.treebuilder.examples

import com.github.treeBuilder.*
import com.github.treeBuilder.Tree.Companion.RootPath
import com.github.treeBuilder.dsl.tree
import org.junit.jupiter.api.Test

/**
 * A generic example of building a [Tree] using both Kotlin DSL and imperative methods.
 * Shows most of the methods available
 * */
class GenericMixed {

    @Test
    fun main() {
        val lvl1L1 = listOf("b0", "b1", "b2")
        val lvl1L3 = listOf("d0", "d1", "d2")
        val lvl1L4 = listOf("e0", "e1", "e2")
        val lvl1L2 = listOf("c0", "c1", "c2")
        val lvl2L1 = listOf("f0", "f1")
        val lvl2L2 = listOf("g0", "g1")
        val lvl2L3 = listOf("h0", "h1")
        val lvl3L1 = listOf("i0")
        val lvl3L2 = listOf("j0")
        val lvl3L3 = listOf("k0")
        val lvl3L4 = listOf("l0")
        val lvl3L5 = listOf("m0")
        val lvl3L6 = listOf("n0")
        val lvl3L7 = listOf("o0")
        val lvl3L8 = listOf("p0")
        val rootL = listOf("a0", "a1", "a2", "a3")

        /**
         * Adding each list to its respective path (passed in pairs)
         * check addNodes() documentation
         */
        val tree: Tree<Int> = tree {
            treeName = "Generic Tree"
            root {
                name = "Generic"
                attributes = listOf(1, 2)
            }

            println(rootNode)

            emptyNodes(rootL)
            emptyNode("Empty")
            atPath(0) {
                emptyNodes(lvl1L1)

                // will move to 0,0
                atRelativePath(0) {
                    emptyNodes(lvl2L1)

                    // will move to 0,0,1
                    atRelativePath(1) {
                        emptyNodes(lvl3L2)
                    }
                    emptyNodes("")
                }
                emptyNodes("Empty1", "Empty2")
                emptyNode("")
            }
        }

        tree.setRootName("Generic Root").setRootAttributes(listOf(1, 2, 3))

        val root = tree.root

        /**
         * Add nodes to root
         */
        tree.appendEmptyNamedToPaths(rootL to RootPath)
        tree.appendEmptyNamedToPaths(lvl1L2 to listOf(1), lvl1L3 to listOf(2), lvl1L4 to listOf(3),
            lvl2L2 to listOf(0, 1), lvl2L3 to listOf(0, 2), lvl3L1 to listOf(0, 0, 0), lvl3L3 to listOf(0, 1, 0), lvl3L4 to listOf(0, 1, 1), lvl3L5 to listOf(0, 2, 0),
            lvl3L6 to listOf(0, 2, 1), lvl3L7 to listOf(0, 2, 0), lvl3L8 to listOf(0, 2, 1))

        /**
         * one way to reference a node in the tree
         */
        val node = root.children[0].children[1].children[0]

        /**
         * Other ways to reference a node in the tree
         */
        // Throws if no such path
        val node2 = root.tryGetNode(0, 2, 1)
        // Get from a node
        val node3 = root.getNodeOrNull(0, 2, 1)
        // Get from the tree
        val node4 = tree.getNodeOrNull(listOf(0, 2, 1))

        val attributes = arrayOf(1, 2, 3)

        //some chaining
        node.setAttributes(*attributes).appendChildren(listOf()).setAttributes().setAttributes(*attributes)

        node.attributes

        /**
         * Use the cursor to track your position in the tree and modify it at your current position.
         * All the "cursor" methods' names start with "cursor" for convenience.
         * */
        tree.cursorSet(*node.getPath().toIntArray())
        println(tree.cursorPath)
        // The root has an empty path
        println(tree.root.getPath())
        println(node2.getPath())// should be the same
        println(node3!!.getPath())// should be the same
        println(node4!!.getPath())// should be the same
        /** Some ways to print the path representation below */
        println("Path to ${node.name} from root is ${tree.getStringPath(*node.getPath().toIntArray())}")
        println(
            "Path to $node from root is ${tree.cursorNode.getPath()}, \n which is the same as " + "cursorPath: ${tree.cursorPath}" + "\n which is ${tree.cursorNode.getStringPathFromRoot()}") //the same

        tree.cursorSet(*node2.getPath().toIntArray())

        println(
            "Path to new cursor is ${tree.cursorPath} which is ${tree.getStringPath(*tree.cursorPath.toIntArray())}")

        tree.cursorMoveUp(2)
        println("New cursor position is ${tree.cursorPath} which is ${tree.cursorNode.getStringPathFromRoot()}")

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
         * clone tree (return a deep copy of it)
         */
        val clonedTree = tree.clone()
        /**
         * Adding new node "New Node" to the cloned tree as a last child of clonedTree.root and visualizing the cloned tree
         */
        clonedTree.appendEmptyNamedToPaths(listOf("New Node") to RootPath)
        clonedTree.visualizeTree()
    }
}