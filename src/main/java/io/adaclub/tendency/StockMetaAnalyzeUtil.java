package io.adaclub.tendency;

import io.adaclub.db.StockAnalyzeDAOImpl;
import io.adaclub.db.StockAnalyzeDO;
import io.adaclub.db.StockMetaDAOImpl;
import io.adaclub.db.StockMetaDO;
import org.apache.commons.math3.stat.descriptive.SummaryStatistics;

import java.util.Collections;
import java.util.List;

public class StockMetaAnalyzeUtil {

    /**
     * 数据时间从近到远，按照数据库查询出来的数据
     * @param stockMetaDOs
     * @param X
     */
    public static void analyzeXDays(List<StockMetaDO> stockMetaDOs, int X){

        SummaryStatistics close = new SummaryStatistics();
        SummaryStatistics high = new SummaryStatistics();
        SummaryStatistics low = new SummaryStatistics();
        SummaryStatistics volumeRate = new SummaryStatistics();
        SummaryStatistics atr = new SummaryStatistics();
        SummaryStatistics findBiggest = new SummaryStatistics();


        for(int i = 0; i < stockMetaDOs.size(); i++) {
            //为什么要-1，因为atr需要取前一天的数据,最开始的数据会出现没有前一天的问题,还有一个-1是list的长度从0开始计算的原因
            int left = stockMetaDOs.size() - i - X - 1 - 1;
            if (left < 0) {
                continue;
            }

            //clear
            high.clear();
            close.clear();
            low.clear();
            volumeRate.clear();
            atr.clear();
            double pathLength = 0;

            StockMetaDO now = stockMetaDOs.get(i);
//            System.out.println("true today: " + now);

            //for 算最近X天的均值
            for (int j = i ; j <= i + X; j++) {
                //算出来的是从以前到现在的数据
                int position = j;
                StockMetaDO stockMetaDO = stockMetaDOs.get(position);
//                System.out.println(stockMetaDO);
                close.addValue(stockMetaDO.getClose());
                high.addValue(stockMetaDO.getHigh());
                low.addValue(stockMetaDO.getLow());
                volumeRate.addValue(stockMetaDO.getChangeVolumeRate());

                //calculate 今天的atr波动值
                findBiggest.clear();
                StockMetaDO yesterday = stockMetaDOs.get(position + 1);
                StockMetaDO today = stockMetaDOs.get(position);
                double way1 = today.getHigh() - today.getLow();
                double way2 = Math.abs(today.getHigh() - yesterday.getClose());
                double way3 = Math.abs(today.getLow() - yesterday.getClose());
                //找到way123中最大的
                findBiggest.addValue(way1);
                findBiggest.addValue(way2);
                findBiggest.addValue(way3);
                double big = findBiggest.getMax();
                atr.addValue(big);

                //计算每天股票价格走过的路径算effect
                pathLength += Math.abs(yesterday.getClose() - today.getClose());
            }

            //calculate effect
            StockMetaDO lastDay = stockMetaDOs.get(i);
            StockMetaDO firstDay = stockMetaDOs.get(i+X);
            double effect = Math.abs(lastDay.getClose()-firstDay.getClose())/pathLength;

//            System.out.println("firstDay "+firstDay);
//            System.out.println("lastDay "+lastDay);
//            System.out.println(i);
//            System.out.println("high "+high.getMean()+" "+high.getStandardDeviation());
//            System.out.println("close "+close.getMean()+" "+close.getStandardDeviation());
//            System.out.println("low "+low.getMean()+" "+low.getStandardDeviation());
//            System.out.println(volumeRate.getSum()*100);
//            System.out.println("atr "+atr.getMean() + " " +atr.getStandardDeviation());
//            System.out.println("effect " + effect);

            StockAnalyzeDO stockAnalyzeDO = new StockAnalyzeDO();
            stockAnalyzeDO.setStockId(now.getId());
            stockAnalyzeDO.setCycle(now.getCycle());
            stockAnalyzeDO.setHighMean(high.getMean());
            stockAnalyzeDO.setHighSd(high.getStandardDeviation());
            stockAnalyzeDO.setCloseMean(close.getMean());
            stockAnalyzeDO.setCloseSd(close.getStandardDeviation());
            stockAnalyzeDO.setLowMean(low.getMean());
            stockAnalyzeDO.setLowSd(low.getStandardDeviation());
            stockAnalyzeDO.setAtr(atr.getMean());
            stockAnalyzeDO.setChangeVolumeRateTotal(volumeRate.getSum());
            stockAnalyzeDO.setEffect(effect);
            stockAnalyzeDO.setStock(lastDay.getStock());
            stockAnalyzeDO.setxDays(X);

            StockAnalyzeDAOImpl.insert(stockAnalyzeDO);
            System.out.println("insert analyze: "+ stockAnalyzeDO);

        }
    }

    public static void main(String[] args){

        List<StockMetaDO> stockMetaDOs = StockMetaDAOImpl.list("MSFT",StockMetaDO.CycleType.DAY.name(), 1000);
        System.out.println(stockMetaDOs.get(0));
//        analyzeXDays(stockMetaDOs,20);
    }
}
