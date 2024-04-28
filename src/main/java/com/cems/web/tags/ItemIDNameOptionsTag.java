package com.cems.web.tags;

import com.cems.Model.Display.ItemsOptions;
import com.cems.database.EquipmentManager;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.io.IOException;
import java.util.ArrayList;

public class ItemIDNameOptionsTag extends SimpleTagSupport{
    EquipmentManager equipmentManager = new EquipmentManager();

    @Override
    public void doTag() throws JspException, IOException {
       try{
           ArrayList<ItemsOptions> items = equipmentManager.getItemsOptions();

              StringBuilder options = new StringBuilder();
                options.append("<option value=\"\">Select Item</option>");
                for (ItemsOptions item : items) {
                    options.append("<option value=\"").append(item.getId()).append("\">").append(item.getName()).append("</option>");
                }
                getJspContext().getOut().print(options.toString());

       }catch (Exception e){
           e.printStackTrace();
       }
    }
}
