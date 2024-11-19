package me.ildarorama.module3.task5.model;

import java.math.BigDecimal;

public class CurrencyBean {
    private Currency currency;
    private BigDecimal amount;

    public CurrencyBean() {}

    public CurrencyBean(Currency currency, BigDecimal amount) {
        this.amount = amount;
        this.currency = currency;
    }
    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
