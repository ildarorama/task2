package me.ildarorama.module3.task5;

import me.ildarorama.module3.task5.model.Currency;
import org.apache.commons.lang3.tuple.ImmutablePair;

import java.math.BigDecimal;
import java.util.Random;

public final class Utils {
    private static final Random rnd = new Random();

    public static BigDecimal getRandomAmount() {
        return BigDecimal.valueOf(rnd.nextLong(1000) / 100, 2);
    }

    public static ImmutablePair<Currency, Currency> getCurrencyToExchange() {
        int from = rnd.nextInt(Currency.values().length);
        int to;
        do {
            to = rnd.nextInt(Currency.values().length);
        } while (to == from);
        return ImmutablePair.of(Currency.values()[from], Currency.values()[to]);
    }
}
