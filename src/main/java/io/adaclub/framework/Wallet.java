package io.adaclub.framework;

/**
 * 我的钱包,哈哈哈
 */
public class Wallet {

    //起始资金
    public static int StartMoney = 100000;

    private double leftMoney;

    public Wallet(double totalMoney){
        this.leftMoney = totalMoney;
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

    @Override
    public String toString() {
        return "Wallet{" +
                "myWallet=" + leftMoney +
                '}';
    }
}
