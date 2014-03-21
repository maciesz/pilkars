/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ai;

import java.util.HashSet;
import java.util.Set;

/**
 * Plansza
 * 
 * @author Maciej Szeszko <m.szeszko@student.uw.edu.pl>
 */
public final class Chart 
{
    // ========================================================================
    // Constructor
    // ========================================================================
    public Chart(final Integer width, final Integer height)
    {
        /* Assign all nodes in graph as non-final states */
        finalState = new boolean[(width + 1) * (height + 1) + 2];
        
        /* Select SUCCESS and LOOSE states */
        this.SUCCESS_STATE = (width * height) + width / 2;
        this.LOOSE_STATE = width / 2;
        
        /* Build graph */
        buildGraph(width, height);
    }
    
    // ========================================================================
    // Public methods
    // ========================================================================
    public boolean isMovePossible(final Integer lhs, final Integer rhs)
    {
        /* Check if hashset contains edge between lhs and rhs */
        return graph.contains(computeHash(lhs, rhs));
    }
   
    public void removeConnection(final Integer lhs, final Integer rhs)
    {
        /* Delete edge between lhs and rhs in graph of game */
        graph.remove(computeHash(lhs, rhs));
    }
    
    // ========================================================================
    // Private methods
    // ========================================================================
    private Integer computeHash(final Integer lhs, final Integer rhs)
    {
        /* Compute unique value for concrete edge */
        return Integer.max(lhs, rhs) * MULTIPLIER + Integer.min(lhs, rhs);
    }
    
    private void buildGraph(final Integer width, final Integer height) 
    {
        /* Connect playground states */
        graph = new HashSet<>();
        for (int i = 1; i< height; ++i)
            for (int j = 1; j< width; ++j)
            {
                final Integer position = height * (width + 1) + j;
                for (int k = 0; k< DIRECTIONS; ++k)
                    graph.add(computeHash(position, position + X_COORDS[k] + Y_COORDS[k] * (width + 1)));
            }

        /* Erase connects with Your goal */
        final Integer ownGoal = width/2;
        for (int k = 0; k< DIRECTIONS - 3; ++k)
            graph.remove(computeHash(ownGoal, ownGoal + X_COORDS[k] + Y_COORDS[k] * (width + 1)));
        
        /* Add connects with Opponent's goal */
        final Integer oppGoal = width * height + width/2;
        for (int k = 4; k< DIRECTIONS; ++k)
            graph.add(computeHash(oppGoal, oppGoal + X_COORDS[k] + Y_COORDS[k] * (width + 1)));
        graph.add(computeHash(oppGoal, oppGoal + X_COORDS[0] + Y_COORDS[0] * (width + 1)));
    }
    
    // ========================================================================
    // Variables
    // ========================================================================
    public static final Integer DIRECTIONS = 8;
    
    public static final Integer[] X_COORDS = {-1, -1, 0, 1, 1, 1, 0, -1};

    public static final Integer[] Y_COORDS = {0, 1, 1, 1, 0, -1, -1, -1};

    public final Integer MULTIPLIER = 1000;
    
    public final Integer SUCCESS_STATE;
    
    public final Integer LOOSE_STATE;
    
    public boolean[] finalState;
    
    public Set<Integer> graph;
}
