package io.adaclub.framework.test;

import io.adaclub.db.StockMetaDAOImpl;
import io.adaclub.db.StockMetaDO;
import io.adaclub.framework.RecallFrameWork;
import io.adaclub.framework.XPosition;
import io.adaclub.tendency.TurtleCloseBuyPositionImpl;
import io.adaclub.tendency.TurtleOpenBuyPositionImpl;

import java.util.List;

public class TurtleTest {

    public static void main(String[] args){
        RecallFrameWork.chartType = "Turtle";
        String stockCode = "QQQ";
        List<StockMetaDO> stockMetaDOs = StockMetaDAOImpl.list(stockCode,StockMetaDO.CycleType.DAY.name(), 1000);
        StockMetaDO today = stockMetaDOs.get(0);
        List<XPosition> positions = RecallFrameWork.goThrough(stockMetaDOs,new TurtleOpenBuyPositionImpl(),new TurtleCloseBuyPositionImpl());
        System.out.println("\nGo through: ");
        for(XPosition xPosition : positions){
            System.out.println(xPosition);
        }
        System.out.println("\ntotalSize : " + positions.size());
        System.out.println("Profit : "+XPosition.totalProfit(positions,today));
    }
}
