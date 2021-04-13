package io.adaclub.framework;

import io.adaclub.db.StockMetaDAOImpl;
import io.adaclub.db.StockMetaDO;
import io.adaclub.tendency.TurtleCloseBuyPositionImpl;
import io.adaclub.tendency.TurtleOpenBuyPositionImpl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RecallFrameWork {

    public static int Period_Days_20 = 20;
    public static int Period_Days_10 = 10;
    public static int Period_Days_5 = 5;
    public static int takeOneHand = 100;

    /**
     * 回溯数据
     * @param stockMetaDOs 从数据库查询出来的数据,最近的日期在0的位置
     * @param openBuyPosition
     * @param closeBuyPosition
     */
    public static List<XPosition> goThrough(List<StockMetaDO> stockMetaDOs, OpenBuyPosition openBuyPosition, CloseBuyPosition closeBuyPosition){
        //将0位置变为时间最远的数据,方便编码理解,0代表过去,size()的位置代表现在
        Collections.reverse(stockMetaDOs);
        List<XPosition> positions = new ArrayList<>();

        for(int i = RecallFrameWork.Period_Days_20; i < stockMetaDOs.size(); i++){

            XPosition nowPostionStatus = XPosition.positionStatus(positions);

            StockMetaDO today = stockMetaDOs.get(i);
            boolean hasTomorrow = (i+1) < stockMetaDOs.size();
            if(nowPostionStatus.getQuantity()==0) {
                OpenBuyPosition.BuyPosition buyPosition = openBuyPosition.toBuyOrNotToBuy(stockMetaDOs.subList(i - openBuyPosition.getPeriod(), i), today);
                if (buyPosition.isBuy()) {
                    //take tomorrowOpen price first or take today close price
                    double buyPrice = today.getClose();
                    if (hasTomorrow) {
                        buyPrice = stockMetaDOs.get(i + 1).getOpen();
                    }
                    XPosition buyOneHand = new XPosition(XPosition.BuyOrSellType.BUY.name(), buyPrice, buyPosition.getMany(), today.getDate());
                    positions.add(buyOneHand);
                    System.out.println(buyOneHand);
                }
            }

            nowPostionStatus = XPosition.positionStatus(positions);

            //判断是否需要平仓
            if(nowPostionStatus.getQuantity() > 0) {
                CloseBuyPosition.ClosePosition closePosition = closeBuyPosition.closeBuyPosition(stockMetaDOs.subList(i - closeBuyPosition.getPeriod(), i), today);
                if(closePosition.isSell()){
                    //take tomorrowOpen price first or take today close price
                    double sellPrice = today.getClose();
                    if(hasTomorrow){
                        sellPrice = stockMetaDOs.get(i+1).getOpen();
                    }
                    XPosition closeBuyOneHand = new XPosition(XPosition.BuyOrSellType.SELL.name(), sellPrice, nowPostionStatus.getQuantity(),today.getDate());
                    positions.add(closeBuyOneHand);
                    System.out.println(closeBuyOneHand);
                    System.out.println();
                }
            }
        }

        return positions;
    }

    public static void main(String[] args){
        String stockCode = "VT";
        List<StockMetaDO> stockMetaDOs = StockMetaDAOImpl.list(stockCode,StockMetaDO.CycleType.DAY.name(), 1000);
        StockMetaDO today = stockMetaDOs.get(0);
        List<XPosition> positions =  goThrough(stockMetaDOs,new TurtleOpenBuyPositionImpl(),new TurtleCloseBuyPositionImpl());
        for(XPosition xPosition : positions){
            System.out.println(xPosition);
        }
        System.out.println("Profit : "+XPosition.totalProfit(positions,today));
    }

}
