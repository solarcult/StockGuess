package io.adaclub.tendency;

import io.adaclub.db.StockAnalyzeDO;
import io.adaclub.db.StockAnalyzeDAOImpl;
import io.adaclub.db.StockMetaDO;
import io.adaclub.framework.CloseBuyPosition;
import io.adaclub.framework.RecallFrameWork;
import io.adaclub.framework.XPosition;

import java.util.List;

public class BollCloseBuyPositionImpl implements CloseBuyPosition {

    //卖出点的x日均线
    int closeAvg;

    public BollCloseBuyPositionImpl(int sellAvg){
        this.closeAvg = sellAvg;
    }

    @Override
    public ClosePosition closeBuyPosition(List<StockMetaDO> histories, StockMetaDO today, XPosition nowPosition) {

        StockAnalyzeDO todaySAOSellAvg = StockAnalyzeDAOImpl.findByStockStuff(today.getId(),today.getStock(),StockMetaDO.CycleType.DAY.name(), getPeriod());

        if(todaySAOSellAvg == null){
            System.out.println("BollCloseBuyPositionImpl is null "+ today.getId()+" "+today.getStock()+" "+ getPeriod());
            return NotClose;
        }
        if(today.getClose() < todaySAOSellAvg.getLowMean() - 2 * todaySAOSellAvg.getLowSd()){
            TendencyUtil.WaveStatus waveStatus = TendencyUtil.waveHighLow(histories,today);
            if(RecallFrameWork.DEBUG) {
                System.out.println("\nClose ALL: " + waveStatus + "\n");
            }
            return new ClosePosition(true, nowPosition.getQuantity(),"ALL");
        }

        if(today.getClose() < todaySAOSellAvg.getLowMean() -  todaySAOSellAvg.getLowSd()){
            TendencyUtil.WaveStatus waveStatus = TendencyUtil.waveHighLow(histories,today);
            if(nowPosition.getLeftPositionBeforeClosedTimes() < 1 ) {
                if(RecallFrameWork.DEBUG) {
                    System.out.println("\nClose Half : " + waveStatus + "\n");
                }
                //之前没有卖过,卖且仅卖一次,只有达到2倍sd时才清仓剩下的.
                return new ClosePosition(true, nowPosition.getQuantity() / 2,"HALF");
            }
        }

        return NotClose;
    }

    @Override
    public int getPeriod() {
        return closeAvg;
    }

    @Override
    public String toString() {
        return "BollCloseBuyPositionImpl{" +
                "closeAvg=" + closeAvg +
                '}';
    }

    @Override
    public String keyDescribe(){
        return "c:"+closeAvg;
    }
}
