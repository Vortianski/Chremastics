package xox.labvorty.chremastics.data.utilities;

public class ClientData {
    private static double bentCoinChance = 0.4;
    private static double tradersInsigniaDiscount = 0.1;

    public static double getBentCoinChance() {
        return bentCoinChance;
    }

    public static double getTradersInsigniaDiscount() {
        return tradersInsigniaDiscount;
    }

    public static void setBentCoinChance(double bentCoinChance) {
        ClientData.bentCoinChance = bentCoinChance;
    }

    public static void setTradersInsigniaDiscount(double tradersInsigniaDiscount) {
        ClientData.tradersInsigniaDiscount = tradersInsigniaDiscount;
    }
}
