package io.adaclub.framework.test;

import io.adaclub.db.StockMetaDAOImpl;
import io.adaclub.db.StockMetaDO;
import io.adaclub.framework.RecallFrameWork;
import io.adaclub.framework.XPosition;
import io.adaclub.tendency.BollCloseBuyPositionImpl;
import io.adaclub.tendency.BollOpenBuyPositionImpl;

import java.util.List;

public class BollTest {

    public static void main(String[] args){
        String stockCode = "SPY";
        List<StockMetaDO> stockMetaDOs = StockMetaDAOImpl.list(stockCode,StockMetaDO.CycleType.DAY.name(), 1000);
        StockMetaDO today = stockMetaDOs.get(0);
        List<XPosition> positions = RecallFrameWork.goThrough(stockMetaDOs,new BollOpenBuyPositionImpl(),new BollCloseBuyPositionImpl());
        System.out.println("\nGo through: ");
        for(XPosition xPosition : positions){
            System.out.println(xPosition);
        }
        System.out.println("\nTotalSize : " + positions.size());
        System.out.println("Profit : " + XPosition.totalProfit(positions,today));
    }
}
