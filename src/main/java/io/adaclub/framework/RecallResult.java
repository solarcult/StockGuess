package io.adaclub.framework;

import java.util.Date;
import java.util.List;

public class RecallResult {
    List<XPosition> positions;
    List<ProfitPack> profitPacks;
    public RecallResult(List<XPosition> positions, List<ProfitPack> profitPacks){
        this.positions = positions;
        this.profitPacks = profitPacks;
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

    @Override
    public String toString() {
        return "RecallResult{" +
                "positions=" + positions +
                ", profitPacks=" + profitPacks +
                '}';
    }

    public double calcMaxRetracement(){
        double maxRetracement = 0;
        for(int i=0; i < profitPacks.size();i++){
            double todayMoney = Wallet.StartMoney + profitPacks.get(i).getProfit();
            for(int j = i+1; j < profitPacks.size();j++){
                double nextDayMoney = Wallet.StartMoney +  profitPacks.get(j).getProfit();
                double nowRetracement = (todayMoney - nextDayMoney) / todayMoney;
                if(nowRetracement > maxRetracement){
                    maxRetracement = nowRetracement;
                }
            }
        }
        return maxRetracement;
    }

    public static class ProfitPack{
        Date date;
        double profit;

        public ProfitPack(Date date, double profit){
            this.date = date;
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

        @Override
        public String toString() {
            return "ProfitPack{" +
                    "date=" + date +
                    ", profit=" + profit +
                    '}';
        }
    }
}
