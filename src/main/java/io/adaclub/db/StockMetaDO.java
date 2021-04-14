package io.adaclub.db;

import java.util.Date;
import java.util.Objects;

public class StockMetaDO {

    private long id;
    private String stock;
    private String cycle;
    private double open;
    private double close;
    private double high;
    private double low;
    private Date date;
    private long volume;
    private double changeVolumeRate;
    private double pe;

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public String getCycle() {
        return cycle;
    }

    public void setCycle(String cycle) {
        this.cycle = cycle;
    }

    public double getOpen() {
        return open;
    }

    public void setOpen(double open) {
        this.open = open;
    }

    public double getClose() {
        return close;
    }

    public void setClose(double close) {
        this.close = close;
    }

    public double getHigh() {
        return high;
    }

    public void setHigh(double high) {
        this.high = high;
    }

    public double getLow() {
        return low;
    }

    public void setLow(double low) {
        this.low = low;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public long getVolume() {
        return volume;
    }

    public void setVolume(long volume) {
        this.volume = volume;
    }

    public double getChangeVolumeRate() {
        return changeVolumeRate;
    }

    public void setChangeVolumeRate(double changeVolumeRate) {
        this.changeVolumeRate = changeVolumeRate;
    }

    public double getPe() {
        return pe;
    }

    public void setPe(double pe) {
        this.pe = pe;
    }

    double miu = 0.005;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StockMetaDO that = (StockMetaDO) o;
        return Objects.equals(stock, that.stock) && Objects.equals(cycle, that.cycle) && Objects.equals(date, that.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(stock, cycle, date);
    }

    @Override
    public String toString() {
        return "StockMetaDO{" +
                "id=" + id +
                ", stock='" + stock + '\'' +
                ", type='" + cycle + '\'' +
                ", open=" + open +
                ", close=" + close +
                ", high=" + high +
                ", low=" + low +
                ", date=" + date +
                ", volume=" + volume +
                ", changeVolumeRate=" + changeVolumeRate +
                ", pe=" + pe +
                '}';
    }

    public static void main(String[] args){
        for(CycleType t : CycleType.values()){
            System.out.println(t.name());
        }
    }

    public enum CycleType{
        DAY,WEEK,MONTH
    }
}


