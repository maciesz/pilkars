package com.github.maciesz.gala.ai;

import static java.lang.Math.abs;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.Stack;

/**
 * Sztuczna inteligencja - poziom HARD.
 *
 * @author Maciej Szeszko <m.szeszko@student.uw.edu.pl>
 */
public final class MonteCarlo implements AI
{
    // ========================================================================
    // Public methods
    // ========================================================================
    @Override
    public List<Integer> executeMoveSeq(Chart chart) 
    {
        /* Actualize data */
        this.gameChart = new CompVsPlayerChart(chart);
        this.moves = new int[gameChart.T_SIZE];
        this.successes = new int[gameChart.T_SIZE];
        this.visited = new boolean[gameChart.T_SIZE];
        
        /* Actualize visited nodes table and mark initial node as visited */
        for (int k = 0; k<gameChart.T_SIZE; ++k)
            visited[k] = gameChart.finalState[k];
        
        visited[gameChart.ACTUAL_STATE] = true;
        
        /* Get pseudo-random trials */
        for (int trial = 0; trial <= EXPLORATION_LIMIT; ++trial)
        {
            explorePath(gameChart.ACTUAL_STATE);
        }
        
        /* Get move sequence */
        List<Integer> resultList = recoverPath(gameChart.ACTUAL_STATE);

        /* Remove compouter sequence edges from original graph */
        for (int iter = 0; iter< resultList.size() - 1; ++iter)
            chart.removeConnection(resultList.get(iter), resultList.get(iter + 1));
        
        /* Change visited states to final */
        for (int iter = 0; iter< resultList.size(); ++iter)
            chart.finalState[resultList.get(iter)] = true;
        
        return resultList;
    }
    
    // ========================================================================
    // Constructor
    // ========================================================================
    public MonteCarlo()
    {
    }
    
    // ========================================================================
    // Private methods
    // ========================================================================
    private int distanceMeasure(final int position, final int goal)
    {
        final int steps = abs(position - goal);
        final int stepsX = steps % (gameChart.WIDTH + 1);
        final int stepsY = (int)Math.ceil((steps - stepsX) / (gameChart.WIDTH + 1)) - 1;
        return MOVE_COST * Math.max(stepsX, stepsY);
    }
    
    private int heuristicFunction(final int moves)
    {
        return (int) Math.log(moves);
    }
        
    private int computeExpectedValue(final int node)
    {
        return successes[node] / moves[node];
    }
    
    private int computeDeviation(final int anc, final int desc)
    {
        return (int) Math.sqrt((heuristicFunction(moves[anc]) / moves[desc]));
    }
    
    private int optimizeRiskBound(final int node, final int descendant, final NodeType type)
    {
        /* Compute expected value of success when passing through descendant
         * and it's deviation */
        final int expectedValue = computeExpectedValue(descendant);
        final int deviation = computeDeviation(node, descendant);
        
        /* Return appropriate probability */
        return (type == NodeType.MAX) ? (expectedValue + deviation) : (expectedValue - deviation);
    }
    
    private int pickDescendant(final int node, final NodeType type)
    {
        int descendant = -1; // Initially descendant is not available
        int successProbability = (type == NodeType.MAX) ? Integer.MIN_VALUE : Integer.MAX_VALUE;
        int expectedValue;
        int deviation;
        int rating = 0;
        
        for (int k = 0; k< Chart.DIRECTIONS; ++k)
        {
            /* Select possible neighbour in graph */
            int next = gameChart.computeNext(node, Chart.X_COORDS[k], Chart.Y_COORDS[k]);
            if (gameChart.isMovePossible(node, next))
            {
                /* If pass between node and next is possible */
                
                /* If next not visited */
                if (!visited[next])
                {
                    visited[next] = true;
                    return next;
                }
                
                /* Judge worth of vertex next on the path */
                expectedValue = computeExpectedValue(next);
                deviation = computeDeviation(node, next);

                boolean decision = false;
                switch (type)
                {
                    case MAX:
                        rating = expectedValue + deviation;
                        decision = rating > successProbability;

                        break;
                    case MIN:
                        rating = expectedValue - deviation;
                        decision = rating < successProbability;

                        break;
                }
                
                if (decision)
                {
                    successProbability = rating;
                    descendant = next;
                }
            }
        }
        
        return descendant;
    }
    
    private void explorePath(final int node)
    {
        Stack usedEdges = new Stack<Integer>();
        Stack stack = new Stack<Integer>();
        stack.push(node);
        int descendant;
        NodeType type = NodeType.MAX;
        boolean decision;
        int actConParam = 0;
        while (true)
        {
            /* Pick descendant */
            descendant = pickDescendant(node, NodeType.MAX);

            decision = true; // Assume that we are in final state
            if (descendant == -1)
                actConParam = 0;
            else if (descendant == gameChart.SUCCESS_STATE)
                actConParam = 1;
            else if (descendant == gameChart.LOOSE_STATE)
                actConParam = 0;
            else 
                decision = false; // If state is not final then remark it
            
            
            if (descendant != -1)
            {
                /* Remember path and used edges */
                usedEdges.push(gameChart.computeHash(node, descendant));

                /* Actualize pitch-graph */
                gameChart.removeConnection(node, descendant);
            }
            
            if (decision)
            {
                /* Mark nodes visited during optimal path-finding */
                boolean[] visitedDuringTraversal = new boolean[gameChart.T_SIZE];
                
                while (!stack.empty())
                    visitedDuringTraversal[(Integer) stack.pop()] = true;
                
                /* Actualize chart move possibilities */
                while (!usedEdges.empty())
                    gameChart.graph.add((Integer) usedEdges.pop());
                
                /* Assign statistics */
                for (int iter = 0; iter< gameChart.T_SIZE; ++iter)
                {
                    moves[iter]++;
                    if (visitedDuringTraversal[iter])
                        successes[iter] += actConParam;
                }
                /* Path is completed */
                return;
            }
            
            /* Swap strategy to player mode */
            if (!visited[descendant])
                type = NodeType.MIN;
        }
    }
    
    private List<Integer> recoverPath(int state)
    {
        List<Integer> resultList = new ArrayList<Integer>();
        resultList.add(state);
        while (visited[state])
        {
            int expectedValue = Integer.MIN_VALUE; // Set EV to -inf
            int node = -1; // Mark that any node has been chosen yet
            for (int k = 0; k< Chart.DIRECTIONS; ++k) // Go through all neighs..
            {
                int next = gameChart.computeNext(state, Chart.X_COORDS[k], Chart.Y_COORDS[k]);
                if (gameChart.isMovePossible(state, next))
                    if(computeExpectedValue(next) >= expectedValue)
                        node = next;
            }
            
            boolean decision = true;
            if (node == -1 || node == gameChart.LOOSE_STATE)
                resultList.add(Chart.DEFEAT_CODE);
            else if (node == gameChart.SUCCESS_STATE)
                resultList.add(Chart.SUCCESS_CODE);
            else {
                resultList.add(node);
                decision = false;
            }
            
            /* If state is final like: SUCCESS / DEFEAT / BLOCK */
            if (decision)
                break;
            
            state = node;
        }
        
        return resultList;
    }
    
    // ========================================================================
    // Private variables
    // ========================================================================
    
    private final int EXPLORATION_LIMIT = 40000;
    
    private final int MOVE_COST = 10;
    
    private Chart gameChart;
    
    private int[] moves;
    
    private int[] successes;
    
    private boolean[] visited;
}
