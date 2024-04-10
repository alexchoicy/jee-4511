package com.cems.web.tags;

import com.cems.Enums.ItemStatus;

import javax.servlet.jsp.tagext.SimpleTagSupport;

public class ItemStatusOptionsTag extends SimpleTagSupport {
    private boolean hasValue;

    public boolean isHasValue() {
        return hasValue;
    }

    public void setHasValue(boolean hasValue) {
        this.hasValue = hasValue;
    }

    @Override
    public void doTag() {
        try {
            StringBuilder options = new StringBuilder();
            if (hasValue) {
                options.append("\"<option value=\\\"\\\">Select Status</option>\" +");
                for (ItemStatus status : ItemStatus.values()) {
                    options.append("\"<option value='").append(status.getValue()).append("'\"");
                    options.append("+ (status === '").append(status.getValue()).append("' ? 'selected' : '') + \"");
                    options.append(">").append(status.getDisplayValue()).append("</option>\" +");
                }
                options.setLength(options.length() - 2);
            } else {
                options.append("<option value=\"\">Select Status</option>");
                for (ItemStatus status : ItemStatus.values()) {
                    options.append("<option value=\"").append(status.getValue()).append("\">").append(status.getDisplayValue()).append("</option>");
                }
            }
            getJspContext().getOut().print(options.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
