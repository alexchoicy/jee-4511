package com.cems.Model.Display;

import com.cems.Enums.DamagedStatus;

import java.io.Serializable;
import java.sql.Timestamp;

public class DamagesReportDisplay implements Serializable {
    private String LoggerID;
    private String LoggerName;
    private int RecordID;
    private int BorrowerID;
    private String BorrowerName;
    private int ItemID;
    private String ItemName;
    private Timestamp LogDatetime;
    private String DamageDetail;
    private DamagedStatus DamagedStatus;

    public com.cems.Enums.DamagedStatus getDamagedStatus() {
        return DamagedStatus;
    }

    public void setDamagedStatus(com.cems.Enums.DamagedStatus damagedStatus) {
        DamagedStatus = damagedStatus;
    }

    public String getLoggerID() {
        return LoggerID;
    }

    public void setLoggerID(String loggerID) {
        LoggerID = loggerID;
    }

    public String getLoggerName() {
        return LoggerName;
    }

    public void setLoggerName(String loggerName) {
        LoggerName = loggerName;
    }

    public int getRecordID() {
        return RecordID;
    }

    public void setRecordID(int recordID) {
        RecordID = recordID;
    }

    public int getBorrowerID() {
        return BorrowerID;
    }

    public void setBorrowerID(int borrowerID) {
        BorrowerID = borrowerID;
    }

    public String getBorrowerName() {
        return BorrowerName;
    }

    public void setBorrowerName(String borrowerName) {
        BorrowerName = borrowerName;
    }

    public int getItemID() {
        return ItemID;
    }

    public void setItemID(int itemID) {
        ItemID = itemID;
    }

    public String getItemName() {
        return ItemName;
    }

    public void setItemName(String itemName) {
        ItemName = itemName;
    }

    public Timestamp getLogDatetime() {
        return LogDatetime;
    }

    public void setLogDatetime(Timestamp logDatetime) {
        LogDatetime = logDatetime;
    }

    public String getDamageDetail() {
        return DamageDetail;
    }

    public void setDamageDetail(String damageDetail) {
        DamageDetail = damageDetail;
    }
}
