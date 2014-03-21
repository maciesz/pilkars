/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ai;

import java.util.List;

/**
 * Klasa Agent pośrednicząca między klasą zarządzającą rozgrywką, a komputerem.
 * 
 * @author Maciej Szeszko <m.szeszko@student.uw.edu.pl>
 */
public final class Agent 
{
    // ========================================================================
    // Constructor
    // ========================================================================
    public Agent(final Integer width, final Integer height, 
            final GoalConfiguration goalConfiguration, final StrategyLevel level)
    {
        /* Set final attributes */
        this.GOAL_CONFIGURATION = goalConfiguration;
        this.SYMETRIC_POINT = (height / 2) * (width + 1) + width / 2;
        
        /* Build chart of game */
        chart = new Chart(width, height);
        
        /* Pick strategy based on passed parametres */
        strategy = StrategyBuilder.getStrategy(level);
    }
    
    // ========================================================================
    // Methods
    // ========================================================================
    public void registerMoveSequence(List<Integer> moveSeq)
    {
        /* Prepare appropriate moveSeq */
        final List<Integer> seq = parsePath(moveSeq);
        
        /* Mark visited edges */
        for (int iter = 0; iter< seq.size() - 1; ++iter)
            chart.removeConnection(seq.get(iter), seq.get(iter + 1));
    }

    public List<Integer> getComputerSequence()
    {
        /* Adapt seq to real game chart */
        return parsePath(strategy.executeMoveSeq(chart));
    }
    
    private List<Integer> parsePath(List<Integer> path)
    {
        /* If computer goal is not set as DEFAULT,
           then use symmetry to adapt the path */
        if (GOAL_CONFIGURATION != GoalConfiguration.DEFAULT)
            for (int iter = 0; iter< path.size(); ++iter)
                path.set(iter, 2 * SYMETRIC_POINT - path.get(iter));

        return path;
    }
    
    // ========================================================================
    // Variables
    // ========================================================================
    public AI strategy;
    
    public Chart chart;
    
    private final Integer SYMETRIC_POINT;
    
    private final GoalConfiguration GOAL_CONFIGURATION;
    
}
