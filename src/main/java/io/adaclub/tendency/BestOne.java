package io.adaclub.tendency;

import io.adaclub.framework.RecallFrameWork;
import io.adaclub.framework.RecallResult;

public class BestOne implements Comparable<BestOne>{
    String openDescribe;
    String closeDescribe;
    double earnMoney;
    double maxRetracement;
    RecallResult result;

    public BestOne(String openDescribe, String closeDescribe, double earnMoney, RecallResult result){
        this.openDescribe = openDescribe;
        this.closeDescribe = closeDescribe;
        this.earnMoney = earnMoney;
        this.result = result;
        this.maxRetracement = result.calcMaxRetracement();
    }

    public String getOpenDescribe() {
        return openDescribe;
    }

    public void setOpenDescribe(String openDescribe) {
        this.openDescribe = openDescribe;
    }

    public String getCloseDescribe() {
        return closeDescribe;
    }

    public void setCloseDescribe(String closeDescribe) {
        this.closeDescribe = closeDescribe;
    }

    public double getEarnMoney() {
        return earnMoney;
    }

    public void setEarnMoney(double earnMoney) {
        this.earnMoney = earnMoney;
    }

    @Override
    public int compareTo(BestOne o) {
        //先看回撤
        if(this.maxRetracement < o.maxRetracement){
            return -1;
        }else if(this.maxRetracement > o.maxRetracement){
            return 1;
        }
        //如果回撤相同,则看收益
        if(this.earnMoney > o.earnMoney){
            return -1;
        }else if(this.earnMoney < o.earnMoney){
            return 1;
        }

        return 0;
    }

    public RecallResult getResult() {
        return result;
    }

    public void setResult(RecallResult result) {
        this.result = result;
    }

    public double getMaxRetracement() {
        return maxRetracement;
    }

    @Override
    public String toString() {
        return "BestOne{" +
                "openDescribe='" + openDescribe + '\'' +
                ", closeDescribe='" + closeDescribe + '\'' +
                ", earnMoney=" + earnMoney +
                ", maxRetracement=" + maxRetracement +
                ", result=" + result +
                '}';
    }
}
