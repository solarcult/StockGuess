package io.adaclub.framework.test;

import io.adaclub.FileUtil;
import io.adaclub.XYMChart;
import io.adaclub.db.StockMetaDAOImpl;
import io.adaclub.db.StockMetaDO;
import io.adaclub.framework.*;
import io.adaclub.tendency.BollCloseBuyPositionImpl;
import io.adaclub.tendency.BollOpenBuyPositionImpl;
import org.jfree.chart.annotations.XYTextAnnotation;
import org.jfree.data.xy.DefaultXYDataset;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class BollTest {

    public static void main(String[] args){
        RecallFrameWork.chartType = "Boll";
        String stockCode = "MSFT";
        List<StockMetaDO> stockMetaDOs = StockMetaDAOImpl.list(stockCode,StockMetaDO.CycleType.DAY.name(), 1000);
        StockMetaDO today = stockMetaDOs.get(0);
        //将0位置变为时间最远的数据,方便编码理解,0代表过去,size()的位置代表现在
        Collections.reverse(stockMetaDOs);

//        BestOne bestOne = tryOnce(stockMetaDOs,today,3,9,9,true);
//        System.out.println(bestOne.getMaxRetracement()+" vs "+bestOne.getEarnMoney());

        findBestOne(stockMetaDOs, today);
    }

    public static int MultiRetracementValue = 100;

    private static void findBestOne(List<StockMetaDO> stockMetaDOs, StockMetaDO today) {
        long astart = System.currentTimeMillis();
        StringBuilder w2f = new StringBuilder();
        w2f.append(Calendar.getInstance().getTime()).append("\n");
        w2f.append(new Wallet()).append("\n");
        List<RecallResult> recallResults = new ArrayList<>();
        List<CompletableFuture<Void>> lotOfCpuS = new ArrayList<>();
        int step = 15;
        String stockName = stockMetaDOs.get(0).getStock();
        AtomicInteger count = new AtomicInteger();
        AtomicLong totalSpendTime = new AtomicLong();
        int totalt = 0;
        for(int i=RecallFrameWork.MIN_PERIOD_DAYS; i <= RecallFrameWork.MAX_PERIOD_DAYS;i=i+step)
            for (int j = RecallFrameWork.MIN_PERIOD_DAYS; j <= RecallFrameWork.MAX_PERIOD_DAYS; j=j+step)
                for (int k = RecallFrameWork.MIN_PERIOD_DAYS; k <= RecallFrameWork.MAX_PERIOD_DAYS; k=k+step)
                    totalt++;

        int total = totalt;
        for(int i=RecallFrameWork.MIN_PERIOD_DAYS; i <= RecallFrameWork.MAX_PERIOD_DAYS;i=i+step){
            for (int j = RecallFrameWork.MIN_PERIOD_DAYS; j <= RecallFrameWork.MAX_PERIOD_DAYS; j=j+step){
                for (int k = RecallFrameWork.MIN_PERIOD_DAYS; k <= RecallFrameWork.MAX_PERIOD_DAYS; k=k+step){
                    int finalI = i;
                    int finalJ = j;
                    int finalK = k;
                    CompletableFuture<Void> completableFuture = CompletableFuture.runAsync(()->{
                        long start = System.currentTimeMillis();
                        RecallResult result = tryOnce(stockMetaDOs, today,new Wallet(),finalI,finalJ,finalK,false);
                        recallResults.add(result);
                        int fc = count.incrementAndGet();
                        long end = System.currentTimeMillis();
                        long totalSpt = totalSpendTime.addAndGet(end-start)/1000;
                        long avg = totalSpt/fc;
                        System.out.println(Calendar.getInstance().getTime() +" : "+finalI + ":" +finalJ+":"+finalK + " done. -> "+fc+"/"+total +" \tleft Min : " + ((total-fc)*avg)/60 + " , \tspend Seconds: "+ totalSpt+" \t avg: " + avg + " | "+ result.getProfit() +" <e-r> "+ result.getMaxRetracement()*MultiRetracementValue +" roi: "+result.getRoi() * MultiRetracementValue);
                    });
                    lotOfCpuS.add(completableFuture);
                }
            }
        }
        System.out.println(Calendar.getInstance().getTime()+" : initial "+total+" done. Good Luck boy. :)");

        CompletableFuture<Void> all = CompletableFuture.allOf(lotOfCpuS.toArray(new CompletableFuture[0]));
        all.join();

        List<Pareto> paretos = new ArrayList<>();
        for(RecallResult result : recallResults){
            paretos.add(new Pareto((int) Math.ceil(result.getMaxRetracement()* MultiRetracementValue),(int)result.getProfit(),result));
        }
        Map<Integer,List<Pareto>> seekParetoFrontMap = new HashMap<>();
        List<Pareto> seekParetoFronts = new ArrayList<>();
        //进行帕累托前沿分析,并画图
        for(Pareto pareto : paretos){
            List<Pareto> retracements = seekParetoFrontMap.getOrDefault(pareto.getRetracement(),new ArrayList<>());
            retracements.add(pareto);
            seekParetoFrontMap.put(pareto.getRetracement(),retracements);
        }
        for(List<Pareto> eachParetos : seekParetoFrontMap.values()){
            Collections.sort(eachParetos);
            seekParetoFronts.add(eachParetos.get(0));
            System.out.println();
            System.out.println(eachParetos.get(0));
            System.out.println(eachParetos.get(eachParetos.size()-1));
        }
        List<XYTextAnnotation> xyTextAnnotations = new ArrayList<>();
        double[][] data=new double[2][seekParetoFronts.size()];
        //出来的都是前沿,准备画图数据
        for(int i = 0;i< seekParetoFronts.size(); i++) {
            Pareto pareto = seekParetoFronts.get(i);
            data[0][i] = pareto.getRetracement();
            data[1][i] = pareto.getEarn();
            XYTextAnnotation xyTextAnnotation = new XYTextAnnotation(pareto.getRecallResult().getOpenKeyDescribe()+pareto.getRecallResult().getCloseKeyDescribe(),pareto.getRetracement(),pareto.getEarn());
            xyTextAnnotations.add(xyTextAnnotation);
        }

        DefaultXYDataset dataset = new DefaultXYDataset ();
        dataset.addSeries("worksWell",data);
        new XYMChart(stockName,dataset,xyTextAnnotations);

        Collections.sort(seekParetoFronts);

        for (Pareto pareto : seekParetoFronts){
            w2f.append(pareto);
            w2f.append("\n");
//            System.out.println(pareto);
        }
        long aend = System.currentTimeMillis();
        w2f.append(Calendar.getInstance().getTime()).append(" : All done , Sir ! Spent life : ").append((aend - astart) / 1000).append(" seconds. We got: ").append(seekParetoFronts.size());
        System.out.println(Calendar.getInstance().getTime()+" : All done , Sir ! Spent life : "+(aend-astart)/1000 +" seconds. We got: "+seekParetoFronts.size());
        FileUtil.write2disk(stockName,w2f.toString());
    }

    public static RecallResult tryOnce(List<StockMetaDO> stockMetaDOs,StockMetaDO today,Wallet wallet, int openShortAvg, int openLongAvg, int closeLongAvg,boolean isPrintChart){

        OpenBuyPosition openBuyPosition = new BollOpenBuyPositionImpl(openShortAvg, openLongAvg);
        CloseBuyPosition closeBuyPosition = new BollCloseBuyPositionImpl(closeLongAvg);
        RecallResult result = RecallFrameWork.goThrough(stockMetaDOs, openBuyPosition, closeBuyPosition,wallet,isPrintChart);
        List<XPosition> positions = result.getPositions();
        if (RecallFrameWork.DEBUG) {
            System.out.println("\nGo through: ");
            for (XPosition xPosition : positions) {
                System.out.println(xPosition);
            }
            System.out.println("\nTotalSize : " + positions.size());
            System.out.println("Profit : " + result.getProfit());
        }

        return result;
    }
}
