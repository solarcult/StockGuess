package io.adaclub.framework;

import io.adaclub.db.StockMetaDO;

import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class XPosition {

    String buyOrSellType;
    double price;
    int quantity;
    Date date;
    //已经开仓的股票,已经平仓了几次
    int leftPositionBeforeClosedTimes;
    String describe;

    public XPosition(){}

    public XPosition(String buyOrSellType, double price, int quantity,Date date){
        this.buyOrSellType = buyOrSellType;
        this.price = price;
        this.quantity = quantity;
        this.date = date;
    }

    public XPosition(String buyOrSellType, double price, int quantity,Date date,int leftPositionBeforeClosedTimes){
        this.buyOrSellType = buyOrSellType;
        this.price = price;
        this.quantity = quantity;
        this.date = date;
        this.leftPositionBeforeClosedTimes = leftPositionBeforeClosedTimes;
    }

    public XPosition(String buyOrSellType, double price, int quantity,Date date,String describe){
        this.buyOrSellType = buyOrSellType;
        this.price = price;
        this.quantity = quantity;
        this.date = date;
        this.describe = describe;
    }

    public String getBuyOrSellType() {
        return buyOrSellType;
    }

    public void setBuyOrSellType(String buyOrSellType) {
        this.buyOrSellType = buyOrSellType;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getLeftPositionBeforeClosedTimes() {
        return leftPositionBeforeClosedTimes;
    }

    public void setLeftPositionBeforeClosedTimes(int leftPositionBeforeClosedTimes) {
        this.leftPositionBeforeClosedTimes = leftPositionBeforeClosedTimes;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public enum BuyOrSellType{
        BUY,SELL,NONE
    }

    @Override
    public String toString() {

        String leftShow = leftPositionBeforeClosedTimes > 0 ? (", lpbct=" + leftPositionBeforeClosedTimes) : "";
        String describeShow = describe != null ? (", describe=" + describe) : "";
        return "XPosition{" +
                "buyOrSellType='" + buyOrSellType + '\'' +
                ", price=" + price +
                ", quantity=" + quantity +
                ", date=" + date +
                describeShow +
                leftShow +
                '}';
    }

    public static XPosition positionStatus(List<XPosition> positions){

        XPosition buys = new XPosition();
        buys.setBuyOrSellType(BuyOrSellType.BUY.name());
        XPosition sells = new XPosition();
        sells.setBuyOrSellType(BuyOrSellType.SELL.name());
        double profit = 0;

        for(XPosition position : positions){
            if(BuyOrSellType.BUY.name().equals(position.getBuyOrSellType())){
                buys.setQuantity(buys.getQuantity() + position.getQuantity());
                profit -= position.getPrice() * position.getQuantity();
                continue;
            }
            if(BuyOrSellType.SELL.name().equals(position.getBuyOrSellType())){
                sells.setQuantity(sells.getQuantity() + position.getQuantity());
                profit += position.getPrice() * position.getQuantity();
            }
        }

        int leftQuantity = buys.getQuantity() - sells.getQuantity();
        BuyOrSellType resultType = leftQuantity >= 0 ? BuyOrSellType.BUY : BuyOrSellType.SELL;
        if(leftQuantity == 0){
            resultType = BuyOrSellType.NONE;
        }

        //现有的持仓,计算之前清仓前已经卖了几次,只要碰到最近一次BUY代表之前的不统计,会出现BUY,BUY,SELL,BUY中有一半没卖,但就算重新开始.
        int leftPositionBeforeClosedTimes = 0;
        if(BuyOrSellType.BUY.equals(resultType)){
            Collections.reverse(positions);
            for(XPosition x : positions){
                if(BuyOrSellType.BUY.name().equals(x.getBuyOrSellType())){
                    break;
                }
                if(BuyOrSellType.SELL.name().equals(x.getBuyOrSellType())){
                    leftPositionBeforeClosedTimes++;
                }
            }
            Collections.reverse(positions);
        }

        return new XPosition(resultType.name(), profit, leftQuantity, Calendar.getInstance().getTime(),leftPositionBeforeClosedTimes);
    }

    public static double totalProfit(List<XPosition> positions, StockMetaDO today){
        XPosition result = positionStatus(positions);

        if(result.getQuantity() > 0){
            if(BuyOrSellType.BUY.name().equals(result.getBuyOrSellType())){
                return today.getClose() * result.getQuantity() + result.getPrice();
            }
        }
        if(result.getQuantity() < 0){
            if(BuyOrSellType.SELL.name().equals(result.getBuyOrSellType())){
                return result.getPrice() - today.getClose() * result.getQuantity() ;
            }
        }

        return result.getPrice();
    }
}
