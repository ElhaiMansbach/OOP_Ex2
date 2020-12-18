# OOP_Ex2
## Ex_1 - directed & Weighted Graphs
*Author: Elhai Mansbach* <br/>
### Part One
In this project I implemented algorithms for developing a data structure into an directed weighted graph. <br/>
After the data structure implementation, I implemented a bunch of algorithms on the graph including the ability to duplicate the graph, check connectivity, check edge's weight and calculate the shortest path between source and destination nodes.

**In the first part of the project I have 4 classes:**

## Data Structures:

### NodeData:<br/>
*The node_data interface is implemented in NodeData class:*
This class represents the set of operations applicable on a node in an directed weighted graph.
Each node has a unique key plus four additional node fields (weight, location, information and tag) that were used only during the algorithms.

### EdgeData:<br/>
*The edge_data interface is implemented in EdgeData class:*
This class represents the set of operations applicable on a edge in an directed weighted graph.
Each edge has a key source, key destination,weight, info and tag that were used only during the algorithms.

### DWGraph_DS:<br/>
*The directed_weighted_graph interface is implemented in WGraph_DS class:*
This class represents an directional weighted graph.
It supports a large number of nodes.
The nodes and the edges are implemented in a data structure – HashMap.
There are functions for adding / removing nodes and edges, getting the node data by his key, obtaining lists of nodes and the node's neighbors list, checking if there is an edge between two nodes, checking the data of edge between to nodes, obtaining the amount of nodes/edges there are in the graph, obtaining the amount of actions done on the graph (saved as MC).

## Algorithms:

### DWGraph_Algo:<br/>
*The dw_graph_algorithms interface is implemented in DWGraph_Algo class:*
The Graph_Algo object contains a graph to activate the algorithms on.

**This class represents the Graph Theory algorithms including:**
1.	**Init** - Init this set of algorithms on a given graph
2. **getGraph** - Return the underlying graph of which this class works
2.	**Copy** - Deep copy of the graph.
3.	**isConnected** - Checks whether there is a valid path from each node to each other node.
4.	**shortestPathDist** - Calculates the length of the shortest path between src to dest.
5.	**shortestPath** - Finds the shortest path (as an ordered list of nodes) between 2 given nodes in the graph.
6.	**Save** - Saves this weighted (directed) graph to the given file name - in JSON format.
7.	**Load** - Loads the graph from the file.

**I have also implemented some auxiliary functions used to implement the above function:**

 **Bfs:**<br/>
I used this function in isConnected function, to check if we visited all the nodes, otherwise the graph is not connected.
I used the bfs utility function that does breadth traversal across the graph and marks the nodes it visited.
I created a queue (data structure) and another filed "element" (which save the current node dequeue from the queue). The tag of a node is 0 if we have not visited the node yet, otherwise 1. I start traversal from the "key" node (which the function gets), set the tag of the node to 1, and inserted him into the queue. Then I made a while loop that works as long as the queue is not empty, in the loop I ran over the neighbors of the current "element"  and checked if the tag of each neighbor is equal to 0 (i.e. we had not yet visited it), otherwise we move to the next neighbor. If equal to 0 then the node was inserted to the queue and the tag was updated.

**getTranspose:**<br/>
I used getTranspose function as an auxiliary function for isConnected.
This function gets a graph and turns its rib directions.

**findMinNode:**<br/>
I used findMinNode function as an auxiliary function for shortestPathDist.
The function Finds the node with the minimum weight and removes it from the queue.
The function receives a list of nodes (Queue) and finds from them the node with the lowest weight.

**Json2Graph:**<br/>
I used Json2Graph function to convert the graph I loaded as a Jason file into a game as a directed_weighted_graph.

**In the second part of the project:**

