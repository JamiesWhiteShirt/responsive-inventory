package com.jamieswhiteshirt.responsiveinventory.client.itemdiff.diff

import java.util.*


enum class EditScriptOperation(val prefix: String) {
    ADD("+"),
    REMOVE("-"),
    KEEP(""),
}

data class EditScriptItem<T>(val item: T, val operation: EditScriptOperation) {
    override fun toString(): String = "${operation.prefix}$item"
}

/**
    Calculate the edit script between two lists.

    Example:
    before = "bcd"
    after = "abc"

    Represented as the following graph, where diagonals represent equal values.
    Each edge represents an operation in an edit script:
    - A horizontal edge represents removing an item
    - A vertical edge represents adding an item
    - A diagonal edge represents keeping an item

             X
          0 1 2 3
           b c d
      0   O-O-O-O
        a | | | |
      1   O-O-O-O
    Y   b |\| | |
      2   O-O-O-O
        c | |\| |
      3   O-O-O-O

    We must find the shortest path from the upper left corner to the lower right corner.
    We turn the graph sideways and create a new coordinate system:

                    K
             -3-2-1 0 1 2 3

      0          a  O  b
                   / \
      1        b  O   O  c
                 /|\ / \
      2      c  O | O   O  d
               / \|/ \ / \
    L 3       O   O   O   O
               \ /|\ / \ /
      4     .   O | O   O   .
                 \|/ \ /
      5   .   .   O   O   .   .
                   \ /
      6 .   .   .   X   .   .   .

    So that:
    X = (L + K) / 2
    Y = (L - K) / 2

    We build a snake graph to compute and represent the shortest path.
    A snake is a path of zero or more consecutive diagonal edges in the graph.

    A node in the snake graph maps to a snake in the other graph.
    An edge in the snake graph maps to an edge between two snakes in the other graph.
    A path in the snake graph maps to a path of connected snakes in the other graph.

    The snake graph is built iteratively.
    The algorithm terminates when we create a snake containing the goal node.
    The first node maps to the end node of the snake from (0, 0).
    A node connects to the node closest to the goal node of the two above it.

                      0,0
                      0,0
                     /   \
                   0,1   1,0
                   2,3   1,0
                  /   \
                2,4   3,3
                2,4   3,3    ___


             ___   ___   ___   ___


          ...   ___   ___   ___   ...


       ...   ...   ___   ___   ...   ...


    ...   ...   ...   ___   ...   ...   ...

    The above graph maps to the following:

                    K
             -3-2-1 0 1 2 3

      0          a  O  b
                   / \
      1        b  O   O  c
                  |
      2      c  O | O   O  d
                  |
    L 3       O   O   O   O
                  |
      4     .   O | O   O   .
                  |
      5   .   .   O   O   .   .
                 / \
      6 .   .   .   O   .   .   .

    Creating the following shortest path:

             X
          0 1 2 3
           b c d
      0   O O O O
        a |
      1   O O O O
    Y   b  \
      2   O O O O
        c    \
      3   O O O-O

    Creating the following edit script:

    +a
    b
    c
    -d
 */
fun <T> editScript(before: List<T>, after: List<T>): List<EditScriptItem<T>> {
    // the resulting d <= dMax
    val levelMax = before.size + after.size

    // Given k, calculates the longest snake as an x value from the supplied x value
    fun snake(fromX: Int, k: Int): Int {
        var x = fromX
        while (x < before.size && x - k < after.size && before[x] == after[x - k]) {
            x++
        }
        return x
    }

    val arrayLength = ((levelMax + 1) * (levelMax + 2)) / 2
    val xMins = IntArray(arrayLength)
    // Array of x values for the advancing frontier for each k of each level
    val xMaxs = IntArray(arrayLength)
    // Links each node with the previous node in the shortest path
    val parents = IntArray(arrayLength)

    xMaxs[0] = snake(0, 0)
    if (xMaxs[0] == before.size && xMaxs[0] == after.size) {
        // The lists are identical
        return before.map { EditScriptItem(it, EditScriptOperation.KEEP) }
    }

    // Index of the above (vertical) node of the current node
    var aboveNode = 0
    // Index of the current node
    var node = 1

    for (level in 1..levelMax) {
        for (k in -level..level step 2) {
            val which = when {
                k == -level || (k != level && xMaxs[aboveNode - 1] < xMaxs[aboveNode]) -> {
                    0 // the edge is vertical (+y)
                }
                else -> {
                    1 // the edge is horizontal (+x)
                }
            }
            // Index of the previous node in the path
            val parentNode = aboveNode - which
            // Min X position of the snake
            val xMin = xMaxs[parentNode] + which // yMin = xMin - k
            // Max X position of the snake
            val xMax = snake(xMin, k) // yMax = xMax - k

            xMins[node] = xMin
            xMaxs[node] = xMax
            parents[node] = parentNode

            if (xMax == before.size && xMax - k == after.size) {
                val result = ArrayList<EditScriptItem<T>>()

                var k = k

                for (l in level downTo 1) {
                    (xMaxs[node] - 1 downTo xMins[node]).mapTo(result) { EditScriptItem(before[it], EditScriptOperation.KEEP) }

                    val which = aboveNode - parents[node]
                    result.add(if (which == 0) {
                        EditScriptItem(after[xMins[node] - k - 1], EditScriptOperation.ADD)
                    } else {
                        EditScriptItem(before[xMins[node] - 1], EditScriptOperation.REMOVE)
                    })

                    node = parents[node]
                    aboveNode = node - l + 1
                    k -= which * 2 - 1
                }

                (xMaxs[node] - 1 downTo xMins[node]).mapTo(result) { EditScriptItem(before[it], EditScriptOperation.KEEP) }

                result.reverse()

                return result
            }

            node++
            aboveNode++
        }

        aboveNode--
    }

    throw Error("The algorithm should have terminated by now")
}
