package com.cems.web.tags;

import com.cems.Enums.ItemStatus;
import javax.servlet.jsp.tagext.SimpleTagSupport;
public class ItemStatusOptionsTag extends SimpleTagSupport {
    @Override
    public void doTag() {
        try {
            StringBuilder options = new StringBuilder();
            options.append("<option value=\"\">Select Status</option>");
            for(ItemStatus status : ItemStatus.values()) {
                options.append("<option value=\"").append(status.getValue()).append("\">").append(status.getDisplayValue()).append("</option>");
            }
            getJspContext().getOut().print(options.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
