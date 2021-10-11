package servlets;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ServletManager extends HttpServlet {
    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String string_ticket_id = req.getParameter("ticket_id");
        int ticket_id = Integer.parseInt(string_ticket_id);
        resp.getWriter().print("Ticket #: "+ticket_id + " has been cancelled");
        resp.setStatus(200);
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp){

    }

}
