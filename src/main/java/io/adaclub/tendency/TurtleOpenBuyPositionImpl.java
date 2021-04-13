package io.adaclub.tendency;

import io.adaclub.db.StockMetaDO;
import io.adaclub.framework.OpenBuyPosition;
import io.adaclub.framework.RecallFrameWork;

import java.util.List;

public class TurtleOpenBuyPositionImpl implements OpenBuyPosition {
    @Override
    public BuyPosition toBuyOrNotToBuy(List<StockMetaDO> history, StockMetaDO today) {
        TendencyUtil.WaveStatus waveStatus = TendencyUtil.waveHighLow(history,today);
        if(waveStatus.higherThanHigh == getPeriod()){
            System.out.println(getPeriod()+" : open");
            System.out.println(waveStatus);
            return new BuyPosition(true, RecallFrameWork.takeOneHand);
        }
        return new BuyPosition(false,0);
    }

    @Override
    public int getPeriod() {
        return RecallFrameWork.Period_Days_20;
    }
}
