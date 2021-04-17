package io.adaclub.framework;

/**
 * 我的钱包,哈哈哈
 */
public class Wallet {

    //起始资金
    public static int StartMoney = 50000;
    //每手股数
    public static int takeOneHand = 10;

    private double leftMoney;
    private int oneHandNumber;
    private boolean onlyBuyOneHand;

    public Wallet(){
        this.leftMoney = StartMoney;
        this.oneHandNumber = takeOneHand;
        this.onlyBuyOneHand = false;
    }

    public Wallet(double totalMoney,int oneHandNumber,boolean onlyBuyOneHand){
        this.leftMoney = totalMoney;
        this.oneHandNumber = oneHandNumber;
        this.onlyBuyOneHand = onlyBuyOneHand;
    }

    public boolean canSpend(double spend){
        return leftMoney >= spend;
    }

    public double spend(double spend){
        if(spend > leftMoney){
            throw new RuntimeException("Don'T Have Enough Money. :(");
        }
        leftMoney -= spend;
        return leftMoney;
    }

    public double fund(double fund){
        leftMoney += fund;
        return leftMoney;
    }

    public double getLeftMoney() {
        return leftMoney;
    }


    public void setLeftMoney(double leftMoney) {
        this.leftMoney = leftMoney;
    }

    public int getOneHandNumber() {
        return oneHandNumber;
    }

    public void setOneHandNumber(int oneHandNumber) {
        this.oneHandNumber = oneHandNumber;
    }

    public boolean isOnlyBuyOneHand() {
        return onlyBuyOneHand;
    }

    public void setOnlyBuyOneHand(boolean onlyBuyOneHand) {
        this.onlyBuyOneHand = onlyBuyOneHand;
    }

    @Override
    public String toString() {
        return "Wallet{" +
                "leftMoney=" + leftMoney +
                ", oneHandNumber=" + oneHandNumber +
                ", onlyBuyOneHand=" + onlyBuyOneHand +
                '}';
    }
}
