package io.adaclub.framework;

import io.adaclub.FileUtil;
import io.adaclub.TimeSeriesChart;
import io.adaclub.db.StockMetaDAOImpl;
import io.adaclub.db.StockMetaDO;
import io.adaclub.tendency.TurtleCloseBuyPositionImpl;
import io.adaclub.tendency.TurtleOpenBuyPositionImpl;
import org.jfree.chart.ChartUtils;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.time.TimeSeriesDataItem;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class RecallFrameWork {

    public static int Period_Days_20 = 20;
    public static int Period_Days_10 = 10;
    public static int Period_Days_5 = 5;
    public static int MIN_PERIOD_DAYS = 3;
    public static int MAX_PERIOD_DAYS = 60;

    //画图时的类型展示文字用
    public static String chartType;

    public static boolean DEBUG = false;

    /**
     * 回溯数据
     * @param stockMetaDOs 从数据库查询出来的数据,最近的日期在0的位置
     * @param openBuyPositionImpl openBuyPositionImpl
     * @param closeBuyPositionImpl closeBuyPositionImpl
     */
    public static RecallResult goThrough(List<StockMetaDO> stockMetaDOs, OpenBuyPosition openBuyPositionImpl, CloseBuyPosition closeBuyPositionImpl, Wallet wallet, boolean isPrintChart){
        List<XPosition> positions = new ArrayList<>();
        List<RecallResult.ProfitPack> profitPacks = new ArrayList<>();
        String stockName = stockMetaDOs.get(0).getStock();
        TimeSeries seriesProfits = new TimeSeries(stockName);
        TimeSeries seriesCapitals = new TimeSeries(stockName);

        for(int i = MAX_PERIOD_DAYS; i < stockMetaDOs.size(); i++){

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
            OpenBuyPosition.BuyPosition buyPosition = OpenBuyPosition.NotBuy;
            CloseBuyPosition.ClosePosition closePosition = CloseBuyPosition.NotClose;
            //判断是否需要平仓
            if(nowPositionStatus.getQuantity() > 0) {
                closePosition = closeBuyPositionImpl.closeBuyPosition(stockMetaDOs.subList(i - closeBuyPositionImpl.getPeriod(), i), today,nowPositionStatus);
            }
            if(nowPositionStatus.getQuantity() <= 0 || !wallet.isOnlyBuyOneHand()) {
                //判断是否要开仓
                buyPosition = openBuyPositionImpl.toBuyOrNotToBuy(stockMetaDOs.subList(i - openBuyPositionImpl.getPeriod(), i), today);
            }

            //算出来又买又卖怎么办?综合考虑一下.
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

            if (buyPosition.isBuy()) {
                //计算钱是否够用
                double spend = actionPrice * buyPosition.getMany();
                if(!wallet.canSpend(spend)){
                    //如果买不起,计算一下到底能买起多少股
                    int canBuyNumber = (int) Math.floor(wallet.getLeftMoney() / actionPrice);
                    System.out.println(" $ WARNING NOT ENOUGH Money : " + stockName+" left $: " + wallet.getLeftMoney() +" change: " + buyPosition.getMany() + " -> " + canBuyNumber );
                    buyPosition.setMany(canBuyNumber);
                }

                if(buyPosition.getMany() > 0) {
                    wallet.spend(buyPosition.getMany() * actionPrice);
                    XPosition buyOneHand = new XPosition(XPosition.BuyOrSellType.BUY.name(), actionPrice, buyPosition.getMany(), today.getDate(), buyPosition.getDescribe());
                    positions.add(buyOneHand);
                    if(DEBUG) {
                        System.out.println(buyOneHand);
                        System.out.println(wallet);
                        System.out.println();
                    }
                }
            }

            if(closePosition.isClose()){
                double fund = actionPrice * closePosition.getMany();
                wallet.fund(fund);
                XPosition closeBuyOneHand = new XPosition(XPosition.BuyOrSellType.SELL.name(), actionPrice, closePosition.getMany(),today.getDate(),closePosition.getDescribe());
                positions.add(closeBuyOneHand);
                if(DEBUG) {
                    System.out.println(closeBuyOneHand);
                    System.out.println(wallet);
                    System.out.println();
                }
            }

            //经历了上面一大堆的操作以后,进行仓位分析,进行数据整理绘图
            Day day = new Day(today.getDate());

            double profit = XPosition.totalProfit(positions,today);
            TimeSeriesDataItem timeProfit = new TimeSeriesDataItem(day,profit);
            seriesProfits.add(timeProfit);

            XPosition inNightPositions = XPosition.positionStatus(positions);
            double todayStockValue = inNightPositions.getQuantity() * today.getClose();
            double capitalAll = wallet.getLeftUsedMoneyTotalCapitalAndProfit() + todayStockValue;
            TimeSeriesDataItem timeCapital = new TimeSeriesDataItem(day,capitalAll);
            seriesCapitals.add(timeCapital);

            //record
            profitPacks.add(new RecallResult.ProfitPack(today.getDate(),capitalAll,profit));
        }

        if(isPrintChart) {
            String profitsName = stockName + "_" + chartType + "_Profits_"+openBuyPositionImpl.keyDescribe() + closeBuyPositionImpl.keyDescribe();
            TimeSeriesChart  profits = new TimeSeriesChart(profitsName, new TimeSeriesCollection(seriesProfits));
            String capitalName = stockName + "_" + chartType + " CapitalAndProfit-ALL "+openBuyPositionImpl.keyDescribe() + closeBuyPositionImpl.keyDescribe() ;
            TimeSeriesChart capital = new TimeSeriesChart(capitalName,new TimeSeriesCollection(seriesCapitals));
            try {
                ChartUtils.saveChartAsJPEG(new File(profitsName + "_" + FileUtil.getTimeString() +".jpg"), profits.getjFreeChart(), 1920, 1080);
                ChartUtils.saveChartAsJPEG(new File(capitalName + "_" + FileUtil.getTimeString() + ".jpg"), capital.getjFreeChart(), 1920, 1080);
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        return new RecallResult(positions,profitPacks,wallet.getUsedMoneyTotalCapital(),openBuyPositionImpl.keyDescribe(), closeBuyPositionImpl.keyDescribe());
    }

    public static void main(String[] args){
        String stockCode = "SPY";
        Wallet wallet = new Wallet();
        List<StockMetaDO> stockMetaDOs = StockMetaDAOImpl.list(stockCode,StockMetaDO.CycleType.DAY.name(), 1000);
        StockMetaDO today = stockMetaDOs.get(0);
        RecallResult result =  goThrough(stockMetaDOs,new TurtleOpenBuyPositionImpl(),new TurtleCloseBuyPositionImpl(),wallet,true);
        for(XPosition xPosition : result.getPositions()){
            System.out.println(xPosition);
        }
        System.out.println("Profit : "+XPosition.totalProfit(result.getPositions(), today));
    }

}
