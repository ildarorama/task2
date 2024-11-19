package me.ildarorama.module3.task5;

import me.ildarorama.module3.task5.model.Currency;
import me.ildarorama.module3.task5.model.CurrencyBean;
import me.ildarorama.module3.task5.model.UserBean;
import org.apache.commons.lang3.tuple.ImmutablePair;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;

public class UserService {
    private static final Logger log = Logger.getLogger(UserDao.class.getName());
    private UserDao userDao = new UserDao();
    private static final Map<Currency, BigDecimal> EXCHANGE_RATE = Map.of(
            Currency.YEN, BigDecimal.valueOf(149.06),
            Currency.EURO, BigDecimal.valueOf(0.92),
            Currency.LARI,  BigDecimal.valueOf(2.71),
            Currency.LIRA,  BigDecimal.valueOf(28.82),
            Currency.POUND,  BigDecimal.valueOf(0.80),
            Currency.USD, BigDecimal.valueOf(1.00)
    );

    public UserBean getUser(long id) {
        return userDao.getUserById(id);
    }

    public void saveUser(UserBean user) {
        userDao.saveUser(user);
    }

    public BigDecimal totalInUsd(UserBean user) {
        BigDecimal result = BigDecimal.ZERO;
        for (CurrencyBean value : user.getCurrencies().values()) {
            result = result.add(value.getAmount().divide(EXCHANGE_RATE.get(value.getCurrency()), 2, RoundingMode.DOWN));
        }
        return result;
    }

    public boolean exchange(UserBean user, ImmutablePair<Currency, Currency> currency, BigDecimal amount) {
        Objects.requireNonNull(currency.left);
        Objects.requireNonNull(currency.right);
        if (currency.left == currency.right) {
            log.warning("Currency must be different");
            throw new RuntimeException("Currency must be different");
        }
        CurrencyBean fromAccount = user.getCurrencies().get(currency.left);
        CurrencyBean toAccount = user.getCurrencies().get(currency.right);

        Objects.requireNonNull(fromAccount, "There is no FROM account");
        Objects.requireNonNull(toAccount, "There is no TO account");

        BigDecimal rate = getRate(currency.left, currency.right);
        BigDecimal converted = amount.multiply(rate).setScale(2, RoundingMode.DOWN);

        var locks = getBeanToLock(fromAccount, toAccount);

        synchronized (locks.left) {
            synchronized (locks.right) {
                if (fromAccount.getAmount().compareTo(amount) < 0) {
                    log.warning("Not enough money");
                    throw new RuntimeException("Not enough money");
                }

                fromAccount.setAmount(fromAccount.getAmount().subtract(amount));
                toAccount.setAmount(toAccount.getAmount().add(converted));
            }
        }
        return true;
    }

    private ImmutablePair<CurrencyBean, CurrencyBean> getBeanToLock(CurrencyBean from, CurrencyBean to) {
        var accountToLock1 = from.getCurrency().ordinal() < to.getCurrency().ordinal() ? from : to;
        var accountToLock2 = accountToLock1 == to ? from : to;
        return ImmutablePair.of(accountToLock2, accountToLock2);
    }

    private BigDecimal getRate(Currency from, Currency to) {
        if (from == Currency.USD) {
            return EXCHANGE_RATE.get(to);
        }

        if (to == Currency.USD) {
            return BigDecimal.ONE.divide(EXCHANGE_RATE.get(from),2, RoundingMode.DOWN);
        }

        return getRate(from, Currency.USD).multiply(EXCHANGE_RATE.get(to));
    }
}
