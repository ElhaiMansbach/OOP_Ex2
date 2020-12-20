package tests;

import Server.Game_Server_Ex2;
import api.DWGraph_Algo;
import api.EdgeData;
import api.directed_weighted_graph;
import api.game_service;
import gameClient.Arena;
import gameClient.CL_Agent;
import gameClient.CL_Pokemon;
import gameClient.Ex2;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.*;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class AlgorithmsOfEx2Test {

//    static int scenario_num =3;
//    static game_service game;
//    Ex2 ex2= new Ex2();
//    directed_weighted_graph gg;
//
//
//
//
//
//    @BeforeEach
//    void setUp() throws Exception {
//        game = Game_Server_Ex2.getServer(scenario_num);
//        Ex2.init(game);
//    }
//
//
//    /**
//     * Test findPokemonSrc(pokemon) function
//     */
//    @Test
//    public void testFindPokemonSrc() {
//        ArrayList<Integer> listSrc =new ArrayList<Integer>();
//        listSrc.add(4);
//        listSrc.add(3);
//        listSrc.add(9);
//        for(CL_Pokemon p: .getPokemons()) {
//            if (!listSrc.contains(Ex2.findPokemonSrc(gg, p)))
//                fail("find pokemon src failed");
//        }
//    }
//    /**
//     * Test isOnEdge(point,edge,type,graph) function
//     */
//    @Test
//    public void testIsOnEdge() {
//
//        if(!Arena.isOnEdge( ex2.get_ar().getPokemons().get(2).getLocation(), new EdgeData(9,8,1.4575484853801393),-1,gg))
//            fail("is on edge failed");
//        if(Arena.isOnEdge( ex2.get_ar().getPokemons().get(2).getLocation(), new EdgeData(3,4,1.4301580756736283),-1,gg))
//            fail("is on edge failed");
//    }
//
//    /**
//     * Test autoLocateAgents() function
//     */
//    @Test
//    public void testLocateAgents() {
//
//        for(CL_Agent a: ex2.get_ar().getAgents()) {
//            if (a.getSrcNode()!=3) // the highest valued pokemon is on edge with src node 3
//                fail("agent location failed");
//        }
//    }
//
//
//
//}
}