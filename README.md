# Supply CHain Algorithm

## Overview
This is a Java implementation of an algorithm that solves the supply chain problem. The supply chain problem is a classic optimization problem that involves determining the most efficient way to move goods from suppliers to customers, while minimizing costs and maximizing profits.

The specific problem has the following rules for different types of nodes:
* A port cannot connect to a store or another port.
* A rail-hub cannot connect to a store.
* A store cannot connect to another store, except through their associated DC.
* A dist-center can only connect to a store that is associated with it.


## Implementation Details
The program begins by parsing the input data to create three hash maps:
* hmap_name_type: maps the name of a node to its type (port, rail-hub, store, or dist-center).
* hmap_name_index: maps the name of a node to its index in the inverted_index array.
* hmap_store_dc: maps the name of a store to the name of its associated distribution center (DC).

The code then parses the edges between the nodes, discarding any edges that do not connect valid pairs of nodes according to rules in the previous section.


The code then uses the **Kruskal's** algorithm to find the **minimum spanning tree** of the graph defined by the remaining edges. The edges are sorted by weight using a custom comparator, and the algorithm iterates over the sorted edges, adding each edge to the tree if it does not create a cycle with the edges already in the tree. The algorithm uses a **disjoint-set** data structure to keep track of which nodes belong to which connected components.

Finally, the code returns the **sum of the weights** of the edges in the minimum spanning tree.

## Run the Prorgam
Users can choose which test file to use by passing it via command line. If there's no user input, the program defaults to use ```Complete Graph 14.txt```.
```
javac Main.java Supply.java

java Main.java
```

Output:
```
User did not specify test file, using default (Complete Graph 14.txt).
edge weight sum: 19
time: 0.032
```
