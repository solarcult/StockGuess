package io.adaclub.tendency;

import io.adaclub.db.StockAnalyzeDO;
import io.adaclub.db.StockAnalyzeDAOImpl;
import io.adaclub.db.StockMetaDO;
import io.adaclub.framework.OpenBuyPosition;
import io.adaclub.framework.RecallFrameWork;
import io.adaclub.framework.Wallet;

import java.util.List;
import java.util.stream.Collectors;

public class BollOpenBuyPositionImpl implements OpenBuyPosition {

    double tendencyIsUp = 0.7;
    //短时间均线
    int shortAvg;
    //长时间均线
    int longAvg;

    public BollOpenBuyPositionImpl(int shortAvg ,int longAvg){
        this.shortAvg = shortAvg;
        this.longAvg = longAvg;
    }

    @Override
    public BuyPosition toBuyOrNotToBuy(List<StockMetaDO> histories, StockMetaDO today) {

        List<Long> ids = histories.stream().map(StockMetaDO::getId).collect(Collectors.toList());
        List<StockAnalyzeDO> stockAnalyzeDOs20 =  StockAnalyzeDAOImpl.list(ids,today.getStock(),StockMetaDO.CycleType.DAY.name(),getPeriod());
        if(stockAnalyzeDOs20.size() != histories.size()){
            //如果相关数据没有准备好,则返回不交易,等待数据准备完毕.
            return NotBuy;
        }
        StockAnalyzeDO todaySAO20 = StockAnalyzeDAOImpl.findByStockStuff(today.getId(),today.getStock(),StockMetaDO.CycleType.DAY.name(),longAvg);
        if(todaySAO20==null){
            System.out.println("BollOpenBuyPositionImpl longAvg is null "+today.getId() +" "+today.getStock() +" "+longAvg);
        }
        StockAnalyzeDO todaySAO5 = StockAnalyzeDAOImpl.findByStockStuff(today.getId(),today.getStock(),StockMetaDO.CycleType.DAY.name(), shortAvg);
        if(todaySAO5==null){
            System.out.println("BollOpenBuyPositionImpl shortAvg is null "+today.getId() +" "+today.getStock() +" "+shortAvg);
        }

        //计算20日均线状态
        TendencyUtil.WaveStatus waveStatus20 = TendencyUtil.waveAverageClose(stockAnalyzeDOs20,todaySAO20);
        //20日均线有?0%以上都在高位运行
        if ((double) waveStatus20.higherThanHigh / waveStatus20.totalSize() >= tendencyIsUp){
            if(
//                    todaySAO5.getLowMean() > today.getClose()
//                    && today.getClose() > (todaySAO5.getLowMean() - 2 * todaySAO5.getLowSd())

//                    todaySAO5.getCloseMean() > todaySAO20.getCloseMean()

//                    today.getClose() <= (todaySAO5.getLowMean() -  1.5 * todaySAO5.getLowSd())

                    today.getClose() <= (todaySAO5.getLowMean() -  todaySAO5.getLowSd())

                ) {
                if(RecallFrameWork.DEBUG) {
                    System.out.println("\nbuy 20 : " + waveStatus20);
                    List<StockAnalyzeDO> stockAnalyzeDOs5 =  StockAnalyzeDAOImpl.list(ids,today.getStock(),StockMetaDO.CycleType.DAY.name(), RecallFrameWork.Period_Days_5);
                    System.out.println("buy 05 : " + TendencyUtil.waveAverageClose(stockAnalyzeDOs5, todaySAO5));
                    System.out.println("buy 5low:" + todaySAO5.getLowMean() + " >5lm-sd " + (todaySAO5.getLowMean() - todaySAO5.getLowSd()) + " >meclose: " + today.getClose() + " >20lm-sd: " + (todaySAO20.getLowMean() - todaySAO20.getLowSd()) + " >20lm-2sd: " + (todaySAO20.getLowMean() - 2 * todaySAO20.getLowSd()) + "\n");
                }
                return new BuyPosition(true, Wallet.takeOneHand);
            }
        }

        return NotBuy;
    }

    @Override
    public int getPeriod() {
        return RecallFrameWork.Period_Days_20;
    }

    @Override
    public String toString() {
        return "BollOpenBuyPositionImpl{" +
                "tendencyIsUp=" + tendencyIsUp +
                ", shortAvg=" + shortAvg +
                ", longAvg=" + longAvg +
                '}';
    }

    @Override
    public String keyDescribe(){
        return "o:" + shortAvg +"," + longAvg+";";
    }
}
