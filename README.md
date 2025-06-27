# TreeBuilder

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/io.github.lifestreamy/treebuilder/badge.svg?style=plastic)](https://search.maven.org/artifact/io.github.lifestreamy/treebuilder)  
[![Javadoc](https://javadoc.io/badge2/io.github.lifestreamy/treebuilder/javadoc.svg)](https://javadoc.io/doc/io.github.lifestreamy/treebuilder)

*Convenient documentation on [GitHub Pages](https://lifestreamy.github.io/TreeBuilder/)*

---

This small Kotlin library, originally created mostly for personal use and as an exercise, is useful (or at least was for me) for easy and fast building of tree structures, specifically designed for menus.

It supports imperative, declarative (not fully declarative), and mixed ways of building menus of nodes with attributes. It allows storing the current location in the tree, navigation, cloning trees, and visualizing them.

Each node has a unique path and can be reached by following this path from the root or other nodes.

Regarding declarativeness, there is no full separation of declaration and validation stages, meaning some validation is done during individual declarations, and the order of *some* steps may matter (mostly it does not).

---

## Usage example

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

---

## Setup

Add to your `settings.gradle.kts` :
```
dependencyResolutionManagement {
    repositories {
        mavenCentral()
    }
}
```

Add to your `build.gradle.kts` dependencies block:

```
dependencies {
    implementation("io.github.lifestreamy:treebuilder:2.0.2")
}
```
---

## Contribution

Contributions and suggestions are always welcome!

Please open new issues for any suggestions or comments.

---

