/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package EntityManager;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.PathParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import org.json.JSONArray;
import org.json.JSONObject;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;
import java.text.*;
import EntityDB.*;
/**
 * REST Web Service
 *
 *
 * @author ramyabalaji
 */

@Path("/event/")
public class EventResource {
    @Context
    private UriInfo context;

    /** Creates a new instance of EventResource */
    public EventResource() {
    }

    /**
     * Retrieves representation of an instance of EntityManager.EventResource
     * @return an instance of java.lang.String
     */
    @Path ("{id}")
    @GET
    @Produces("application/json")
    public String getEvent(@PathParam ("id") String id)
    {
        try {
          JSONObject json = new JSONObject();
          System.out.println("ID is "+id);
          //Note: make sure this works.  Right now it is untested.
          Event event = (Event)EntityBase.selectByID(id);
          if(event == null)
          {
              return "Error: Event doesn't exist.";
          }
          json.put("EntityId",id);
          json.put("StartDate",event.getStartDate());
          json.put("EndDate",event.getEndDate());
          //TODO: Create a parsible message for the application devs.
          return (json.toString());
        } catch (Exception E){
           return (E.getMessage());
        }
     }


    /**
     * PUT method for updating or creating an instance of EventResource
     * @param content representation for the resource
     * @return an HTTP response with content of the updated or created resource.
     */
    @Path ("/")
    @PUT
    @Consumes("application/json")
    public String createEvent(String content)
    {
        try
        {
           JSONObject json=new JSONObject(content);
           Event event=new Event();
           event.createNewID();
           //DateFormat df=DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM);
          SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
           if (!json.isNull("StartDate"))
               event.setStartDate(df.parse(json.getString("StartDate")));
           if (!json.isNull("EndDate"))
               event.setEndDate(df.parse(json.getString("EndDate")));
           event.save();
        }
        catch (Exception e)
        {
            return (e.toString());
        }
         return "successfully created event";
    }
    @Path("{id}")
    @POST
    @Consumes("application/json")
    public String updateEvent(String json)
    {
        try
        {
            JSONObject content=new JSONObject(json);
            String id=content.getString("Id");
            Event event=(Event)EntityBase.selectByID(id);
        
            if (event==null)
                return("That event does not exisit");

           
           SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
            if (!content.isNull("StartDate"))
                event.setStartDate(df.parse(content.getString("StartDate")));
                
            if (!content.isNull("EndDate"))
                event.setEndDate(df.parse(content.getString("EndDate")));
            event.save();
            return "successfully changed event details";
        }
        catch(Exception e)
        {
            return(e.toString());
        }
    }
    /**
* Delete method for deleting an instance of User
* @param content representation for the resource
*/
   @Path("{id}")
   @DELETE
    public String deleteEvent(@PathParam("id") String id) {
      try {
           Event event = (Event)EntityBase.selectByID(id) ;
           if (event==null) {
                throw new RuntimeException("Delete: User with " + id  + " not found");
          }
          event.delete(true);
          return ("Successfully Deleted");
        } catch (Exception ex){
            return (ex.toString());
        }
     }

   @Path("/AddPerson/")
   @POST @Consumes ("application/json")
   public String addPersonToEvent(String content)
   {
       try
       {
           JSONObject json=new JSONObject(content);
           if ((json.isNull("eventid")) || (json.isNull("personid")))
                   return
                   ("Please specify an event and a person to add to the event");
           String eventId, personId;
           eventId=json.getString("eventid");
           personId=json.getString("personid");
           
           Event e=(Event)EntityBase.selectByID(eventId);
           Person p = (Person)EntityBase.selectByID(personId);

           if ((e==null)|| (p==null))
               return("One of the objects does not exist. Ensure event and person exist.");
           e.addPerson(p);
           e.save();
           return ("This person has been added to the Event");
       }
       catch (Exception e)
       {
           return (e.toString());
       }
   }
}
