package main.gala.factories;

import main.gala.core.*;
import main.gala.enums.GameMode;
import main.gala.exceptions.UnknownGameModeException;

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
    private static Map managers;

    /**
     * Inicjalizacja struktury WSZYSTKIMI dostępnymi rodzajami zarządców.
     */
    static {
        managers = new EnumMap<>(GameMode.class);
        addManager(GameMode.ComputerVsPlayer, new CvPManager());
        addManager(GameMode.PlayerVsPlayer, new PvPManager());
        addManager(GameMode.Mock, new MockManager());
        addManager(GameMode.WiFiP2P, new P2PManager());
    }
    
    /**
     * Funkcja zwracająca konkretnego managera.
     *
     * @param gameMode typ rozgrywki(PvP, CvP)
     * @return instancja konkretnego zarządcy gry
     * @throws main.gala.exceptions.UnknownGameModeException
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