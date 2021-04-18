package io.adaclub.framework;

import java.util.Objects;

public class Pareto implements Comparable<Pareto>{
    int retracement;
    int earn;
    RecallResult recallResult;

    public Pareto(int retracement, int earn,RecallResult bestOne){
        this.retracement = retracement;
        this.earn = earn;
        this.recallResult = bestOne;
    }

    public int getRetracement() {
        return retracement;
    }

    public void setRetracement(int retracement) {
        this.retracement = retracement;
    }

    public int getEarn() {
        return earn;
    }

    public void setEarn(int earn) {
        this.earn = earn;
    }

    public RecallResult getRecallResult() {
        return recallResult;
    }

    public void setRecallResult(RecallResult recallResult) {
        this.recallResult = recallResult;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pareto pareto = (Pareto) o;
        return retracement == pareto.retracement && earn == pareto.earn;
    }

    @Override
    public int hashCode() {
        return Objects.hash(retracement, earn);
    }

    @Override
    public String toString() {
        return "Pareto{" +
                "retracement=" + retracement +
                ", earn=" + earn +
                ", bestOne=" + recallResult +
                '}';
    }

    @Override
    public int compareTo(Pareto o) {
        //先看回撤
        if(this.getRetracement() < o.getRetracement()){
            return -1;
        }else if(this.getRetracement() > o.getRetracement()){
            return 1;
        }
        //如果回撤相同,则看收益
        if(this.getEarn() > o.getEarn()){
            return -1;
        }else if(this.getEarn() < o.getEarn()){
            return 1;
        }

        return 0;
    }

}
