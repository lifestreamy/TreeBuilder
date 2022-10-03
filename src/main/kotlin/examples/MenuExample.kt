package examples

import treeBuilder.TreeBuilder
import java.lang.reflect.Type

class MenuExample {
    companion object {

        @JvmStatic
        fun main(args: Array<String>) {
            val treeBuilder = TreeBuilder<String>()
            treeBuilder.addRoot(TreeBuilder.TreeNode("menu"))
            val menuList = mutableListOf(
                "Open Info Menu",  //0
                "Friends Actions", //1
                "Notifications Actions", //2
                "Support Developer" //3
            )
            treeBuilder.root.addChildren(menuList)
            val infoMenuList = mutableListOf(
                "See Your Info",
                "Change Your Info",
                "Go back from info"
            )
            val friendsActions = mutableListOf(
                "Public friends",
                "Local friends",
                "Go back from friends"
            )
            val notificationsActions = mutableListOf(
                "My active notifications", //0
                "Bot notifications", //1
                "Single friend reminders settings", //2
                "Multiple friend reminders settings", //3
                "Go Back from notifications"
            )
            val supportDeveloperActions = mutableListOf(
                "Share this bot",
                "Donate",
                "Go back from support"
            )
            treeBuilder.addNodes(
                treeBuilder.root, Pair(intArrayOf(0), infoMenuList),
                Pair(intArrayOf(1), friendsActions), Pair(
                    intArrayOf(2), notificationsActions
                ),
                Pair(intArrayOf(3),
                    supportDeveloperActions)
            )
            val publicFriends = mutableListOf(
                "My friend list",
                "Friend requests",
                "Send friend request",
                "Remove a friend",
                "See friend's profile information",
                "See friend's wishlist",
                "Go back from public friends"
            )
            val localFriends = mutableListOf(
                "My local friends",
                "Create a local friend",
                "Share my local friend",
                "See local friend profile info",
                "Go back from local friends"
            )
            treeBuilder.addNodes(
                treeBuilder.root, Pair(
                    intArrayOf(1, 0), publicFriends
                ), Pair(intArrayOf(1, 1), localFriends)
            )
            val singleNotificationsActions = mutableListOf(
                "My active notifications",
                "Add a notification",
                "Change a notification",
                "Delete a notification",
                "Go Back"
            )
            val multipleNotificationsActions = mutableListOf(
                "My active notifications",
                "Modify all notifications",
                "Add all friends' birthdays to notifications",
                "Remove all notifications",
                "Go Back"
            )
            treeBuilder.addNodes(
                treeBuilder.root,
                Pair(intArrayOf(2, 2), singleNotificationsActions),
                Pair(intArrayOf(2, 3), multipleNotificationsActions)
            )

            /**
             * one way to reference a node in the tree
             */
            val node =
                treeBuilder.root.childrenList[2].childrenList[3].childrenList[2]

            /**
             * Another way to reference node in a tree
             */
            val node2 = treeBuilder.root.getNode(mutableListOf(2, 3, 1))

            /**
             * Search for the first leaf of the tree
             */
//            val node3 = treeBuilder.searchFirstLeaf()

            treeBuilder.pathToCurrentNode = node.getPath()
            println("Path to ${node.name} is ${treeBuilder.getStringPath(treeBuilder.pathToCurrentNode)}")
            treeBuilder.pathToCurrentNode = node2.getPath()
            println("Path to ${node2.name} is ${treeBuilder.getStringPath(treeBuilder.pathToCurrentNode)}")
            treeBuilder.movePointerUp()
            println("Path to parent of ${node2.name} is ${treeBuilder.getStringPath(treeBuilder.pathToCurrentNode)}")
//            println("Path to the first leaf ${node3.name} is ${treeBuilder.getStringPath(node3.getPath())}")
            val allNodes = treeBuilder.getAllNodes()
            println("All nodes (amount = ${allNodes.size}) are:")

            allNodes.forEach {
                println(it.name)
            }
            val allLeafNodes = treeBuilder.getAllLeafs(allNodes)
            println("All leaf nodes (amount = ${allLeafNodes.size}) are:")
            allLeafNodes.forEach {
                println(it.name)
            }
//            val root
        }
    }

}