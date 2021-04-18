package io.adaclub.tendency;

import io.adaclub.db.StockMetaDO;
import io.adaclub.framework.CloseBuyPosition;
import io.adaclub.framework.RecallFrameWork;
import io.adaclub.framework.XPosition;

import java.util.List;

public class TurtleCloseBuyPositionImpl implements CloseBuyPosition {
    @Override
    public ClosePosition closeBuyPosition(List<StockMetaDO> histories, StockMetaDO today, XPosition nowPosition) {
        TendencyUtil.WaveStatus waveStatus = TendencyUtil.waveHighLow(histories,today);
        if(waveStatus.lowerThanLow == getPeriod()){
//            System.out.println(waveStatus);
            return new ClosePosition(true, nowPosition.getQuantity(),"ALL");
        }
        return NotClose;
    }

    @Override
    public int getPeriod() {
        return RecallFrameWork.Period_Days_10;
    }

    @Override
    public String keyDescribe() {
        return "c:"+getPeriod();
    }
}
