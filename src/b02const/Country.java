package b02const;

import java.util.Locale;

public enum Country {

    Are("ARE", Ccy.Aed),
    Aus("AUS", Ccy.Aud),
    Bra("BRA", Ccy.Brl),
    Can(Locale.CANADA.getISO3Country(), Ccy.Cad),
    Che("CHE", Ccy.Chf),
    Chn(Locale.CHINA.getISO3Country(), Ccy.Cny),
    Deu("DEU", Ccy.Eur),
    Fra(Locale.FRANCE.getISO3Country(), Ccy.Eur),
    Gbr(Locale.UK.getISO3Country(), Ccy.Gbp),
    Ita(Locale.ITALY.getISO3Country(), Ccy.Eur),
    Jpn(Locale.JAPAN.getISO3Country(), Ccy.Jpy),
    Kor(Locale.KOREA.getISO3Country(), Ccy.Krw),
    Hkg("HKG", Ccy.Hkd),
    Tur("TUR", Ccy.Try),
    Usa(Locale.US.getISO3Country(), Ccy.Usd);

    public final String code;

    public final Ccy ccy;

    private Country(String code, Ccy ccy) {
        this.code = code;
        this.ccy = ccy;
    }
}
