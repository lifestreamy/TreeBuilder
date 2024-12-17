package com.github.treebuilder.examples

import com.github.treeBuilder.dsl.tree
import com.github.treeBuilder.getStringPath
import com.github.treeBuilder.printLeavesWithDepth
import com.github.treeBuilder.visualizeTree
import org.junit.jupiter.api.Test

/**
 * An example of building a menu in a declarative way utilizing a Kotlin DSL
 * */
class MenuDeclarative {

    @Test
    fun main() {
        // The order of building blocks matters.
        val tree = tree<String> {
            treeName = "Main Menu" // Empty by default. Helpful to differentiate different Menus
            // Use the root block only once per tree. An exception is raised otherwise.
            root("Menu")
            // Add nodes with no attributes in bulk
            // May later add an option to use emptyNodes(...){ } to denote a context for them clearer
            emptyNodes("Open Info Menu",  //0
                "Friends Actions", //1
                "Notifications Actions", //2
                "Support Developer") //3
            atPath(0) {
                emptyNodes("See Your Info", "Change Your Info", "Go Back")
                // Can use loops for convenience
                (0..1).forEach {
                    atRelativePath(it) {
                        emptyNode("Go Back")
                    }
                }
            }
            atPath(1) {
                emptyNodes("Public friends", "Local friends", "Go Back")
                // Will link a path relative to the current node which was provided by the nearest outer context
                atRelativePath(0) {
                    emptyNodes("My friend list", "Friend requests", "Send friend request", "Remove a friend",
                        "See friend's profile information", "See friend's wishlist", "Go Back")
                    (0..5).forEach {
                        atRelativePath(it) {
                            emptyNode("Go Back")
                        }
                    }
                }
                atRelativePath(1) {
                    emptyNodes("My local friends", "Create a local friend", "Share my local friend",
                        "See local friend profile info", "Go back")
                    (0..3).forEach {
                        atRelativePath(it) {
                            emptyNode("Go Back")
                        }
                    }
                }
            }
            atPath(2) {
                emptyNodes("My active notifications", //0
                    "Bot notifications", //1
                    "Single friend reminders settings", //2
                    "Multiple friend reminders settings", //3
                    "Go Back")
                ((0..1)).forEach {
                    atRelativePath(it) {
                        emptyNode("Go Back")
                    }
                }
                atRelativePath(2) {
                    emptyNodes("My active notifications", "Add a notification", "Change a notification",
                        "Delete a notification", "Go Back")
                    (0..3).forEach {
                        atRelativePath(it) {
                            emptyNode("Go Back")
                        }
                    }
                }
                atRelativePath(3) {
                    emptyNodes("My active notifications", "Modify all notifications",
                        "Add all friends' birthdays to notifications", "Remove all notifications", "Go Back")
                    (0..3).forEach {
                        atRelativePath(it) {
                            emptyNode("Go Back")
                        }
                    }
                }
            }
            atPath(3) {
                emptyNodes("Share this bot", "Donate", "Go Back")
                (0..1).forEach {
                    atRelativePath(it) {
                        emptyNode("Go Back")
                    }
                }
            }

        }
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
         *  Printing all leaf nodes.
         *
         *  Here you can see that we have added a "Go Back" option as a terminating item for each final menu entry
         */
        tree.printLeavesWithDepth()
        /**
         * Print the nodes' names from the root to the node at the path.
         * */
        //This prints "Invalid Path"
        println(tree.getStringPath(0,0,0,0))
        // This one prints the path
        // Menu -> Friends Actions -> Public friends -> My friend list -> Go Back
        println(tree.getStringPath(1,0,0,0))
    }
}