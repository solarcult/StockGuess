package io.adaclub.tendency;

import io.adaclub.framework.RecallFrameWork;
import io.adaclub.framework.RecallResult;

public class BestOne implements Comparable<BestOne>{
    String openDescribe;
    String closeDescribe;
    RecallResult result;

    public BestOne(String openDescribe, String closeDescribe, RecallResult result){
        this.openDescribe = openDescribe;
        this.closeDescribe = closeDescribe;
        this.result = result;
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

    @Override
    public int compareTo(BestOne o) {
        //先看回撤
        if(this.result.getMaxRetracement() < o.result.getMaxRetracement()){
            return -1;
        }else if(this.result.getMaxRetracement() > o.result.getMaxRetracement()){
            return 1;
        }
        //如果回撤相同,则看收益
        if(this.result.getProfit() > o.result.getProfit()){
            return -1;
        }else if(this.result.getProfit() < o.result.getProfit()){
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


    @Override
    public String toString() {
        return "BestOne{" +
                "open='" + openDescribe + '\'' +
                ", close='" + closeDescribe + '\'' +
                ", result=" + result +
                '}';
    }
}
