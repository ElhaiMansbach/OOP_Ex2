# OOP_Ex2
## Ex_2 - directed & Weighted Graphs & The pokemons game
*Author: Elhai Mansbach* <br/>

# Part 1: Directed Weighted Graph  
![](https://www.researchgate.net/profile/Yilun_Shang/publication/271944978/figure/fig2/AS:669349501231106@1536596765134/A-weighted-directed-graph-G-with-six-vertices.png)
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
3.	**Copy:** Deep copy of the graph.
4.	**isConnected:** Checks whether there is a valid path from each node to each other node.
5.	**shortestPathDist:** Calculates the length of the shortest path between src to dest.
6.	**shortestPath:** Finds the shortest path (as an ordered list of nodes) between 2 given nodes in the graph.
7.	**Save:** Saves this weighted (directed) graph to the given file name - in JSON format.
8.	**Load:** Loads the graph from the file.

<div align="center">
  <div style="display: flex; justify-content: space-between;">
    <img src="https://media1.giphy.com/media/jP4pPl5z1lccFcGvR0/200w.webp" alt="GIF">
    <img src="https://media1.giphy.com/media/jP4pPl5z1lccFcGvR0/200w.webp" alt="GIF">
  </div>
</div>

<h1 >Part 2: The Pokémon Game</h1>


In the Pokemon game you have to manage a group of agents that their goal is to catch as many Pokemons as possible, before that time will run out.<br/>
The more Pokemon you catch, the more points you earn!<br/>
There are 24 different levels((0-23), with increasing difficulty.<br/>
The movement in the board is on a weighted directed graph, on which Pokemon are randomly scattered, which Agents can catch by moving close to them on the graph.<br/>
The game is communicating with the game_service server to track the changes in the game. the api folder hold an interface of the game_service class.<br/>
The game is built on from several already made classes and libraries(mainly libs folder, Arena, CL_Agent, CL_Pokemon).<br/>
All the game elements are found in src\gameClient folder.<br/>
The pokemon game is made from:

### GameClient: <br/>
The gameClient folder also contains MyFrame and simpleGameClient, which are pre built classes that the main classes are built on.<br/> 
they are not to be used, only for testing purposes.

**The main class- Ex2:**<br/>
The main java class launches the game and his associate GUI.<br/> 
the class is responsible to initialize the game and to maintain the GUI.<br/> 
the class contains:<br/>

**MyFrame & MyPanel**<br/>
 Displays the game in JAVA (Using JFrame and JPanel).
 
 
**SimpleGameClient**<br/>
This class represents the simplest "Client-Game" main class which uses the "server for moving the "Agents".
*Note: this code is a very simple no algorithm no threaded mechanism - it is presented just to show the basic use of the "server".

### Elements: 
**Arena:**
A class that utilizes the util folder in order to build the game's level.<br/>
The class is responsible to hold and track the game's elements since the last move.<br/>
The class also reads Json in order to process the data of the game server.<br/>

**CL_Pokemon:**
A class that represent the pokemons in game.<br/> 
Each hold a specific edge, location type and value.<br/> 
The pokemon type and location determine on what edge he is located.<br/> 
The pokemons value determine the score of the capturing agent.<br/> 
The game is remaking the pokemons from json string from the server after every move.<br/> 

**CL_Agent:**
A class that represent the games agents.<br/> 
The agent is remade with each move of the game, and is made from the Json String of the game server.<br/> 
The agent contains:<br/> 


## util:
Util hold number of pre built classes.<br/> 
they are responsible to calculate and maintain position of the game's elements.<br/> 
like the location of agents and to calculate how to resize the arena after resizing the game's UI window.<br/> 

* Range - represents a simple 1D range of shape [min,max].
* Point3D - represents a 3D point in space.
* Point2D - represents a 2D Range, composed from two 1D Ranges.
* Range2Range - represents a simple world 2 frame (w2f) conversion (both ways).

## Tests:
In this class there are some JUNIT tests (using JUNIT 5 version). <br/>

## Data:<br />
In this package you will find the json files represntes the graphs.<br />


## Resources:<br />
In this package you will find the elemnents images.<br />

## Libs:<br />
* Ex2_Server_v0.13.jar
* annotations-13.0.jar
* java-json.jar
* kotlin-stdlib-1.3.72.jar
* kotlin-stdlib-common-1.3.70.jar
* okhttp-4.8.0.jar
* okio-2.7.0.jar

## Docs:<br />
In this package you will find the all the documention of the java project.<br />


# Enjoy the game :)
![image](https://user-images.githubusercontent.com/74247437/102534186-073fac80-40af-11eb-8569-60568ef88368.png)




