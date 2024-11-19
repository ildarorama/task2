package me.ildarorama.module3.task5.model;

import java.util.Map;

public class UserBean {
    private long id;

    private String name;

    private Map<Currency, CurrencyBean> currencies;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<Currency, CurrencyBean> getCurrencies() {
        return currencies;
    }

    public void setCurrencies(Map<Currency, CurrencyBean> currencies) {
        this.currencies = currencies;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("User: ").append(name).append("\n");
        sb.append("Currencies:\n");
        for (CurrencyBean value : currencies.values()) {
            sb.append(value.getCurrency().name()).append(": ")
                    .append(value.getAmount()).append("\n");
        }
        return sb.toString();
    }
}
