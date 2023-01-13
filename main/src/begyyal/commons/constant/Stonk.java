package begyyal.commons.constant;

public enum Stonk {

    EU50(Ccy.Eur),
    HK50(Ccy.Hkd),
    JP225(Ccy.Jpy),
    UK100(Ccy.Gbp),
    US30(Ccy.Usd),
    US100(Ccy.Usd),
    US500(Ccy.Usd);

    public final Ccy ccy;

    private Stonk(Ccy ccy) {
	this.ccy = ccy;
    }
}
