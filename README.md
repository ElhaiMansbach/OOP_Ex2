# OOP_Ex2
## Ex_2 - directed & Weighted Graphs & The pokemons game
*Author: Elhai Mansbach* <br/>
### Part 1: Directed Weighted Graph  *********************************
In this project I implemented algorithms for developing a data structure into an directed weighted graph. <br/>
After the data structure implementation, I implemented a bunch of algorithms on the graph including the ability to duplicate the graph, check connectivity, add node/edge, remove node/edge, check edge's weight and calculate the shortest path between source and destination nodes.

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
1.	**Init:** Init this set of algorithms on a given graph
2. **getGraph:** Return the underlying graph of which this class works
2.	**Copy:** Deep copy of the graph.
3.	**isConnected:** Checks whether there is a valid path from each node to each other node.
4.	**shortestPathDist:** Calculates the length of the shortest path between src to dest.
5.	**shortestPath:** Finds the shortest path (as an ordered list of nodes) between 2 given nodes in the graph.
6.	**Save:** Saves this weighted (directed) graph to the given file name - in JSON format.
7.	**Load:** Loads the graph from the file.

**I have also implemented some auxiliary functions used to implement the above function:**

 **Bfs:**<br/>
I used this function in isConnected function, to check if we visited all the nodes, otherwise the graph is not connected.<br/>
I used the bfs utility function that does breadth traversal across the graph and marks the nodes it visited.<br/>
I created a queue (data structure) and another filed "element" (which save the current node dequeue from the queue).<br/>
The tag of a node is 0 if we have not visited the node yet, otherwise 1.<br/>
I start traversal from the "key" node (which the function gets), set the tag of the node to 1, and inserted him into the queue.<br/>
Then I made a while loop that works as long as the queue is not empty, in the loop I ran over the neighbors of the current "element"  and checked if the tag of each neighbor is equal to 0 (i.e. we had not yet visited it), otherwise we move to the next neighbor.<br/>
If equal to 0 then the node was inserted to the queue and the tag was updated.

**getTranspose:**<br/>
I used getTranspose function as an auxiliary function for isConnected.<br/>
This function gets a graph and turns its rib directions.<br/>

**findMinNode:**<br/>
I used findMinNode function as an auxiliary function for shortestPathDist.<br/>
The function Finds the node with the minimum weight and removes it from the queue.<br/>
The function receives a list of nodes (Queue) and finds from them the node with the lowest weight.<br/>

**Json2Graph:**<br/>
I used Json2Graph function to convert the graph I loaded as a Jason file into a game as a directed_weighted_graph.

### Part 2: The pokemons game
In the Pokemon game you have to manage a group of agents that their goal is to catch as many Pokemons as possible, before that time will run out.<br/>
The more Pokemon you catch, the more points you earn!<br/>
There are 24 different levels((0-23), with increasing difficulty.<br/>
The movement in the board is on a weighted directed graph, on which Pokemon are randomly scattered, which Agents can catch by moving close to them on the graph.<br/>
The game is communicating with the game_service server to track the changes in the game. the api folder hold an interface of the game_service class.<br/>
The game is built on from several already made classes and libraries(mainly libs folder, Arena, CL_Agent, CL_Pokemon).<br/>
All the game elements are found in src\gameClient folder.<br/>
The pokemon game is made from:

## Ex2:***********************<br/>

the main java class launches the game and his associate GUI.<br/> 
the class is responsible to initialize the game and to maintain the GUI.<br/> 
the class contains:<br/>

## MyFrame which extends the class - JFrame:
The main GUI frame class - is responsible to draw the game, utilizes the classes from the util folder.<br/>
the frame will represent the graph, pokemons, the moving agents, level, timer to the end of the game and the total score. <br/>

## MyPanel which extends the class - JPanel:
A GUI window that allows the user to enter his id and choose a game level.<br/>
the panel is made up of a text box for entering the id, a popup menu to choose the level (0-23) and option to login.<br/>
closing the panel will terminate the program.<br/>

## Arena:
a pre built class, that utilizes the util folder in order to build the game's level.<br/>
the class is responsible to hold and track the game's elements since the last move.<br/>
the class also reads JString in order to process the data of the game server.<br/>

## CL_Pokemon classthat implements Comparable:
a pre built class, that represent the pokemons in game.<br/> 
each hold a specific edge, location type and value.<br/> 
the pokemon type and location determine on what edge he is located.<br/> 
the pokemons value determine the score of the capturing agent.<br/> 
the game is remaking the pokemons from json string from the server after every move.<br/> 

## CL_Agent class that implements Comparable:
A pre built class, that represent the games agents.<br/> 
the agent is remade with each move of the game, and is made from the Json String of the game server.<br/> 
the agent contains:<br/> 

1.**_pos:** the current location of the agent.<br/> 
2.**_speed:** the speed is calculated as edge-weight per second, and is increased after he reaches a certain amount of score.<br/> 
3.**_curr_edge:** the edge the agent is currently located.<br/> 
4.**_curr_node:** the node the agent is currently located, or the source of the edge he is currently located.<br/> 

## util folder:
Util hold number of pre built classes.<br/> 
they are responsible to calculate and maintain position of the game's elements.<br/> 
like the location of agents and to calculate how to resize the arena after resizing the game's UI window.<br/> 

the gameClient folder also contains MyFrame, Ex2_client and simpleGameClient, which are pre built classes that the main classes are built on.<br/> 
they are not to be used, only for testing purposes.

# Enjoy the game :)
![image](https://user-images.githubusercontent.com/74247437/102534186-073fac80-40af-11eb-8569-60568ef88368.png)





