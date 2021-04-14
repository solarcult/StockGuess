package io.adaclub.framework;

/**
 * 我的钱包,哈哈哈
 */
public class Wallet {

    private double myWallet;

    public Wallet(double wallet){
        this.myWallet = wallet;
    }

    public double spend(double spend){
        if(spend > myWallet){
            throw new RuntimeException("Don'T Have Enough Money. :(");
        }
        myWallet -= spend;
        return myWallet;
    }

    public double fund(double fund){
        myWallet += fund;
        return myWallet;
    }

    public double getMyLeftMoney() {
        return myWallet;
    }

    @Override
    public String toString() {
        return "Wallet{" +
                "myWallet=" + myWallet +
                '}';
    }
}
