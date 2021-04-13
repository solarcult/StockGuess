package io.adaclub.framework;

import io.adaclub.db.StockMetaDO;

import java.util.List;

public interface CloseBuyPosition {

    ClosePosition NotClose = new ClosePosition(false,0);

    ClosePosition closeBuyPosition(List<StockMetaDO> history, StockMetaDO today);

    int getPeriod();

    class ClosePosition{
        private int many;
        private boolean isSell;

        public ClosePosition(boolean isSell,int many){
            this.isSell = isSell;
            this.many = many;
        }

        public int getMany() {
            return many;
        }

        public void setMany(int many) {
            this.many = many;
        }

        public boolean isSell() {
            return isSell;
        }

        public void setSell(boolean sell) {
            isSell = sell;
        }

        @Override
        public String toString() {
            return "BuyPosition{" +
                    "many=" + many +
                    ", isSell=" + isSell +
                    '}';
        }
    }
}
