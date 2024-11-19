package me.ildarorama.module3.task5;

import me.ildarorama.module3.task5.model.Currency;
import me.ildarorama.module3.task5.model.CurrencyBean;
import me.ildarorama.module3.task5.model.UserBean;
import org.apache.commons.lang3.tuple.ImmutablePair;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

import static me.ildarorama.module3.task5.Utils.getCurrencyToExchange;
import static me.ildarorama.module3.task5.Utils.getRandomAmount;

public class Task5Runner {
    private static final Logger log = Logger.getLogger(Task5Runner.class.getName());
    private static final ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    private static final Map<ImmutablePair<Currency, Currency>, AtomicInteger> SUCCESS = new HashMap<>();
    private static final Map<ImmutablePair<Currency, Currency>, AtomicInteger> FAILURE = new HashMap<>();

    public static void main(String[] args) throws InterruptedException {
        var service = new UserService();

        UserBean user = new UserBean();
        user.setId(1);
        user.setName("User1");
        user.setCurrencies(new HashMap<>());

        CurrencyBean currencyBean = new CurrencyBean(Currency.USD, BigDecimal.valueOf(45.45));
        user.getCurrencies().put(currencyBean.getCurrency(), currencyBean);

        currencyBean = new CurrencyBean(Currency.EURO, BigDecimal.valueOf(98.55));
        user.getCurrencies().put(currencyBean.getCurrency(), currencyBean);

        currencyBean = new CurrencyBean(Currency.YEN, BigDecimal.valueOf(920));
        user.getCurrencies().put(currencyBean.getCurrency(), currencyBean);

        currencyBean = new CurrencyBean(Currency.LIRA, BigDecimal.valueOf(540.50));
        user.getCurrencies().put(currencyBean.getCurrency(), currencyBean);

        currencyBean = new CurrencyBean(Currency.LARI, BigDecimal.valueOf(85.60));
        user.getCurrencies().put(currencyBean.getCurrency(), currencyBean);

        currencyBean = new CurrencyBean(Currency.POUND, BigDecimal.valueOf(37.1));
        user.getCurrencies().put(currencyBean.getCurrency(), currencyBean);
        service.saveUser(user);

        var us = service.getUser(1);

        log.info(us.toString());
        log.info("Total: " + service.totalInUsd(us));

        List<FutureTask> taskList = new ArrayList<>();
        for (int i = 0; i < 10000; i++) {
            var currencies = getCurrencyToExchange();
            var amount = getRandomAmount();
            var future = executorService.submit(() -> service.exchange(us, currencies, amount));
            taskList.add(new FutureTask(future, currencies));
        }

        executorService.shutdown();
        executorService.awaitTermination(1, TimeUnit.MINUTES);

        for (FutureTask task : taskList) {
            try {
                task.future.get();
                SUCCESS.computeIfAbsent(task.currency, k -> new AtomicInteger()).incrementAndGet();
            } catch (ExecutionException e) {
                FAILURE.computeIfAbsent(task.currency, k -> new AtomicInteger()).incrementAndGet();
            }
        }

        service.saveUser(us);

        log.info(us.toString());
        log.info("Total: " + service.totalInUsd(us));

        log.info("Success: ");
        log.info(SUCCESS.toString());

        log.info("Failure: ");
        log.info(FAILURE.toString());
    }


}
