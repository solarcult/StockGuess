package io.adaclub.tendency;

import io.adaclub.db.StockAnalyzeDO;
import io.adaclub.db.StockAnaylzeDAOImpl;
import io.adaclub.db.StockMetaDO;
import io.adaclub.framework.OpenBuyPosition;
import io.adaclub.framework.RecallFrameWork;

import java.util.List;
import java.util.stream.Collectors;

public class BollOpenBuyPositionImpl implements OpenBuyPosition {

    @Override
    public BuyPosition toBuyOrNotToBuy(List<StockMetaDO> historys, StockMetaDO today) {

        List<Long> ids = historys.stream().map(StockMetaDO::getId).collect(Collectors.toList());
        List<StockAnalyzeDO> stockAnalyzeDOs20 =  StockAnaylzeDAOImpl.list(ids,today.getStock(),StockMetaDO.CycleType.DAY.name(),getPeriod());
        if(stockAnalyzeDOs20.size() != historys.size()){
            //如果相关数据没有准备好,则返回不交易,等待数据准备完毕.
            return NotBuy;
        }
        StockAnalyzeDO todaySAO20 = StockAnaylzeDAOImpl.findByStockStuff(today.getId(),today.getStock(),StockMetaDO.CycleType.DAY.name(),getPeriod());
//        for(StockAnalyzeDO s : stockAnalyzeDOs){
//            System.out.println(s);
//        }
        List<StockAnalyzeDO> stockAnalyzeDOs5 =  StockAnaylzeDAOImpl.list(ids,today.getStock(),StockMetaDO.CycleType.DAY.name(), RecallFrameWork.Period_Days_5);
        StockAnalyzeDO todaySAO5 = StockAnaylzeDAOImpl.findByStockStuff(today.getId(),today.getStock(),StockMetaDO.CycleType.DAY.name(), RecallFrameWork.Period_Days_5);

        //计算20日均线状态
        TendencyUtil.WaveStatus waveStatus20 = TendencyUtil.waveAverageClose(stockAnalyzeDOs20,todaySAO20);
        //20日均线有80%以上都在高位运行
        if ((double) waveStatus20.higherThanHigh / waveStatus20.totalSize() >= 0.8){
            if(
                    todaySAO5.getLowMean() > today.getClose()
                    && today.getClose() > (todaySAO5.getLowMean() - 2 * todaySAO5.getLowSd())
//                    today.getClose() <= (todaySAO5.getLowMean() -  todaySAO5.getLowSd())
                ) {
                System.out.println("20:");
                System.out.println(waveStatus20);
                System.out.println("5:");
                System.out.println(TendencyUtil.waveAverageClose(stockAnalyzeDOs5,todaySAO5));
                System.out.println(todaySAO5.getLowMean() + " > "+ today.getClose() + " > " +  (todaySAO5.getLowMean() - 2 * todaySAO5.getLowSd()));
                return new BuyPosition(true, RecallFrameWork.takeOneHand);
            }
        }

        return NotBuy;
    }

    @Override
    public int getPeriod() {
        return RecallFrameWork.Period_Days_20;
    }
}
