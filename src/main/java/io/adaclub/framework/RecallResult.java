package io.adaclub.framework;

import java.util.Date;
import java.util.List;

public class RecallResult {
    List<XPosition> positions;
    List<ProfitPack> profitPacks;
    double maxRetracement;
    double profit;
    //总共花出去的钱
    double capitalTotalSpent;
    //花出去钱和收益,花了就算,理论上应该等于profit+capitalTotalSpent
    double capitalAndProfitResult;
    double roi;
    public RecallResult(List<XPosition> positions, List<ProfitPack> profitPacks,double capitalTotalSpent){
        this.positions = positions;
        this.profitPacks = profitPacks;
        this.maxRetracement = calcMaxRetracement();
        this.profit = profitPacks.get(profitPacks.size()-1).getProfit();
        this.capitalTotalSpent = capitalTotalSpent;
        this.capitalAndProfitResult = profitPacks.get(profitPacks.size()-1).getCapitalAndProfitResult();;
        this.roi = profit/capitalTotalSpent;
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
            double todayCapitalValue = profitPacks.get(i).getCapitalAndProfitResult();
            for(int j = i+1; j < profitPacks.size();j++){
                double nextDayCapitalValue = profitPacks.get(j).getCapitalAndProfitResult();
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

    public double getCapitalTotalSpent() {
        return capitalTotalSpent;
    }

    public void setCapitalTotalSpent(double capitalTotalSpent) {
        this.capitalTotalSpent = capitalTotalSpent;
    }

    public double getCapitalAndProfitResult() {
        return capitalAndProfitResult;
    }

    public void setCapitalAndProfitResult(double capitalAndProfitResult) {
        this.capitalAndProfitResult = capitalAndProfitResult;
    }

    public double getRoi() {
        return roi;
    }

    public void setRoi(double roi) {
        this.roi = roi;
    }

    @Override
    public String toString() {
        return "RecallResult{" +
                "maxRetracement=" + maxRetracement +
                ", profit=" + profit +
                ", capitalTotalSpent=" + capitalTotalSpent +
                ", capitalAndProfitResult=" + capitalAndProfitResult +
                ", roi=" + roi +
//                "， positions=" + positions +
//                ", profitPacks=" + profitPacks +
                '}';
    }

    public static class ProfitPack{
        Date date;
        double profit;
        double capitalAndProfitResult;
        public ProfitPack(Date date,double capitalAndProfitResult, double profit){
            this.date = date;
            this.capitalAndProfitResult = capitalAndProfitResult;
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

        public double getCapitalAndProfitResult() {
            return capitalAndProfitResult;
        }

        public void setCapitalAndProfitResult(double capitalAndProfitResult) {
            this.capitalAndProfitResult = capitalAndProfitResult;
        }

        @Override
        public String toString() {
            return "ProfitPack{" +
                    "date=" + date +
                    ", profit=" + profit +
                    ", capitalAndProfitResult=" + capitalAndProfitResult +
                    '}';
        }
    }
}
