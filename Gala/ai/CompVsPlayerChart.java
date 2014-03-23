/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ai;

/**
 * Plansza computer vs człowiek
 * 
 * @author Maciej Szeszko <m.szeszko@student.uw.edu.pl>
 */
public class CompVsPlayerChart extends Chart 
{
    // ========================================================================
    // Constructors
    // ========================================================================
    public CompVsPlayerChart(final int width, final int height)
    {
        super(width, height);
    }
    
    public CompVsPlayerChart(final Chart other)
    {
        super(other);
    }
}
