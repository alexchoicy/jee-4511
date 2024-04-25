package com.cems.web.tags;

import com.cems.Model.Location;
import com.cems.Model.Reservations;
import com.cems.database.LocationManager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class ReservationListTag extends SimpleTagSupport {
    private ArrayList<Reservations> reservations;

    public ArrayList<Reservations> getReservations() {
        return reservations;
    }

    public void setReservations(ArrayList<Reservations> reservations) {
        this.reservations = reservations;
    }

    @Override
    public void doTag() {
        HttpServletRequest request = (HttpServletRequest) getJspContext().findAttribute("javax.servlet.jsp.jspRequest");
        String contextPath = request.getContextPath();
        try {
            StringBuilder options = new StringBuilder();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            for (Reservations reservation :reservations) {
                options.append("<tr>");
                options.append("<td>").append(reservation.getId()).append("</td>");
                options.append("<td>").append(sdf.format(reservation.getStartTime())).append("</td>");
                options.append("<td>").append(sdf.format(reservation.getEndTime())).append("</td>");
                options.append("<td>").append(sdf.format(reservation.getCreatedAt())).append("</td>");
                options.append("<td>").append("<a href='").append(contextPath).append("/reservations/records?recordID=").append(reservation.getId()).append("'>Detail</a>").append("</td>");
                options.append("</tr>");
            }
            getJspContext().getOut().print(options.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
