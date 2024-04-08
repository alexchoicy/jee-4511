package com.cems.web.tags;

import com.cems.Model.Location;
import com.cems.database.LocationManager;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import java.util.ArrayList;

public class LocationOptionsTag extends SimpleTagSupport {
    LocationManager locationManager = new LocationManager();
    @Override
    public void doTag() {
        try {
            ArrayList<Location> locations = locationManager.getLocations();
            StringBuilder options = new StringBuilder();
            options.append("<option value=\"\">Select Status</option>");
            for(Location location : locations) {
                options.append("<option value=\"").append(location.getId()).append("\">").append(location.getName()).append("</option>");
            }
            getJspContext().getOut().print(options.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
