package io.adaclub.framework.test;

import io.adaclub.db.StockMetaDAOImpl;
import io.adaclub.db.StockMetaDO;
import io.adaclub.framework.CloseBuyPosition;
import io.adaclub.framework.OpenBuyPosition;
import io.adaclub.framework.RecallFrameWork;
import io.adaclub.framework.XPosition;
import io.adaclub.tendency.BestOne;
import io.adaclub.tendency.BollCloseBuyPositionImpl;
import io.adaclub.tendency.BollOpenBuyPositionImpl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BollTest {

    public static void main(String[] args){
        RecallFrameWork.chartType = "Boll";
        String stockCode = "QQQ";
        List<StockMetaDO> stockMetaDOs = StockMetaDAOImpl.list(stockCode,StockMetaDO.CycleType.DAY.name(), 1000);
        StockMetaDO today = stockMetaDOs.get(0);

        //将0位置变为时间最远的数据,方便编码理解,0代表过去,size()的位置代表现在
        Collections.reverse(stockMetaDOs);
        List<BestOne> bestOnes = new ArrayList<>();

        for(int i=RecallFrameWork.MIN_PERIOD_DAYS; i <= RecallFrameWork.MAX_PERIOD_DAYS;i++) {
            System.out.println("i: "+i);
            for (int j = RecallFrameWork.MIN_PERIOD_DAYS; j <= RecallFrameWork.MAX_PERIOD_DAYS; j++) {
                System.out.println("  j: "+j);
                for (int k = RecallFrameWork.MIN_PERIOD_DAYS; k <= RecallFrameWork.MAX_PERIOD_DAYS; k++) {
                    System.out.println("    k: "+k);
                    OpenBuyPosition openBuyPosition = new BollOpenBuyPositionImpl(i, j);
                    CloseBuyPosition closeBuyPosition = new BollCloseBuyPositionImpl(k);
                    List<XPosition> positions = RecallFrameWork.goThrough(stockMetaDOs, openBuyPosition, closeBuyPosition);
                    if (RecallFrameWork.DEBUG) {
                        System.out.println("\nGo through: ");
                        for (XPosition xPosition : positions) {
                            System.out.println(xPosition);
                        }
                        System.out.println("\nTotalSize : " + positions.size());
                        System.out.println("Profit : " + XPosition.totalProfit(positions, today));
                    }
                    bestOnes.add(new BestOne(openBuyPosition.toString(), closeBuyPosition.toString(), XPosition.totalProfit(positions, today)));
                }
            }
        }
        Collections.sort(bestOnes);
        for (int i= 0;i<10;i++){
            System.out.println(bestOnes.get(i));
        }
    }
}
