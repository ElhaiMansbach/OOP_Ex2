package gameClient;

import Server.Game_Server_Ex2;
import api.*;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import static gameClient.Arena.*;

public class Ex2 implements Runnable {
    private static MyFrame _win;
    private static Arena _ar = new Arena();
    private static game_service game;
    private static DWGraph_Algo algo = new DWGraph_Algo();
    private static ArrayList<Integer> chosenDest;


    private static int id = -1, ind = 0, amountOfAgents = 0;
    private static long dt = 100;
    private static int scenario_num = 0;


    public static void main(String[] args) {
        if (args.length == 2) { //Gets values from the command line
            id = Integer.parseInt(args[0]);
            scenario_num = Integer.parseInt(args[1]);
        } else {  //Gets values from user on a panel screen
            id = MyPanel.idPanel();
            scenario_num = MyPanel.levelPanel();
        }
        Thread client = new Thread(new Ex2());
        client.start();
    }

    @Override
    public void run() {
        game = Game_Server_Ex2.getServer(scenario_num); //You have [0,23] games
        game.login(id); //Updates your game result on the server

        String g = game.getGraph();
        System.out.println(g);
        directed_weighted_graph gg = algo.Json2Graph(g);

        init(game);

        game.startGame();
        _win.setTitle("OOP- directed weighted graph- catch the Pokemon's " + game.toString());

        int count = 0;
        while (game.isRunning()) { //As long as the game is running
            if (dt == 50) count++;
            if (dt == 50 && count == 2) { //Keep dt=50 for 2 times
                dt = 100;
                count = 0;
            }
            chosenDest = new ArrayList<>(); //A list with the  chosen destination from move function
            moveAgents(game, gg);
            try {
                if (ind % 1 == 0) {
                    _win.repaint();
                }
                Thread.sleep(dt);
                ind++;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        MyPanel.resultsPanel();
        System.out.println(_ar.getGame().toString());
        System.exit(0);
    }

    /**
     * Initialize the given game
     *
     * @param game represent the game server
     */
    public static void init(game_service game) {
        String g = game.getGraph();
        String fs = game.getPokemons();
        directed_weighted_graph gg = algo.Json2Graph(g);

        _ar = new Arena(game);
        _ar.setGraph(gg);
        _ar.setPokemons(Arena.json2Pokemons(fs));
        _win = new MyFrame(gg);
        _win.setSize(1000, 700);
        _win.update(_ar);
        MyPanel panel = new MyPanel(gg);
        panel.update(_ar);
        panel.paint(_win.getGraphics());
        _win.add(panel);

        _win.show();
        String info = game.toString();
        JSONObject line;
        try {
            line = new JSONObject(info);
            JSONObject ttt = line.getJSONObject("GameServer");
            amountOfAgents = ttt.getInt("agents");
            System.out.println(info);
            System.out.println(game.getPokemons());
            ArrayList<CL_Pokemon> cl_fs = Arena.json2Pokemons(game.getPokemons());
            for (CL_Pokemon cl_f : cl_fs) {
                Arena.updateEdge(cl_f, gg);
            }
            locateAgents(gg, cl_fs);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Initialize the given game without show the frame
     *
     * @param game represent the game server
     */
    public static void initWithoutFrame(game_service game) {
        String g = game.getGraph();
        String fs = game.getPokemons();
        directed_weighted_graph gg = algo.Json2Graph(g);

        _ar = new Arena(game);
        _ar.setGraph(gg);
        _ar.setPokemons(Arena.json2Pokemons(fs));
        String info = game.toString();
        JSONObject line;
        try {
            line = new JSONObject(info);
            JSONObject ttt = line.getJSONObject("GameServer");
            amountOfAgents = ttt.getInt("agents");
            System.out.println(info);
            System.out.println(game.getPokemons());
            ArrayList<CL_Pokemon> cl_fs = Arena.json2Pokemons(game.getPokemons());
            for (CL_Pokemon cl_f : cl_fs) {
                Arena.updateEdge(cl_f, gg);
            }
            locateAgents(gg, cl_fs);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    /**
     * Moves each of the agents along the edge,
     * in case the agent is on a node the next destination (next edge) is chosen by an algorithm
     * in NextNode method.
     *
     * @param game represents the gane server
     * @param gg   represents the graph
     */
    private static void moveAgents(game_service game, directed_weighted_graph gg) {
        algo = new DWGraph_Algo(gg);
        String lg = game.move();
        List<CL_Agent> log = Arena.getAgents(lg, gg);
        _ar.setAgents(log);
        String fs = game.getPokemons();
        List<CL_Pokemon> ffs = Arena.json2Pokemons(fs);

        _ar.setPokemons(ffs);

        for (CL_Agent ag : log) { //Go through the number of agents
            int id = ag.getID();
            int dest = ag.getNextNode(); //Updates the destination node
            int src = ag.getSrcNode(); //Updates the source node
            double v = ag.getValue();
            if (dest == -1) {
                dest = nextNode(gg, src, ag);
                game.chooseNextEdge(ag.getID(), dest);
                chosenDest.add(dest); //Add the chosen destention from nextNode function
                System.out.println("Agent: " + id + ", val: " + v + "   turned to node: " + dest);
            }
        }
        resetEdgesTag(); //Reset all edges to be no targeted.
    }

    /**
     * Move the agents according to a wise algorithm.
     * Each agent move to the closest pokemon which unselected by another agent.
     * If the agent is closet to the highest valued edge and his speed is at most 4,
     * the agent move to that edge instead.
     *
     * @param g   represents the graph
     * @param src represents the current node of the agent
     * @return the destination node of the current agent
     */
    private static int nextNode(directed_weighted_graph g, int src, CL_Agent agent) {
        edge_data maxEdge;
        int dest;
        edge_data edgeDest;
        int agentID = agent.getID();
        HashMap<Integer, CL_Pokemon> nodeOfPokemons = nodeOfPokemons(g);

        if (nodeOfPokemons.containsKey(src)) { //The agent is on a src node of a pokemon edge.
            edgeDest = bestAdj(src); //Find the highest valued adjacent edge.
            dest = bestAdj(src).getDest();
            agent.set_curr_pokemon(getPokemonOnEdge(edgeDest, g));
            if (distToPokemon(agent, agent.get_curr_pokemon()))
                if (distToPokemon(agent, agent.get_curr_pokemon())) { //Close to the location of pokemon
                    dt = 50;
                }
            return dest;
        } else {
            //Move the closet agent to the highest valued edge , only when speed is at most 4.
            maxEdge = getMaxEdge(g);
            int agentMax = findClosetAgent(maxEdge, g);
            if (agent.getSpeed() <= 4 && agentMax == agentID) {
                maxEdge.setTag(1);
                if (algo.shortestPathDist(src, maxEdge.getSrc()) != 0) {
                    dest = algo.shortestPath(src, maxEdge.getSrc()).get(1).getKey();
                    if (!chosenDest.contains(dest)) { //There is no other agent goes to this node
                        edgeDest = maxEdge;
                        edgeDest.setTag(1); //Update the edge has targeted
                        agent.set_curr_pokemon(getPokemonOnEdge(edgeDest, g));
                        if (distToPokemon(agent, agent.get_curr_pokemon())) {//Close to the location of pokemon
                            dt = 50;
                        }
                        return dest;
                    }
                }
            }
            //Find the closet pokemon to the agent
            edgeDest = closetPokemon(g, src);
            if (edgeDest != null) {
                dest = algo.shortestPath(src, edgeDest.getSrc()).get(1).getKey();
                if (!chosenDest.contains(dest)) { //There is no other agent goes to this node
                    edgeDest.setTag(1); //Update the edge has targeted
                    agent.set_curr_pokemon(getPokemonOnEdge(edgeDest, g));
                    if (distToPokemon(agent, agent.get_curr_pokemon())) {//Close to the location of pokemon
                        dt = 50;
                    }
                    return dest;
                }
            } else {
                //Finds a neighbor node (of the agent node) where no agent goes.
                for (edge_data e : algo.getG().getE(src))
                    if (!chosenDest.contains(e.getDest())) {
                        e.setTag(1);
                        return e.getDest();
                    }
            }
        }
        return -1;
    }

    /**
     * Locate the agents on the graph next to the highest valued pokemons.
     *
     * @param gg       represents the graph
     * @param pokemons represents the list of pokemons
     */
    public static void locateAgents(directed_weighted_graph gg, List<CL_Pokemon> pokemons) {
        ArrayList<CL_Agent> agents = new ArrayList<>();
        Collections.sort(pokemons); //Sort the pokemon list by their values
        for (int a = 0; a < amountOfAgents; a++) {
            int ind = a % pokemons.size();
            CL_Pokemon c = pokemons.get(ind);
            int nn = c.get_edge().getDest();
            if (c.getType() < 0) {
                nn = c.get_edge().getSrc();
            }
            game.addAgent(nn);
            agents.add(new CL_Agent(gg, nn));
        }
        _ar.setAgents(agents);

    }

    /**
     * Finds the node which we need to get in order to eat the pokemon.
     *
     * @param g represents the graph
     * @param p represents the given pokemon.
     * @return the found node key, returns -1 if the node cannot be found.
     */
    public static int findPokemonSrc(directed_weighted_graph g, CL_Pokemon p) {
        double type = p.getType();
        for (node_data n : g.getV()) {
            for (edge_data e : g.getE(n.getKey())) {
                if (isOnEdge(p.getLocation(), e, p.getType(), g)) {
                    if (type == -1)
                        return Math.max(e.getSrc(), e.getDest());
                    return Math.min(e.getSrc(), e.getDest());
                }
            }
        }
        return -1;
    }

    /**
     * Connects each pokemon to his edge source node.
     *
     * @param g represents the graph
     * @return an hash map of nodes and their pokemons.
     */
    private static HashMap<Integer, CL_Pokemon> nodeOfPokemons(directed_weighted_graph g) {
        List<CL_Pokemon> pokemons = Arena.json2Pokemons(game.getPokemons());
        HashMap<Integer, CL_Pokemon> nodes = new HashMap<>();
        int key;
        for (CL_Pokemon p : pokemons) {
            key = findPokemonSrc(g, p);
            if (key != -1)
                nodes.put(key, p);
        }
        return nodes;
    }

    /**
     * Finds the adjacent edge with the highest pokemon values.
     *
     * @param src represents the agent node.
     * @return the destination node of the edge found .
     */
    private static edge_data bestAdj(int src) {
        List<CL_Pokemon> pokemons = Arena.json2Pokemons(game.getPokemons());
        DWGraph_DS g = (DWGraph_DS) algo.getG();
        double maxVal = 0;
        int maxDest = -1;
        for (edge_data edge : g.getE(src)) {
            double currVal = 0;
            for (CL_Pokemon p : pokemons) {
                if (isOnEdge(p.getLocation(), edge, p.getType(), g))
                    currVal += p.getValue();
            }
            if (maxVal < currVal) {
                maxVal = currVal;
                maxDest = edge.getDest();
            }
        }
        g.getEdge(src, maxDest).setTag(1);
        return g.getEdge(src, maxDest);
    }

    /**
     * Finds the closet pokemon to the agent.
     *
     * @param g   represents the graph
     * @param src represents the agent node.
     * @return the source node of the closet pokemon edge.
     */
    private static edge_data closetPokemon(directed_weighted_graph g, int src) {
        double min = Double.POSITIVE_INFINITY;
        int dest = -1;
        double dist;
        EdgeData edge, edgeClosest = null;
        HashMap<Integer, CL_Pokemon> nodes = nodeOfPokemons(g);
        for (Integer node : nodes.keySet()) {
            dist = algo.shortestPathDist(src, node);
            edge = (EdgeData) edgeOfPokemon(g, nodes.get(node));
            if (dist != -1 && dist < min) {
                assert edge != null;
                if (edge.getTag() == 0 && src != node) {
                    min = dist;
                    dest = node;
                    edgeClosest = edge;
                }
            }
        }
        if (dest == -1) return null;
        return edgeClosest;
    }

    /**
     * Initializes all the edges to be unvisited.
     */
    private static void resetEdgesTag() {
        for (node_data n : algo.getG().getV())
            for (edge_data e : algo.getG().getE(n.getKey()))
                e.setTag(0);
    }

    /**
     * Finds the edge on which the pokemon is located.
     *
     * @param p represents the given pokemon.
     * @return the edge of the given pokemon.
     */
    private static edge_data edgeOfPokemon(directed_weighted_graph g, CL_Pokemon p) {
        for (node_data n : g.getV()) {
            for (edge_data e : g.getE(n.getKey())) {
                if (isOnEdge(p.getLocation(), e, p.getType(), g))
                    return e;
            }
        }
        return null;
    }

    /**
     * Find the pokemon who stay on the given edge.
     *
     * @param e represents the edge
     * @param g represents the graph
     * @return the pokemon.
     */
    private static CL_Pokemon getPokemonOnEdge(edge_data e, directed_weighted_graph g) {
        List<CL_Pokemon> pokemons = Arena.json2Pokemons(game.getPokemons());
        for (CL_Pokemon p : pokemons) {
            if (isOnEdge(p.getLocation(), e, p.getType(), g))
                return p;
        }
        return null;
    }

    /**
     * Find the edge with the highest value.
     *
     * @param g represents the graph
     * @return the edge.
     */
    private static edge_data getMaxEdge(directed_weighted_graph g) {
        edge_data max = null;
        List<CL_Pokemon> pokemons = Arena.json2Pokemons(game.getPokemons());
        double maxVal = 0;
        for (node_data n : g.getV()) {
            for (edge_data e : g.getE(n.getKey())) {
                double currVal = 0;
                for (CL_Pokemon p : pokemons)
                    if (isOnEdge(p.getLocation(), e, p.getType(), g))
                        currVal += p.getValue();
                if (maxVal < currVal) {
                    maxVal = currVal;
                    max = e;
                }
            }
        }
        return max;
    }

    /**
     * Find the closest agent to the highest valued edge
     *
     * @param e represents the edge with the highest value
     * @param g represents the graph
     * @return the id of the closest agent
     */
    private static int findClosetAgent(edge_data e, directed_weighted_graph g) {
        List<CL_Agent> agents = Arena.getAgents(game.move(), g);
        double minDist = Double.POSITIVE_INFINITY;
        int agentID = -1;
        for (CL_Agent a : agents) {
            double currDist = algo.shortestPathDist(a.getSrcNode(), e.getSrc());
            if (currDist != -1 && minDist > currDist) {
                minDist = currDist;
                agentID = a.getID();
            }
        }
        return agentID;
    }

    /**
     * An auxiliary function that calculates the distance between an agent and a Pokemon
     *
     * @param agent represent the agent
     * @param p     represents the pokemon
     * @return true if the agent has epsilon distance from the Pokemon.
     */
    private static boolean distToPokemon(CL_Agent agent, CL_Pokemon p) {
        geo_location agentPos = agent.getLocation();
        double distance = 0;
        try {
            geo_location pokemonPos = p.getLocation();
            distance = agentPos.distance(pokemonPos);
        } catch (Exception ex) {
            ex.getStackTrace();
        }
        return distance < agent.getSpeed() * EPS1;
    }
}

