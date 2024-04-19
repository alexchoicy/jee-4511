package com.cems.web.tags;

import com.cems.Model.Location;
import com.cems.database.LocationManager;

import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.util.ArrayList;

public class LocationOptionsTag extends SimpleTagSupport {
    LocationManager locationManager = new LocationManager();

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
            ArrayList<Location> locations = locationManager.getLocations();
            StringBuilder options = new StringBuilder();

            if (hasValue) {
                options.append("\"<option value=\\\"\\\">Select Location</option>\" +");
                for (Location location : locations) {
                    options.append("\"<option value='").append(location.getId()).append("'\"");
                    options.append("+ (location === '").append(location.getId()).append("' ? 'selected' : '') + \"");
                    options.append(">").append(location.getName()).append("</option>\" +");
                }

                options.setLength(options.length() - 2);
            } else {            options.append("<option value=\"\">Select Location</option>");

                for (Location location : locations) {
                    options.append("<option value=\"").append(location.getId()).append("\">").append(location.getName()).append("</option>");
                }
            }
            getJspContext().getOut().print(options.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
