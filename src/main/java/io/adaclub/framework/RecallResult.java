package io.adaclub.framework;

import java.util.Date;
import java.util.List;

public class RecallResult {
    List<XPosition> positions;
    List<ProfitPack> profitPacks;
    double maxRetracement;
    double profit;
    double capitalValue;
    double roi;
    public RecallResult(List<XPosition> positions, List<ProfitPack> profitPacks){
        this.positions = positions;
        this.profitPacks = profitPacks;
        this.maxRetracement = calcMaxRetracement();
        this.profit = profitPacks.get(profitPacks.size()-1).getProfit();
        this.capitalValue = profitPacks.get(profitPacks.size()-1).getCapitalStockValue();
        this.roi = profit/capitalValue;
    }

    public List<XPosition> getPositions() {
        return positions;
    }

    public void setPositions(List<XPosition> positions) {
        this.positions = positions;
    }

    public List<ProfitPack> getProfitPacks() {
        return profitPacks;
    }

    public void setProfitPacks(List<ProfitPack> profitPacks) {
        this.profitPacks = profitPacks;
    }

    public double calcMaxRetracement(){
        double maxRetracement = 0;
        for(int i=0; i < profitPacks.size();i++){
            double todayCapitalValue = profitPacks.get(i).getCapitalStockValue();
            double profit = profitPacks.get(i).getProfit();
            //还没下单时
            if(profit == 0){
                continue;
            }
            for(int j = i+1; j < profitPacks.size();j++){
                double nextDayCapitalValue = profitPacks.get(j).getCapitalStockValue();
                //中间空仓时
                if(nextDayCapitalValue == 0){
                    continue;
                }
                double nowRetracement = (todayCapitalValue - nextDayCapitalValue) / todayCapitalValue;
                if(nowRetracement > maxRetracement){
                    maxRetracement = nowRetracement;
                }
            }
        }
        return maxRetracement;
    }

    public double getMaxRetracement() {
        return maxRetracement;
    }

    public void setMaxRetracement(double maxRetracement) {
        this.maxRetracement = maxRetracement;
    }

    public double getProfit() {
        return profit;
    }

    public void setProfit(double profit) {
        this.profit = profit;
    }

    @Override
    public String toString() {
        return "RecallResult{" +
                "positions=" + positions +
                ", profitPacks=" + profitPacks +
                ", maxRetracement=" + maxRetracement +
                ", profit=" + profit +
                ", capitalValue=" + capitalValue +
                ", roi=" + roi +
                '}';
    }

    public static class ProfitPack{
        Date date;
        double profit;
        double capitalStockValue;
        public ProfitPack(Date date,double capitalStockValue, double profit){
            this.date = date;
            this.capitalStockValue = capitalStockValue;
            this.profit = profit;

        }

        public Date getDate() {
            return date;
        }

        public void setDate(Date date) {
            this.date = date;
        }

        public double getProfit() {
            return profit;
        }

        public void setProfit(double profit) {
            this.profit = profit;
        }

        public double getCapitalStockValue() {
            return capitalStockValue;
        }

        public void setCapitalStockValue(double capitalStockValue) {
            this.capitalStockValue = capitalStockValue;
        }

        @Override
        public String toString() {
            return "ProfitPack{" +
                    "date=" + date +
                    ", capitalStockValue=" + capitalStockValue +
                    ", profit=" + profit +
                    '}';
        }
    }
}
