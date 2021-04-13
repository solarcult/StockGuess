package io.adaclub.db;

import java.util.Objects;

public class StockAnalyzeDO {
    private long id;
    private String cycle;
    private long stockId;
    private double highMean;
    private double highSd;
    private double closeMean;
    private double closeSd;
    private double lowMean;
    private double lowSd;
    private double atr;
    private double changeVolumeRateTotal;
    private double effect;
    private String stock;
    private int xDays;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCycle() {
        return cycle;
    }

    public void setCycle(String cycle) {
        this.cycle = cycle;
    }

    public long getStockId() {
        return stockId;
    }

    public void setStockId(long stockId) {
        this.stockId = stockId;
    }

    public double getHighMean() {
        return highMean;
    }

    public void setHighMean(double highMean) {
        this.highMean = highMean;
    }

    public double getHighSd() {
        return highSd;
    }

    public void setHighSd(double highSd) {
        this.highSd = highSd;
    }

    public double getCloseMean() {
        return closeMean;
    }

    public void setCloseMean(double closeMean) {
        this.closeMean = closeMean;
    }

    public double getCloseSd() {
        return closeSd;
    }

    public void setCloseSd(double closeSd) {
        this.closeSd = closeSd;
    }

    public double getLowMean() {
        return lowMean;
    }

    public void setLowMean(double lowMean) {
        this.lowMean = lowMean;
    }

    public double getLowSd() {
        return lowSd;
    }

    public void setLowSd(double lowSd) {
        this.lowSd = lowSd;
    }

    public double getAtr() {
        return atr;
    }

    public void setAtr(double atr) {
        this.atr = atr;
    }

    public double getChangeVolumeRateTotal() {
        return changeVolumeRateTotal;
    }

    public void setChangeVolumeRateTotal(double changeVolumeRateTotal) {
        this.changeVolumeRateTotal = changeVolumeRateTotal;
    }

    public double getEffect() {
        return effect;
    }

    public void setEffect(double effect) {
        this.effect = effect;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public int getxDays() {
        return xDays;
    }

    public void setxDays(int xDays) {
        this.xDays = xDays;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StockAnalyzeDO that = (StockAnalyzeDO) o;
        return stockId == that.stockId && xDays == that.xDays && Objects.equals(cycle, that.cycle) && Objects.equals(stock, that.stock);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cycle, stockId, stock, xDays);
    }

    @Override
    public String toString() {
        return "StockAnalyzeDO{" +
                "id=" + id +
                ", cycle='" + cycle + '\'' +
                ", stockId=" + stockId +
                ", highMean=" + highMean +
                ", highSd=" + highSd +
                ", closeMean=" + closeMean +
                ", closeSd=" + closeSd +
                ", lowMean=" + lowMean +
                ", lowSd=" + lowSd +
                ", atr=" + atr +
                ", changeVolumeRateTotal=" + changeVolumeRateTotal +
                ", effect=" + effect +
                ", stock='" + stock + '\'' +
                ", xDays=" + xDays +
                '}';
    }
}
