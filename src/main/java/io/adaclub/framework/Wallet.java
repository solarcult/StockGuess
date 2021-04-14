package io.adaclub.framework;

/**
 * 我的钱包,哈哈哈
 */
public class Wallet {

    private double myWallet;

    public Wallet(double wallet){
        this.myWallet = wallet;
    }

    public boolean spend(double spend){
        if(spend > myWallet){
            return false;
        }
        myWallet -= spend;
        return true;
    }

    public double fund(double fund){
        myWallet += fund;
        return myWallet;
    }

    public double getMyWallet() {
        return myWallet;
    }

    @Override
    public String toString() {
        return "Wallet{" +
                "myWallet=" + myWallet +
                '}';
    }
}
