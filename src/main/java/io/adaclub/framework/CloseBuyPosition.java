package io.adaclub.framework;

import io.adaclub.db.StockMetaDO;

import java.util.List;

public interface CloseBuyPosition {

    ClosePosition NotClose = new ClosePosition(false,0,"NOT");

    ClosePosition closeBuyPosition(List<StockMetaDO> histories, StockMetaDO today, XPosition nowPosition);

    int getPeriod();

    class ClosePosition{
        private int many;
        private boolean isSell;
        private String describe;

        public ClosePosition(boolean isSell,int many,String describe){
            this.isSell = isSell;
            this.many = many;
            this.describe = describe;
        }

        public int getMany() {
            return many;
        }

        public void setMany(int many) {
            this.many = many;
        }

        public boolean isClose() {
            return isSell;
        }

        public void setSell(boolean sell) {
            isSell = sell;
        }

        public String getDescribe() {
            return describe;
        }

        public void setDescribe(String describe) {
            this.describe = describe;
        }

        @Override
        public String toString() {
            return "ClosePosition{" +
                    "many=" + many +
                    ", isSell=" + isSell +
                    ", describe='" + describe + '\'' +
                    '}';
        }
    }
}
