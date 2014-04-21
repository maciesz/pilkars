package com.github.maciesz.gala.ai;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author maciej
 */
public abstract class Chart 
{  
    // ========================================================================
    // Constructor
    // ========================================================================
    public Chart(final int width, final int height)
    {
        /* Set width, height and size of the playgorund */
        this.WIDTH = width;
        this.HEIGHT = height;
        this.T_SIZE = (WIDTH + 1) * (HEIGHT + 1);
        
        /* Assign all nodes in graph as non-final states */
        finalState = new boolean[(WIDTH + 1) * (HEIGHT + 1) + 2];
        
        /* Select SUCCESS, LOOSE and ACTUAL states */
        this.SUCCESS_STATE = 1500100900;
        this.LOOSE_STATE = 1500100901;
        this.ACTUAL_STATE = (WIDTH + 1) * (HEIGHT + 1) / 2;
        
        /* Build graph */
        buildGraph();
    }

    public Chart(Chart other)
    {
        this.SUCCESS_STATE = other.SUCCESS_STATE;
        this.LOOSE_STATE = other.LOOSE_STATE;
        this.WIDTH = other.WIDTH;
        this.HEIGHT = other.HEIGHT;
        this.T_SIZE = other.T_SIZE;
        this.graph = other.graph;
        this.ACTUAL_STATE = other.ACTUAL_STATE;
        this.finalState = other.finalState;
    }
    // ========================================================================
    // Public methods
    // ========================================================================
    public int computeNext(final int node, final int xCoord, final int yCoord)
    {
        return node + xCoord + (WIDTH + 1) * yCoord;
    }

    public boolean isMovePossible(final int lhs, final int rhs)
    {
        /* Check if hashset contains edge between lhs and rhs */
        return graph.contains(computeHash(lhs, rhs));
    }

    public void removeConnection(final int lhs, final int rhs)
    {
        /* Delete edge between lhs and rhs in graph of game */
        graph.remove(computeHash(lhs, rhs));
    }

    public int computeHash(final int lhs, final int rhs)
    {
        /* Compute unique value for concrete edge */
        return Math.min(lhs, rhs) * MULTIPLIER + Math.max(lhs, rhs);
    }
  
    // ========================================================================
    // Private methods
    // ========================================================================
    private void buildGraph() 
    {
        /* Connect playground states */
        graph = new HashSet<Integer>();
        for (int i = 1; i< HEIGHT; ++i)
            for (int j = 1; j< WIDTH; ++j)
            {
                final int position = HEIGHT * (WIDTH + 1) + j;
                for (int k = 0; k< DIRECTIONS; ++k)
                    graph.add(computeHash(position, computeNext(position, X_COORDS[k], Y_COORDS[k])));
            }

        final int scale = HEIGHT * (WIDTH + 1);
        for (int k = WIDTH/2 -1; k <= WIDTH / 2 + 1; ++k)
        {
            graph.add(computeHash(k, 1500100901));
            graph.add(computeHash(k + scale, 1500100900));
        }
    }
    
    // ========================================================================
    // Public Static Structures/Variables
    // ========================================================================
    public static final int DIRECTIONS = 8;
    
    public static final int[] X_COORDS = {-1, -1, 0, 1, 1, 1, 0, -1};

    public static final int[] Y_COORDS = {0, 1, 1, 1, 0, -1, -1, -1};
    
    public static final int DEFEAT_CODE = -1032331390;
    
    public static final int SUCCESS_CODE = 2123908232;
    
    // ========================================================================
    // Public Structures/Variables
    // ========================================================================
    public final int T_SIZE;

    public final int MULTIPLIER = 1000;
    
    public final int SUCCESS_STATE;
    
    public final int LOOSE_STATE;
    
    public int ACTUAL_STATE;
    
    public boolean[] finalState;
    
    public final int WIDTH;
    
    public final int HEIGHT;
    
    public Set<Integer> graph; 
}
