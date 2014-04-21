package com.github.maciesz.gala.ai;

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
