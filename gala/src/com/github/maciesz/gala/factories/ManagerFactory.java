package com.github.maciesz.gala.factories;

import com.github.maciesz.gala.core.AbstractManager;
import com.github.maciesz.gala.core.CvPManager;
import com.github.maciesz.gala.core.PvPManager;
import com.github.maciesz.gala.enums.GameMode;
import com.github.maciesz.gala.exceptions.UnknownGameModeException;
import java.util.EnumMap;
import java.util.Map;

/**
 * Fabryka tworząca zarządców gier wyspecyfikowanego typu.
 * 
 * @author Maciej Szeszko <m.szeszko@student.uw.edu.pl>
 */

public class ManagerFactory {
    /**
     * Struktura umożliwiająca tworzenie konkretnych obiektów na podstawie klucza.
     */
    private static final Map managers = new EnumMap<GameMode, AbstractManager>(GameMode.class);
    
    /**
     * Inicjacja struktury WSZYSTKIMI dostępnymi rodzajami zarządców.
     */
    static {
        addManager(GameMode.ComputerVsPlayer, new CvPManager());
        addManager(GameMode.PlayerVsPlayer, new PvPManager());
    }
    
    /**
     * Funkcja zwracająca konkretnego managera.
     * 
     * @param view widok, z którym będzie się komunikował zarządca
     * @param gameMode typ rozgrywki(PvP, CvP)
     * @return instancja konkretnego zarządcy gry
     * @throws UnknownGameModeException
     */
    public static AbstractManager
        createManager(final GameMode gameMode) throws UnknownGameModeException {
        
        if (!containsManager(gameMode))
            throw new UnknownGameModeException();
        
        AbstractManager manager = (AbstractManager) managers.get(gameMode);
        return manager.getInstance();
    }
    
    /**
     * Funkcja odpowiedzialna za dodanie wskazanej pary do struktury.
     * 
     * @param gameMode typ rozgrywki(PvP, CvP)
     * @param manager instancja konkretnego zarządcy
     */
    private static void addManager(final GameMode gameMode, AbstractManager manager) {
        managers.put(gameMode, manager);
    }
    
    
    /**
     * Funkcja weryfikująca przynależność obiektu do struktury.
     * 
     * @param gameMode typ rozgrywki(PvP, CvP)
     * @return czy struktura jest w stanie stworzyć zarządcę dla wskazanego typu rozgrywki?
     */
    private static boolean containsManager(final GameMode gameMode)
    {
        return managers.containsKey(gameMode);
    }
}