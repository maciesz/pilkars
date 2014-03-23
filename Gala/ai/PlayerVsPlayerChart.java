/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ai;

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