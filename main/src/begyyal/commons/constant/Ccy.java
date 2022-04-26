package begyyal.commons.constant;

import java.util.Arrays;
import java.util.Currency;

import begyyal.commons.object.collection.XList;
import begyyal.commons.object.collection.XList.XListGen;

public enum Ccy {

    Aed(Currency.getInstance("AED")),
    Aud(Currency.getInstance("AUD")),
    Brl(Currency.getInstance("BRL")),
    Cad(Currency.getInstance("CAD")),
    Chf(Currency.getInstance("CHF")),
    Cny(Currency.getInstance("CNY")),
    Eur(Currency.getInstance("EUR")),
    Gbp(Currency.getInstance("GBP")),
    Hkd(Currency.getInstance("HKD")),
    Jpy(Currency.getInstance("JPY")),
    Krw(Currency.getInstance("KRW")),
    Try(Currency.getInstance("TRY")),
    Usd(Currency.getInstance("USD"));

    public final Currency ccy;

    private volatile XList<Country> countries;

    private Ccy(Currency ccy) {
        this.ccy = ccy;
    }

    public XList<Country> getCountry() {
        if (countries != null)
            return countries;
        countries = Arrays.stream(Country.values())
                .filter(country -> country.ccy == this)
                .collect(XListGen.collect());
        return countries;
    }
}
