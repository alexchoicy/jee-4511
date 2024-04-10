package com.cems.Model.Display;

import com.cems.Model.Equipment;
import com.cems.Model.EquipmentItem;

import java.io.Serializable;
import java.util.ArrayList;

public class EquipmentDisplay implements Serializable {
    private Equipment equipment;
    private ArrayList<EquipmentItem> items;

    public EquipmentDisplay() {
    }
    public EquipmentDisplay(Equipment equipment, ArrayList<EquipmentItem> items) {
        this.equipment = equipment;
        this.items = items;
    }

    public Equipment getEquipment() {
        return equipment;
    }

    public void setEquipment(Equipment equipment) {
        this.equipment = equipment;
    }

    public ArrayList<EquipmentItem> getItems() {
        return items;
    }

    public void setItems(ArrayList<EquipmentItem> items) {
        this.items = items;
    }
}
