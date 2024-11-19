package me.ildarorama.module3.task5;

import me.ildarorama.module3.task5.model.Currency;
import org.apache.commons.lang3.tuple.ImmutablePair;

import java.util.concurrent.Future;

class FutureTask {
    final Future<Boolean> future;
    final ImmutablePair<Currency, Currency>  currency;
    FutureTask(Future<Boolean> future, ImmutablePair<Currency, Currency>  currency) {
        this.future = future;
        this.currency = currency;
    }
}
