package io.adaclub.tendency;

import io.adaclub.db.StockMetaDO;
import io.adaclub.framework.OpenBuyPosition;
import io.adaclub.framework.RecallFrameWork;
import io.adaclub.framework.XPosition;

import java.util.List;

public class TurtleOpenBuyPositionImpl implements OpenBuyPosition {
    @Override
    public BuyPosition toBuyOrNotToBuy(List<StockMetaDO> histories, StockMetaDO today) {
        TendencyUtil.WaveStatus waveStatus = TendencyUtil.waveHighLow(histories,today);
        if(waveStatus.higherThanHigh == getPeriod()){
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
