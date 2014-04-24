package com.github.maciesz.gala.factories;

import com.github.maciesz.gala.ai.IArtificialIntelligence;
import com.github.maciesz.gala.ai.RandomPlayer;
import com.github.maciesz.gala.enums.GameMode;
import com.github.maciesz.gala.enums.Strategy;
import com.github.maciesz.gala.exceptions.UnknownStrategyException;

import java.util.EnumMap;
import java.util.Map;

/**
 * Fabryka tworząca strategie dla gracza komputerowego.
 *
 * @author Maciej Szeszko <m.szeszko@student.uw.edu.pl>
 */

public class StrategyFactory {
    /**
     * Struktura umożliwiająca tworzenie konkretnych obiektów na podstawie klucza.
     */
    private static Map strategies;

    /**
     * Inicjalizacja struktury dostępnymi strategiami.
     */
    static {
        strategies = new EnumMap<>(GameMode.class);
        addStrategy(Strategy.RANDOM, new RandomPlayer());
    }

    /**
     * Funkcja zwracająca konkretną AI zależną od parametru Strategii.
     *
     * @param strategy opis strategii dla gracza komputerowego
     * @return instancja klasy roszerzającej interfejs AI dla podanej strategii
     * @throws UnknownStrategyException
     */
    public static IArtificialIntelligence
        initializeStrategy(final Strategy strategy) throws UnknownStrategyException {

        if (!containsStrategy(strategy))
            throw new UnknownStrategyException();

        IArtificialIntelligence ai = (IArtificialIntelligence) strategies.get(strategy);
        return ai.getInstance();
    }

    /**
     * Funkcja odpowiedzialna za dodanie wskazanej pary do struktury.
     *
     * @param strategy opis konkretnej strategii
     * @param ai instancja klasy implementującej interfejs IArtificialIntelligence
     */
    private static void addStrategy(final Strategy strategy, IArtificialIntelligence ai) {
        strategies.put(strategy, ai);
    }

    /**
     * Funkcja weryfikująca przynależność obiektu do struktury.
     *
     * @param strategy opis konkretnej strategii
     * @return czy struktura jest w stanie stworzyć instancję klasy implementującą interfejs IArtificialIntelligence
     * dla wskazanej strategii
     */
    private static boolean containsStrategy(final Strategy strategy) {
        return strategies.containsKey(strategy);
    }

}
