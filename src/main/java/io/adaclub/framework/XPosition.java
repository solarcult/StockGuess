package io.adaclub.framework;

import io.adaclub.db.StockMetaDO;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class XPosition {

    String buyOrSellType;
    double price;
    double quantity;
    Date date;

    public XPosition(){}

    public XPosition(String buyOrSellType, double price, double quantity,Date date){
        this.buyOrSellType = buyOrSellType;
        this.price = price;
        this.quantity = quantity;
        this.date = date;
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

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "XPosition{" +
                "buyOrSellType='" + buyOrSellType + '\'' +
                ", price=" + price +
                ", quantity=" + quantity +
                ", date=" + date +
                '}';
    }

    public enum BuyOrSellType{
        BUY,SELL
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
        BuyOrSellType resultType = buys.getQuantity()- sells.getQuantity() >= 0 ? BuyOrSellType.BUY : BuyOrSellType.SELL;

        return new XPosition(resultType.name(), profit, buys.getQuantity()- sells.getQuantity(), Calendar.getInstance().getTime());
    }

    public static double totalProfit(List<XPosition> positions, StockMetaDO today){
        XPosition result = positionStatus(positions);
        System.out.println(today);
        System.out.println(result);
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
