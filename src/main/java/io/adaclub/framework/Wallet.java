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

    //为回撤值进行的计算,独立于上面三个值,不影响整体任何计算
    //花出去的钱
    private double usedMoneyTotalCapital;
    //花出去又平仓后赚到的钱,又被花掉,总和留下来的剩余的钱
    private double leftUsedMoneyTotalCapitalAndProfit;
    //leftUsedMoneyTotalCapitalAndProfit + 外面的股票钱 = 整体资产收益

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

        //剩余的钱不够,则总值增加
        if(leftUsedMoneyTotalCapitalAndProfit - spend < 0){
            usedMoneyTotalCapital += spend - leftUsedMoneyTotalCapitalAndProfit;
            leftUsedMoneyTotalCapitalAndProfit = 0;
        }

        return leftMoney;
    }

    public double fund(double fund){
        leftMoney += fund;

        //如果投入的钱卖掉标的后得到更多收益,则提高leftUsedMoneyTotalCapitalAndProfit
        leftUsedMoneyTotalCapitalAndProfit += fund;

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

    public double getUsedMoneyTotalCapital() {
        return usedMoneyTotalCapital;
    }

    public double getLeftUsedMoneyTotalCapitalAndProfit() {
        return leftUsedMoneyTotalCapitalAndProfit;
    }

    @Override
    public String toString() {
        return "Wallet{" +
                "leftMoney=" + leftMoney +
                ", oneHandNumber=" + oneHandNumber +
                ", onlyBuyOneHand=" + onlyBuyOneHand +
                ", usedMoneyTotalCapitalAndProfit=" + usedMoneyTotalCapital +
                ", leftUsedMoneyTotalCapitalAndProfit=" + leftUsedMoneyTotalCapitalAndProfit +
                '}';
    }
}
