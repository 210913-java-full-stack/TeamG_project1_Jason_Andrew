package servlets;

import models.Flights;
import models.People;
import services.FlightService;
import services.PeopleService;
import utils.JSONSplitter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Scanner;

public class ServletFlights extends HttpServlet {
    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        boolean dbg =true;

        System.out.println("DEBUG- ServletFlights REACHED");//take this out after debug finished

        //These lines read the request body and put it into a string called jsonText
        InputStream requestBody = req.getInputStream();
        Scanner sc = new Scanner(requestBody, StandardCharsets.UTF_8.name());
        String jsonText = sc.useDelimiter("\\A").next();
        //This will be a set of key-value pairs, like "numberOfTickets": 3, "userFlightID":4, "userCancelTickeID": 4

        System.out.println("DEBUG FlightID- JSON Text: " + jsonText);//take this out after debugging

        //get the action from the request header
        String action = req.getHeader("Servlet-action");
        System.out.println("DEBUG- action: "+action);

        switch(action){
            case "AdminScheduleFlight":

                //jsonText comes from adminPortalAddFlights.js
                String[] asflight = JSONSplitter.jsonSplitter(jsonText);

                Flights newflight = new Flights();

                String asflightDepart = asflight[2].substring(1,asflight[2].length()-1);
                String asflightArrive = asflight[4].substring(1,asflight[4].length()-1);

                newflight.setDeparture_city(asflightDepart);//asflight[2] = departure_city.value
                newflight.setArrival_city(asflightArrive);//asflight[4] = arrival_city.value
                newflight.setLocked_For_Takeoff(false);

                FlightService.saveNewFlight(newflight);

                resp.setStatus(200);
                break;
            case "AdminCancelFlight":

                System.out.println("DEBUG: AdminCancelFlight reached");
                String[] acflight = JSONSplitter.jsonSplitter(jsonText);

                String acflight2 = acflight[2].substring(1,acflight[2].length()-1);
                Flights acf = FlightService.getFlightById(Integer.parseInt(acflight2));
                System.out.println("DEBUG: flight_id parsed");

                FlightService.deleteFlight(acf);

                resp.setStatus(200);
                break;

            case "PilotTakeoffLock":
                System.out.println("DEBUG: PilotTakeoffLock case reached");
                String[] ptlock = JSONSplitter.jsonSplitter(jsonText);

                //Need to keep this!!!!!!!
                String ptlock2 = ptlock[2].substring(1,ptlock[2].length()-1);

                Integer flight_id = Integer.valueOf(ptlock2);
                System.out.println("DEBUG: FlightID parsed");

                Flights ptl = FlightService.getFlightById(flight_id);
                if(dbg){
                    System.out.println("DEBUG: getFlightById success");}
                FlightService.PilotTakeoffLock(ptl);

                resp.getWriter().println("Flight id: "+ptlock[1]+" is locked and ready for takeoff");
                resp.setStatus(200);
                break;
        }

    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        // TODO 2 ACTIONS... AdminFlightManifest, UserViewFlights... no body
        String sa = req.getHeader("Servlet-action");
        switch(sa){
            case "AdminFlightManifest":
                // need flight_id
                Flights flight =FlightService.getFlightById(Integer.parseInt(req.getHeader("flightID").substring(1, req.getHeader("flightID").length()-1)));
                List<People> passengerManifest = PeopleService.getPassengersByFlight(flight.getFlight_id()); // might need to change this method... using flights Object

                System.out.println("DEBUG: Manifest Created");
                String manifestJSON = "{";
                for (People passenger:passengerManifest) {
                    int pid =passenger.getPeople_id();
                    String username = passenger.getUsername();
                    manifestJSON=manifestJSON+"ID: "+pid+"  Username: "+username+",\n";
                }
                manifestJSON= manifestJSON+"}";
                // TODO send manifestJSON using JavaScript to AdminViewManifest
                // TODO: google how to send JSON back to html page
                System.out.println("DEBUG: Manifest JSON= "+manifestJSON);
                resp.getWriter().write(manifestJSON);
                resp.setStatus(200);

                break;
            case "UserViewFlights":
                // need departure_city and arrival_city
                List<Flights> flightSchedule = FlightService.getFlightsByArrivalDestination(req.getHeader("selectDepartureCity"), req.getHeader("selectArrivalCity"));
                String flightScheduleJSON = "{";
                for (Flights viewflight:flightSchedule) {
                    resp.getWriter().println("Flight ID: "+viewflight.getFlight_id()+" from: "+
                            viewflight.getDeparture_city()+" to: "+viewflight.getArrival_city());
                }
                resp.setStatus(200);
                //TODO: send flightScheduleJSON using JS to

                break;
        }
    }

}