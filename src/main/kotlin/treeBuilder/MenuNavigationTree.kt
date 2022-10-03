package mainBot

abstract class MenuNavigationTree {

    class TreeNode<T>(private var value: T, val name: T = value) {
        private var parent: TreeNode<T>? = null
        var childrenList: MutableList<TreeNode<T>> = mutableListOf()
        var childrenNamesList: MutableList<T> = mutableListOf()
        private fun addChild(node: TreeNode<T>) {
            node.parent = this
            childrenList.add(node)
            childrenNamesList.add(node.name)
        }

        fun addChildren(children: MutableList<T>) =
            children.forEach {
                addChild(TreeNode(it))
            }

        fun hasChildren(): Boolean = childrenList.isNotEmpty()

        private fun hasParent(): Boolean = parent !== null

        fun getPath(): MutableList<TreeNode<T>> {
            val path = mutableListOf<TreeNode<T>>()
            if (this.hasParent()) {
                path.let { list1 -> this.parent!!.getPath().let(list1::addAll) }
            }
            path.add(this)
            return path
        }

        fun getNode(nodesIndices: MutableList<Int>? = null) : TreeNode<T> {
            if (nodesIndices.isNullOrEmpty()) return this
            return if (nodesIndices.size==1)
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

    companion object {
        private var pathToCurrentNode = mutableListOf<TreeNode<String>>()
        private fun getStringPath(path: MutableList<TreeNode<String>>): String {
            var s = ""
            path.forEach {
                s += " -> ${it.name}"
            }
            return s
        }


        private fun movePointerUp() {
            if (pathToCurrentNode.isNotEmpty()) {
                pathToCurrentNode.removeLast()
            }
        }

        private fun addNodes(
            rootNode: TreeNode<String>,
            path: MutableList<Int>, nodes: MutableList<String>
        ) {
            var node: TreeNode<String> = rootNode
            path.forEach {
                node = node.childrenList[it]
            }
            node.addChildren(nodes)
        }



        lateinit var menu: TreeNode<String>
            private set

        fun buildMenu(): TreeNode<String> {
            val menu = TreeNode("Main Menu")
            val menuList = mutableListOf(
                "Open Info Menu",  //0
                "Friends Actions", //1
                "Notifications Actions", //2
                "Support Developer" //3
            )
            menu.addChildren(menuList)
            val infoMenuList = mutableListOf(
                "See Your Info",
                "Change Your Info",
                "Go back from info"
            )
            addNodes(menu, mutableListOf(0), infoMenuList)
            val friendsActions = mutableListOf(
                "Public friends",
                "Local friends",
                "Go back from friends"
            )
            addNodes(menu, mutableListOf(1), friendsActions)
            val publicFriends = mutableListOf(
                "My friend list",
                "Friend requests",
                "Send friend request",
                "Remove a friend",
                "See friend's profile information",
                "See friend's wishlist",
                "Go back from public friends"
            )
            addNodes(menu, mutableListOf(1,0), publicFriends)
            val localFriends = mutableListOf(
                "My local friends",
                "Create a local friend",
                "Share my local friend",
                "See local friend profile info",
                "Go back from local friends"
            )
            addNodes(menu, mutableListOf(1,1),localFriends)
            val notificationsActions = mutableListOf(
                "My active notifications", //0
                "Bot notifications", //1
                "Single friend reminders settings", //2
                "Multiple friend reminders settings", //3
                "Go Back from notifications"
            )
            addNodes(menu, mutableListOf(2), notificationsActions)
            val supportDeveloperActions = mutableListOf(
                "Share this bot",
                "Donate",
                "Go back from support"
            )
            addNodes(menu, mutableListOf(3), supportDeveloperActions)
            val singleNotificationsActions = mutableListOf(
                "My active notifications",
                "Add a notification",
                "Change a notification",
                "Delete a notification",
                "Go Back"
            )

            addNodes(menu, mutableListOf(2, 2), singleNotificationsActions)
            val multipleNotificationsActions = mutableListOf(
                "My active notifications",
                "Modify all notifications",
                "Add all friends' birthdays to notifications",
                "Remove all notifications",
                "Go Back"
            )
            addNodes(menu, mutableListOf(2, 3), multipleNotificationsActions)
            this.menu = menu
            return menu
        }

        @JvmStatic
        fun main(args: Array<String>) {
            buildMenu()
            println(menu)
            val node =
                menu.childrenList[2].childrenList[3].childrenList[2]
            val node2 = menu.getNode(mutableListOf(2,3,1))
            pathToCurrentNode = node.getPath()
            println("Path to ${node.name} is " + getStringPath(pathToCurrentNode))
            pathToCurrentNode = node2.getPath()
            println("Path to ${node.name} is " + getStringPath(pathToCurrentNode))
        }
    }
}
