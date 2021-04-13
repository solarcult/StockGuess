package io.adaclub.tendency;

import io.adaclub.db.StockMetaDO;
import io.adaclub.framework.CloseBuyPosition;
import io.adaclub.framework.RecallFrameWork;

import java.util.List;

public class BollCloseBuyPositionImpl implements CloseBuyPosition {
    @Override
    public ClosePosition closeBuyPosition(List<StockMetaDO> history, StockMetaDO today) {
        return null;
    }

    @Override
    public int getPeriod() {
        return RecallFrameWork.Period_Days_20;
    }
}
