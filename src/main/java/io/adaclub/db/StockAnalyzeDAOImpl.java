package io.adaclub.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class StockAnalyzeDAOImpl {

    public static String INSERT_SQL = "insert into stock_analyze (cycle,stock_id,high_mean,high_sd,close_mean,close_sd,low_mean,low_sd,atr,change_volume_rate_total,effect,stock,xdays) value (?,?,?,?,?,?,?,?,?,?,?,?,?) ";
    public static String FIND_SQL = "select id,cycle,stock_id,high_mean,high_sd,close_mean,close_sd,low_mean,low_sd,atr,change_volume_rate_total,effect,stock,xdays from stock_analyze where stock_id = ? and stock = ? and cycle = ? and xdays = ?";
    public static String LIST_SQL = "select id,cycle,stock_id,high_mean,high_sd,close_mean,close_sd,low_mean,low_sd,atr,change_volume_rate_total,effect,stock,xdays from stock_analyze  where stock = ? and cycle = ? and xdays = ? and stock_id in ";
    public static String DEL_SQL = "delete from stock_analyze where stock = ?";

    public static void insert(StockAnalyzeDO stockAnaylzeDO){
        PreparedStatement d = null;
        try {
            d = StockDBManager.getStockConnection().prepareStatement(INSERT_SQL);
            d.setString(1, stockAnaylzeDO.getCycle());
            d.setLong(2, stockAnaylzeDO.getStockId());
            d.setDouble(3,stockAnaylzeDO.getHighMean());
            d.setDouble(4,stockAnaylzeDO.getHighSd());
            d.setDouble(5,stockAnaylzeDO.getCloseMean());
            d.setDouble(6,stockAnaylzeDO.getCloseSd());
            d.setDouble(7,stockAnaylzeDO.getLowMean());
            d.setDouble(8,stockAnaylzeDO.getLowSd());
            d.setDouble(9,stockAnaylzeDO.getAtr());
            d.setDouble(10,stockAnaylzeDO.getChangeVolumeRateTotal());
            d.setDouble(11,stockAnaylzeDO.getEffect());
            d.setString(12,stockAnaylzeDO.getStock());
            d.setInt(13,stockAnaylzeDO.getxDays());

            d.executeUpdate();
            d.close();
        }catch(Exception e) {
            e.printStackTrace();
        }finally {
            try {
                if(d!=null) d.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public static List<StockAnalyzeDO> list(Collection<Long> ids,String stock,String cycle,int xDays){

        List<StockAnalyzeDO> stockAnaylzeDOs = new ArrayList<>();

        StringBuilder sb = new StringBuilder(250);

        sb.append(LIST_SQL);
        sb.append("(");
        for(Long id : ids){
            sb.append(id);
            sb.append(",");
        }
        sb.deleteCharAt(sb.length()-1);
        sb.append(")");

        PreparedStatement d = null;
        try {
            d = StockDBManager.getStockConnection().prepareStatement(sb.toString());
            d.setString(1,stock);
            d.setString(2,cycle);
            d.setInt(3,xDays);
            ResultSet resultSet = d.executeQuery();
            while (resultSet.next()){
                StockAnalyzeDO stockAnaylzeDO = buildStockAnaylzeDO(resultSet);
                if(stockAnaylzeDO!=null){
                    stockAnaylzeDOs.add(stockAnaylzeDO);
                }
            }
            d.close();
        }catch(Exception e) {
            e.printStackTrace();
        }finally {
            try {
                if(d!=null) d.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return stockAnaylzeDOs;
    }

    public static StockAnalyzeDO findByStockStuff(Long stockId,String stock,String cycle,int xDays){

        PreparedStatement d = null;
        StockAnalyzeDO stockAnalyzeDO = null;
        try {
            d = StockDBManager.getStockConnection().prepareStatement(FIND_SQL);
            d.setLong(1, stockId);
            d.setString(2,stock);
            d.setString(3,cycle);
            d.setInt(4,xDays);

            ResultSet resultSet = d.executeQuery();
            if(resultSet.next()){
                stockAnalyzeDO = buildStockAnaylzeDO(resultSet);
            }
            d.close();
        }catch(Exception e) {
            e.printStackTrace();
        }finally {
            try {
                if(d!=null) d.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return stockAnalyzeDO;
    }

    private static StockAnalyzeDO buildStockAnaylzeDO(ResultSet resultSet){
        StockAnalyzeDO stockAnaylzeDO;

        try {
            stockAnaylzeDO = new StockAnalyzeDO();
            stockAnaylzeDO.setId(resultSet.getLong("id"));
            stockAnaylzeDO.setCycle(resultSet.getString("cycle"));
            stockAnaylzeDO.setStockId(resultSet.getLong("stock_id"));
            stockAnaylzeDO.setHighMean(resultSet.getDouble("high_mean"));
            stockAnaylzeDO.setHighSd(resultSet.getDouble("high_sd"));
            stockAnaylzeDO.setCloseMean(resultSet.getDouble("close_mean"));
            stockAnaylzeDO.setCloseSd(resultSet.getDouble("close_sd"));
            stockAnaylzeDO.setLowMean(resultSet.getDouble("low_mean"));
            stockAnaylzeDO.setLowSd(resultSet.getDouble("low_sd"));
            stockAnaylzeDO.setAtr(resultSet.getDouble("atr"));
            stockAnaylzeDO.setChangeVolumeRateTotal(resultSet.getDouble("change_volume_rate_total"));
            stockAnaylzeDO.setEffect(resultSet.getDouble("effect"));
            stockAnaylzeDO.setStock(resultSet.getString("stock"));
            stockAnaylzeDO.setxDays(resultSet.getInt("xdays"));

        }catch (Exception e){
            e.printStackTrace();
            return null;
        }

        return stockAnaylzeDO;
    }

    public static boolean deleteStockAllAnalyzeRecords(String stockCode){
        PreparedStatement d = null;
        try {
            d = StockDBManager.getStockConnection().prepareStatement(DEL_SQL);
            d.setString(1,stockCode);
            return d.execute();
        }catch(Exception e) {
            e.printStackTrace();
        }finally {
            try {
                if(d!=null) d.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        return false;
    }

    public static void main(String[] args){

        String stock = "AAPL";
        String cycle = "XXX";
        int xdays = 5;

//        StockAnalyzeDO stockAnaylzeDO = new StockAnalyzeDO();
//        stockAnaylzeDO.setAtr(1);
//        stockAnaylzeDO.setStockId(343);
//        stockAnaylzeDO.setCloseMean(34);
//        stockAnaylzeDO.setCloseSd(23);
//        stockAnaylzeDO.setHighMean(56);
//        stockAnaylzeDO.setHighSd(33);
//        stockAnaylzeDO.setLowMean(12);
//        stockAnaylzeDO.setLowSd(789);
//        stockAnaylzeDO.setChangeVolumeRateTotal(42342);
//        stockAnaylzeDO.setEffect(3242);
//        stockAnaylzeDO.setCycle(cycle);
//        stockAnaylzeDO.setStock(stock);
//        stockAnaylzeDO.setxDays(xdays);
//
//        insert(stockAnaylzeDO);

//        List<Long> ids = new ArrayList<>();
//        ids.add(343l);
//        ids.add(34l);
//        ids.add(1l);
//        List<StockAnalyzeDO> stockAnaylzeDOs = list(ids,stock,cycle,xdays);
//        for(StockAnalyzeDO s : stockAnaylzeDOs){
//            System.out.println(s);
//        }

//        StockAnalyzeDO stockAnaylzeDO = findByStockStuff(343l,stock,cycle,xdays);
//        System.out.println(stockAnaylzeDO);

        deleteStockAllAnalyzeRecords(stock);

    }
}
