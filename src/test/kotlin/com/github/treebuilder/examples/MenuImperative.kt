package com.github.treebuilder.examples

import com.github.treeBuilder.Tree
import com.github.treeBuilder.Tree.Companion.RootPath
import com.github.treeBuilder.printLeavesWithDepth
import com.github.treeBuilder.visualizeTree
import org.junit.jupiter.api.Test

/** An example of building a [Tree] Menu imperatively, i.e. by executing commands in sequence.
 *
 *  This might be unsafe, so the new DSL approach is recommended.
 *  @see [GenericMixed]*/
class MenuImperative {
    @Test
    fun main() {
        val tree = Tree<String>("Main Menu").apply { root.name = "Menu" }
        val menuList = listOf(
            "Open Info Menu",  //0
            "Friends Actions", //1
            "Notifications Actions", //2
            "Support Developer" //3
                             )
        tree.appendEmptyNamedToPaths(menuList to RootPath)
        val infoMenuList = listOf("See Your Info", "Change Your Info", "Go back from info")
        val friendsActions = listOf("Public friends", "Local friends", "Go back from friends")
        val notificationsActions = listOf(
            "My active notifications", //0
            "Bot notifications", //1
            "Single friend reminders settings", //2
            "Multiple friend reminders settings", //3
            "Go Back from notifications"
                                         )
        val supportDeveloperActions = listOf("Share this bot", "Donate", "Go back from support")
        /**
         * Adding each list to its respective path (passed in pairs)
         * check addNodes() documentation
         */
        tree.appendEmptyNamedToPaths(
            infoMenuList to listOf(0), friendsActions to listOf(1), notificationsActions to listOf(2),
            supportDeveloperActions to listOf(3)
                                    )

        val publicFriends = listOf(
            "My friend list", "Friend requests", "Send friend request", "Remove a friend",
            "See friend's profile information", "See friend's wishlist", "Go back from public friends"
                                  )
        val localFriends = listOf(
            "My local friends", "Create a local friend", "Share my local friend", "See local friend profile info",
            "Go back from local friends"
                                 )
        /**
         * Adding each list to its respective path (passed in pairs)
         * check addNodes() documentation
         */
        tree.appendEmptyNamedToPaths(
            publicFriends to listOf(1, 0), localFriends to listOf(1, 1)
                                    )
        val singleNotificationsActions = listOf(
            "My active notifications", "Add a notification", "Change a notification", "Delete a notification", "Go Back"
                                               )
        val multipleNotificationsActions = listOf(
            "My active notifications", "Modify all notifications", "Add all friends' birthdays to notifications",
            "Remove all notifications", "Go Back"
                                                 )
        /**
         * Adding each list to its respective path (passed in pairs)
         * check addNodes() documentation
         */
        tree.appendEmptyNamedToPaths(
            singleNotificationsActions to listOf(2, 2), multipleNotificationsActions to listOf(2, 3)
                                    )

        /**
         *  Get visualization of the whole tree
         *  How to read visualization:
         *  All nodes with (x) = 1 + (x) of the node left to it
         *  and that are above that node are its children
         */
        tree.visualizeTree()
        /**
         *  Printing all leaf nodes
         */
        tree.printLeavesWithDepth()
    }
}