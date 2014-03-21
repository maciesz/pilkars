/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ai;

/**
 * Budowniczy strategii na podstawie przekazanego parametru trudności gry.
 * 
 * @author Maciej Szeszko <m.szeszko@student.uw.edu.pl>
 */
public class StrategyBuilder 
{
    public static AI getStrategy(final StrategyLevel level)
    {
        switch (level)
        {
            case HARD:
                return new MonteCarlo();
            case MEDIUM:
                return new GreedyDFS();
            case EASY:
                return new SimpleTraverse();
        }
        
        throw new AssertionError("Unknown StrategyLevel: " + level);
    }
}
