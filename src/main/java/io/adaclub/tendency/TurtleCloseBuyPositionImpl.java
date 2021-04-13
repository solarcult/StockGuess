package io.adaclub.tendency;

import io.adaclub.db.StockMetaDO;
import io.adaclub.framework.CloseBuyPosition;
import io.adaclub.framework.RecallFrameWork;

import java.util.List;

public class TurtleCloseBuyPositionImpl implements CloseBuyPosition {
    @Override
    public ClosePosition closeBuyPosition(List<StockMetaDO> history, StockMetaDO today) {
        TendencyUtil.WaveStatus waveStatus = TendencyUtil.waveHighLow(history,today);
        if(waveStatus.lowerThanLow == getPeriod()){
            System.out.println(getPeriod()+" : close");
            System.out.println(waveStatus);
            return new ClosePosition(true, RecallFrameWork.takeOneHand);
        }
        return NotClose;
    }

    @Override
    public int getPeriod() {
        return RecallFrameWork.Period_Days_10;
    }
}
