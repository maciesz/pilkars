package com.github.maciesz.gala.ai;

/**
 * Plansza człowiek vs człowiek
 * 
 * @author Maciej Szeszko <m.szeszko@student.uw.edu.pl>
 */
public class PlayerVsPlayerChart extends Chart 
{
    // ========================================================================
    // Constructors
    // ========================================================================
    public PlayerVsPlayerChart(final int width, final int height)
    {
        super(width, height);
    }
    
    public PlayerVsPlayerChart(final Chart other)
    {
        super(other);
    }
}