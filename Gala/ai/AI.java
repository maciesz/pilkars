/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ai;

import java.util.List;

/**
 * Interfejs sztucznej inteligencji
 * 
 * @author Maciej Szeszko <m.szeszko@student.uw.edu.pl>
 */
public interface AI 
{
    public List<Integer> executeMoveSeq(Chart chart);
}
