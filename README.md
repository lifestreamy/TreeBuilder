# TreeBuilder
This library is useful for easy and fast building of tree structures (made specifically for menus).

It supports imperative, declarative and mixed ways of building the menus, storing current location in the tree for navigation, cloning the trees and much more. 

Each node has a unique path, and can be reached easily.

# Usage example
```
// See GenericMixed in the examples.

val rootL = listOf("a0", "a1", "a2", "a3")

val tree = tree {
            treeName = "Generic Tree"
            root {
                name = "Generic Root"
                attributes = listOf(1, 2, 3) // The type of the tree can be auto-inferred from this list type
            }

            emptyNodes(rootL)
            emptyNode("Empty")
            // will switch context to [Root -> index 0 child node]
            atPath(0) {
                emptyNodes("b0", "b1", "b2")

                // will switch to 0,0
                atRelativePath(0) {
                    emptyNodes("f0", "f1")

                    // will switch to 0,0,1
                    atRelativePath(1) {
                        emptyNodes("j0")
                    }
                    emptyNodes("") 
                }
                emptyNodes("Empty1", "Empty2")
                emptyNode("")
            }
        }
```


# Setup

For now use https://www.jitpack.io to add this library (add the code below to build.gradle), see examples folder for usage scenarios.

[Link to the library on jitpack](https://www.jitpack.io/#lifestreamy/TreeBuilder)

## build.gradle:
```
allprojects 
{
    repositories
    {
        maven { url 'https://jitpack.io' }    
    } 
}
```
```
dependencies 
{

    implementation 'com.github.lifestreamy:TreeBuilder:master-2.0'
    
}
```


Read the documentation in the code, it provides all the needed info. 

# Contribution 

Always open for suggestions and contributions.

For any suggestions and comments open new issues

