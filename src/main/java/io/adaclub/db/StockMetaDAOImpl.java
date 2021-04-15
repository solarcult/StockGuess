package io.adaclub.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class StockMetaDAOImpl {

    public static String INSERT_SQL = "insert into stock_meta (stock,cycle,open,close,high,low,date,volume,change_volume_rate,pe) value (?,?,?,?,?,?,?,?,?,?) ";
    public static String FIND_SQL = "select id,stock,cycle,open,close,high,low,date,volume,change_volume_rate,pe from stock_meta where stock = ? and cycle = ? and date = ?";
    public static String LIST_SQL = "select id,stock,cycle,open,close,high,low,date,volume,change_volume_rate,pe from stock_meta where stock = ? and cycle = ? order by date desc limit ?";
    public static String DEL_SQL = "delete from stock_meta where stock = ?";

    public static void insert(StockMetaDO stockMetaDO){
        PreparedStatement d = null;
        try {
            d = StockDBManager.getStockConnection().prepareStatement(INSERT_SQL);
            d.setString(1, stockMetaDO.getStock());
            d.setString(2, stockMetaDO.getCycle());
            d.setDouble(3,stockMetaDO.getOpen());
            d.setDouble(4,stockMetaDO.getClose());
            d.setDouble(5,stockMetaDO.getHigh());
            d.setDouble(6,stockMetaDO.getLow());
            d.setDate(7,new java.sql.Date(stockMetaDO.getDate().getTime()));
            d.setLong(8,stockMetaDO.getVolume());
            d.setDouble(9,stockMetaDO.getChangeVolumeRate());
            d.setDouble(10,stockMetaDO.getPe());
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

    public static StockMetaDO find(String stockCode, StockMetaDO.CycleType type, Date date){
        PreparedStatement d = null;
        StockMetaDO stockMetaDO = null;
        try {
            d = StockDBManager.getStockConnection().prepareStatement(FIND_SQL);
            d.setString(1, stockCode);
            d.setString(2, type.name());
            d.setDate(3,new java.sql.Date(date.getTime()));

            ResultSet resultSet = d.executeQuery();
            if(resultSet.next()){
                stockMetaDO = buildStockMetaDO(resultSet);
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
        return stockMetaDO;
    }

    public static List<StockMetaDO> list(String stockCode , String type, int range){

        List<StockMetaDO> stockMetaDOs = new ArrayList<>(range);

        PreparedStatement d = null;
        try {
            d = StockDBManager.getStockConnection().prepareStatement(LIST_SQL);
            d.setString(1, stockCode);
            d.setString(2, type);
            d.setInt(3,range);

            ResultSet resultSet = d.executeQuery();
            while(resultSet.next()){
                StockMetaDO stockMetaDO = buildStockMetaDO(resultSet);
                if(stockMetaDO!=null) {
                    stockMetaDOs.add(stockMetaDO);
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
        return stockMetaDOs;
    }

    static StockMetaDO buildStockMetaDO(ResultSet resultSet){
        StockMetaDO stockMetaDO;
        try {
            stockMetaDO = new StockMetaDO();
            stockMetaDO.setId(resultSet.getLong("id"));
            stockMetaDO.setStock(resultSet.getString("stock"));
            stockMetaDO.setCycle(resultSet.getString("cycle"));
            stockMetaDO.setOpen(resultSet.getDouble("open"));
            stockMetaDO.setClose(resultSet.getDouble("close"));
            stockMetaDO.setHigh(resultSet.getDouble("high"));
            stockMetaDO.setLow(resultSet.getDouble("low"));
            stockMetaDO.setDate(resultSet.getDate("date"));
            stockMetaDO.setVolume(resultSet.getLong("volume"));
            stockMetaDO.setChangeVolumeRate(resultSet.getDouble("change_volume_rate"));
            stockMetaDO.setPe(resultSet.getDouble("pe"));
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }

        return stockMetaDO;
    }

    public static boolean deleteStockAllMetaRecords(String stockCode){
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
//        StockMetaDO stockMetaDO = new StockMetaDO();
//        stockMetaDO.setStock("ABC");
//        stockMetaDO.setType(StockMetaDO.TYPE_DAY);
//        stockMetaDO.setDate(new java.util.Date());
//        stockMetaDO.setOpen(1.1);
//        stockMetaDO.setClose(2.2);
//        stockMetaDO.setHigh(3.3);
//        stockMetaDO.setLow(4.4);
//        stockMetaDO.setVolume(55);
//        stockMetaDO.setChangeVolumeRate(6.789);
//
//        insert(stockMetaDO);

//        List<StockMetaDO> stockMetaDOs = list("AAPL",StockMetaDO.CycleType.DAY.name(), 10);
//        for(StockMetaDO s : stockMetaDOs){
//            System.out.println(s);
//        }

//        StockMetaDO s = null;
//        try {
//            s = find("AAPL", StockMetaDO.CycleType.DAY, TendencyQuery.formatter.parse("2021-03-22"));
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        System.out.println(s);


        deleteStockAllMetaRecords("AAPL");
    }

}
