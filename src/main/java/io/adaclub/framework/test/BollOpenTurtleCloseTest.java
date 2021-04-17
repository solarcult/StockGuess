package io.adaclub.framework.test;

import io.adaclub.db.StockMetaDAOImpl;
import io.adaclub.db.StockMetaDO;
import io.adaclub.framework.RecallFrameWork;
import io.adaclub.framework.RecallResult;
import io.adaclub.framework.Wallet;
import io.adaclub.framework.XPosition;
import io.adaclub.tendency.BollOpenBuyPositionImpl;
import io.adaclub.tendency.TurtleCloseBuyPositionImpl;

import java.util.List;

public class BollOpenTurtleCloseTest {

    public static void main(String[] args){
        Wallet wallet = new Wallet(Wallet.StartMoney);
        wallet.moreHands = true;
        String stockCode = "BABA";
        List<StockMetaDO> stockMetaDOs = StockMetaDAOImpl.list(stockCode,StockMetaDO.CycleType.DAY.name(), 1000);
        StockMetaDO today = stockMetaDOs.get(0);
        RecallResult result = RecallFrameWork.goThrough(stockMetaDOs,new BollOpenBuyPositionImpl(5,20),new TurtleCloseBuyPositionImpl(),wallet,true);
        List<XPosition> positions = result.getPositions();
        for(XPosition xPosition : positions){
            System.out.println(xPosition);
        }
        System.out.println("totalSize : " + positions.size());
        System.out.println("Profit : " + XPosition.totalProfit(positions,today));
    }
}
