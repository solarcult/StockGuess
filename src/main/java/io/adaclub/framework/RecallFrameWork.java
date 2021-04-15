package io.adaclub.framework;

import io.adaclub.TimeSeriesChart;
import io.adaclub.db.StockMetaDAOImpl;
import io.adaclub.db.StockMetaDO;
import io.adaclub.tendency.TurtleCloseBuyPositionImpl;
import io.adaclub.tendency.TurtleOpenBuyPositionImpl;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RecallFrameWork {

    public static int Period_Days_20 = 20;
    public static int Period_Days_10 = 10;
    public static int Period_Days_5 = 5;
    public static int MIN_PERIOD_DAYS = 3;
    public static int MAX_PERIOD_DAYS = 60;

    //每手股数
    public static int takeOneHand = 100;
    //最大允许持有手数
    public static int maxHand = 50;
    //起始资金
    public static int StartMoney = 20000000;
    //画图时的类型展示文字用
    public static String chartType;

    public static boolean DEBUG = false;

    /**
     * 回溯数据
     * @param stockMetaDOs 从数据库查询出来的数据,最近的日期在0的位置
     * @param openBuyPositionImpl openBuyPositionImpl
     * @param closeBuyPositionImpl closeBuyPositionImpl
     */
    public static List<XPosition> goThrough(List<StockMetaDO> stockMetaDOs, OpenBuyPosition openBuyPositionImpl, CloseBuyPosition closeBuyPositionImpl){
        Wallet myWallet = new Wallet(StartMoney);
        List<XPosition> positions = new ArrayList<>();
        String stockName = stockMetaDOs.get(0).getStock();
        TimeSeries series = new TimeSeries(stockName);

        for(int i = openBuyPositionImpl.getPeriod(); i < stockMetaDOs.size(); i++){

            XPosition nowPositionStatus = XPosition.positionStatus(positions);

            StockMetaDO today = stockMetaDOs.get(i);
            StockMetaDO tomorrow = null;
            boolean hasTomorrow = (i + 1) < stockMetaDOs.size();
            if(hasTomorrow) {
                tomorrow = stockMetaDOs.get(i + 1);
            }
            //take tomorrowOpen price first or take today close price
            double actionPrice = today.getClose();
            if(tomorrow != null){
                actionPrice = tomorrow.getOpen();
            }

            CloseBuyPosition.ClosePosition closePosition = CloseBuyPosition.NotClose;
            //判断是否需要平仓
            if(nowPositionStatus.getQuantity() > 0) {
                closePosition = closeBuyPositionImpl.closeBuyPosition(stockMetaDOs.subList(i - closeBuyPositionImpl.getPeriod(), i), today,nowPositionStatus);
            }
            //判断是否要开仓
            OpenBuyPosition.BuyPosition buyPosition = openBuyPositionImpl.toBuyOrNotToBuy(stockMetaDOs.subList(i - openBuyPositionImpl.getPeriod(), i), today);

            //综合考虑怎么办
            if(buyPosition.isBuy() && closePosition.isClose()){
                int finalQuantity = buyPosition.getMany() - closePosition.getMany();
                if(finalQuantity > 0){
                    //buy is bigger than close
                    buyPosition.setMany(finalQuantity);
                    buyPosition.setDescribe("REMAKE");
                    closePosition = CloseBuyPosition.NotClose;
                }else if(finalQuantity < 0){
                    closePosition.setMany(Math.abs(finalQuantity));
                    closePosition.setDescribe(closePosition.getDescribe()+"-REMAKE");
                    buyPosition = OpenBuyPosition.NotBuy;
                }else {
                    //same amount
                    buyPosition = OpenBuyPosition.NotBuy;
                    closePosition = CloseBuyPosition.NotClose;
                }
            }

            int leftCanBuyNumber = maxHand * takeOneHand - nowPositionStatus.getQuantity();
            if (buyPosition.isBuy() && leftCanBuyNumber > 0 ) {

                //计算手数是否超过最大允许值
                if(buyPosition.getMany() > leftCanBuyNumber){
                    System.out.println(" Left Hand WARNING USED MAX Hand : " + stockName +" : " +buyPosition.getMany() +" -> " + leftCanBuyNumber);
                    buyPosition.setMany(leftCanBuyNumber);
                }

                //计算钱是否够用
                double spend = actionPrice * buyPosition.getMany();
                if(myWallet.getMyLeftMoney() < spend){
                    int canBuyNumber = (int) Math.floor(myWallet.getMyLeftMoney() / actionPrice);
                    System.out.println(" $ WARNING NOT ENOUGH Money : " + stockName+" left $: " + myWallet.getMyLeftMoney() +" change: " + buyPosition.getMany() + " -> " + canBuyNumber );
                    buyPosition.setMany(canBuyNumber);
                }

                if(buyPosition.getMany() > 0) {
                    myWallet.spend(buyPosition.getMany() * actionPrice);
                    XPosition buyOneHand = new XPosition(XPosition.BuyOrSellType.BUY.name(), actionPrice, buyPosition.getMany(), today.getDate(), buyPosition.getDescribe());
                    positions.add(buyOneHand);
                    if(DEBUG) {
                        System.out.println(buyOneHand);
                        System.out.println(myWallet);
                        System.out.println();
                    }
                }
            }

            if(closePosition.isClose()){
                double fund = actionPrice * closePosition.getMany();
                myWallet.fund(fund);
                XPosition closeBuyOneHand = new XPosition(XPosition.BuyOrSellType.SELL.name(), actionPrice, closePosition.getMany(),today.getDate(),closePosition.getDescribe());
                positions.add(closeBuyOneHand);
                if(DEBUG) {
                    System.out.println(closeBuyOneHand);
                    System.out.println(myWallet);
                    System.out.println();
                }
            }

            //进行数据整理绘图
            Day day = new Day(today.getDate());

//            XPosition result = XPosition.positionStatus(positions);
//            series.add(day,myWallet.getMyWallet() + today.getClose() * result.getQuantity());

            series.add(day,XPosition.totalProfit(positions,today) );
        }

//        XYDataset dataset = new TimeSeriesCollection(series);
//        TimeSeriesChart timeSeriesChart = new TimeSeriesChart(stockName+" "+chartType,dataset);
//        timeSeriesChart.pack();
//        timeSeriesChart.setVisible(true);

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
