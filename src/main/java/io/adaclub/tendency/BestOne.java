package io.adaclub.tendency;

public class BestOne implements Comparable<BestOne>{
    String openDescribe;
    String closeDescribe;
    double earnMoney;

    public BestOne(String openDescribe,String closeDescribe,double earnMoney){
        this.openDescribe = openDescribe;
        this.closeDescribe = closeDescribe;
        this.earnMoney = earnMoney;
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
    public String toString() {
        return "BestOne{" +
                "openDescribe='" + openDescribe + '\'' +
                ", closeDescribe='" + closeDescribe + '\'' +
                ", earnMoney=" + earnMoney +
                '}';
    }

    @Override
    public int compareTo(BestOne o) {
        if(this.earnMoney > o.earnMoney){
            return -1 ;
        }else if(this.earnMoney < o.earnMoney){
            return 1;
        }
        return 0;
    }
}
