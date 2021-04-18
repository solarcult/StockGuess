package io.adaclub.framework;

import io.adaclub.db.StockMetaDO;

import java.util.List;

public interface OpenBuyPosition {

    BuyPosition NotBuy = new BuyPosition(false,0);

    BuyPosition toBuyOrNotToBuy(List<StockMetaDO> histories, StockMetaDO today);

    int getPeriod();

    String keyDescribe();

    class BuyPosition{
        private int many;
        private boolean isBuy;
        private String describe;

        public BuyPosition(boolean isBuy,int many){
            this.isBuy = isBuy;
            this.many = many;
        }

        public int getMany() {
            return many;
        }

        public void setMany(int many) {
            this.many = many;
        }

        public boolean isBuy() {
            return isBuy;
        }

        public void setBuy(boolean buy) {
            isBuy = buy;
        }

        public String getDescribe() {
            return describe;
        }

        public void setDescribe(String describe) {
            this.describe = describe;
        }

        @Override
        public String toString() {
            return "BuyPosition{" +
                    "many=" + many +
                    ", isBuy=" + isBuy +
                    ", describe='" + describe + '\'' +
                    '}';
        }
    }
}
