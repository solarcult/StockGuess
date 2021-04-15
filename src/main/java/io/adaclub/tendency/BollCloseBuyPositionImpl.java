package io.adaclub.tendency;

import io.adaclub.db.StockAnalyzeDO;
import io.adaclub.db.StockAnalyzeDAOImpl;
import io.adaclub.db.StockMetaDO;
import io.adaclub.framework.CloseBuyPosition;
import io.adaclub.framework.RecallFrameWork;
import io.adaclub.framework.XPosition;

import java.util.List;

public class BollCloseBuyPositionImpl implements CloseBuyPosition {
    @Override
    public ClosePosition closeBuyPosition(List<StockMetaDO> histories, StockMetaDO today, XPosition nowPosition) {

        StockAnalyzeDO todaySAO20 = StockAnalyzeDAOImpl.findByStockStuff(today.getId(),today.getStock(),StockMetaDO.CycleType.DAY.name(),getPeriod());

        if(today.getClose() < todaySAO20.getLowMean() - 2 * todaySAO20.getLowSd()){
            TendencyUtil.WaveStatus waveStatus = TendencyUtil.waveHighLow(histories,today);
            System.out.println("\nClose ALL: "+waveStatus+"\n");
            return new ClosePosition(true, nowPosition.getQuantity(),"ALL");
        }

        if(today.getClose() < todaySAO20.getLowMean() -  todaySAO20.getLowSd()){
            TendencyUtil.WaveStatus waveStatus = TendencyUtil.waveHighLow(histories,today);
            if(nowPosition.getLeftPositionBeforeClosedTimes() < 1 ) {
                System.out.println("\nClose Half : "+waveStatus+"\n");
                //之前没有卖过,卖且仅卖一次,只有达到2倍sd时才清仓剩下的.
                return new ClosePosition(true, nowPosition.getQuantity() / 2,"HALF");
            }
        }

        return NotClose;
    }

    @Override
    public int getPeriod() {
        return RecallFrameWork.Period_Days_20;
    }
}
