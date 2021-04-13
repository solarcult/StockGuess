package io.adaclub.tendency;

import io.adaclub.db.StockAnalyzeDO;
import io.adaclub.db.StockMetaDAOImpl;
import io.adaclub.db.StockMetaDO;

import java.util.List;

public class TendencyUtil {

    /**
     * 判断此时的趋势是否为震荡市
     * @param stockMetaDOs 传入时,请调整好日期的顺序
     * @return WaveStatus
     */
    public static WaveStatus waveHighLow(List<StockMetaDO> stockMetaDOs,StockMetaDO today){

//        System.out.println("today: "+today);
        int hh = 0;
        int between = 0;
        int ll =0;
        StringBuilder sb = new StringBuilder(stockMetaDOs.size());
        for (StockMetaDO before : stockMetaDOs) {
//            System.out.println(stockMetaDOs.get(i));
            if (today.getClose() > before.getHigh()) {
                hh++;
                sb.append("h");
            } else if (today.getClose() < before.getLow()) {
                ll++;
                sb.append("l");
            } else {
                between++;
                sb.append("-");
            }
        }
        return new WaveStatus(hh,between,ll,sb.toString());
    }

    public static WaveStatus waveClose(List<StockMetaDO> stockMetaDOs, StockMetaDO today){
//        System.out.println("today: "+today);
        int hh = 0;
        int between = 0;
        int ll =0;
        StringBuilder sb = new StringBuilder(stockMetaDOs.size());
        for (StockMetaDO before : stockMetaDOs) {
//            System.out.println(stockMetaDOs.get(i));
            if (today.getClose() > before.getClose()) {
                hh++;
                sb.append("h");
            } else if (today.getClose() < before.getClose()) {
                ll++;
                sb.append("l");
            } else {
                between++;
                sb.append("-");
            }
        }
        return new WaveStatus(hh,between,ll,sb.toString());
    }

    public static WaveStatus waveAverageClose(List<StockAnalyzeDO> stockAnalyzeDOs, StockAnalyzeDO today){
//        System.out.println("today: "+today);
        int hh = 0;
        int between = 0;
        int ll =0;
        StringBuilder sb = new StringBuilder(stockAnalyzeDOs.size());
        for (StockAnalyzeDO before : stockAnalyzeDOs) {
//            System.out.println(stockAnalyzeDOs.get(i));
            if (today.getCloseMean() > before.getCloseMean()) {
                hh++;
                sb.append("h");
            } else if (today.getCloseMean() < before.getCloseMean()) {
                ll++;
                sb.append("l");
            } else {
                between++;
                sb.append("-");
            }
        }
        return new WaveStatus(hh,between,ll,sb.toString());
    }

    public static void main(String[] args) {
        String stockCode = "VT";
        List<StockMetaDO> stockMetaDOs = StockMetaDAOImpl.list(stockCode,StockMetaDO.CycleType.DAY.name(), 1000);
        StockMetaDO today = stockMetaDOs.get(0);
        WaveStatus waveStatus = waveHighLow(stockMetaDOs.subList(1,stockMetaDOs.size()),today);
        System.out.println(waveStatus);
        WaveStatus waveStatusClose = waveClose(stockMetaDOs.subList(1,stockMetaDOs.size()),today);
        System.out.println(waveStatusClose);
    }

    public static class WaveStatus{
        int higherThanHigh;
        int betweenHighLow;
        int lowerThanLow;
        String way;

        public WaveStatus(int hh,int between,int ll,String way){
            this.higherThanHigh = hh;
            this.betweenHighLow = between;
            this.lowerThanLow = ll;
            this.way = way;
        }

        public int getHigherThanHigh() {
            return higherThanHigh;
        }

        public int getBetweenHighLow() {
            return betweenHighLow;
        }

        public int getLowerThanLow() {
            return lowerThanLow;
        }

        public String getWay() {
            return way;
        }

        public int totalSize(){
            return higherThanHigh + betweenHighLow + lowerThanLow;
        }

        @Override
        public String toString() {
            return "WaveStatus{" +
                    "higherThanHigh= " + higherThanHigh +
                    ", betweenHighLow= " + betweenHighLow +
                    ", lowerThanLow= " + lowerThanLow +
                    ", total= " + totalSize() +
                    ", way= " + way +
                    '}';
        }
    }
}
