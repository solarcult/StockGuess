package io.adaclub.framework.test;

import io.adaclub.db.StockMetaDAOImpl;
import io.adaclub.db.StockMetaDO;
import io.adaclub.framework.RecallFrameWork;
import io.adaclub.framework.RecallResult;
import io.adaclub.framework.Wallet;

import java.util.Collections;
import java.util.List;

public class RRR {
    public static void main(String[] args) {
        RecallFrameWork.chartType = "Boll";
        Wallet wallet = new Wallet();
        String stockCode = "MSFT";
        List<StockMetaDO> stockMetaDOs = StockMetaDAOImpl.list(stockCode,StockMetaDO.CycleType.DAY.name(), 1000);
        StockMetaDO today = stockMetaDOs.get(0);
        //将0位置变为时间最远的数据,方便编码理解,0代表过去,size()的位置代表现在
        Collections.reverse(stockMetaDOs);
        RecallFrameWork.DEBUG = true;
        RecallResult result = BollTest.tryOnce(stockMetaDOs,today,wallet,18,18,3,true);
        System.out.println(result.getMaxRetracement()*100+" , "+result.getProfit()+" , "+result.getRoi()*100);
    }
}
