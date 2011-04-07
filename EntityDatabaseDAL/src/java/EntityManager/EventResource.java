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
import org.json.JSONArray;
import org.json.JSONObject;
import javax.ws.rs.core.Context;
import java.text.*;
import java.util.*;
import EntityDB.*;
import java.net.URI;
import javax.ws.rs.core.UriBuilder;
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
          SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
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

   @Path("/{eventid}/person/")
   @PUT @Consumes ("application/json")
   public String addPeopleToEvent(@PathParam("eventid") String eventId,String content)
   {
       try
       {
           JSONObject json=new JSONObject(content);
           if (eventId == null)
                   return
                   ("Please specify an event and a person or list of persons to add to the event");
           JSONArray jsonArray =new JSONArray();
           jsonArray = json.getJSONArray("Person");
           Event e=(Event)EntityBase.selectByID(eventId);
           if ((e==null))
               return("This event does not exist.");
           for(int i = 0;i<jsonArray.length();i++){
               JSONObject person = ( JSONObject) jsonArray.get(i);
               String personId = person.getString("id");
               Person p = (Person)EntityBase.selectByID(personId);
               e.addPerson(p);

           }
           e.save();
           return (jsonArray.length()+ " person(s) added to the Event");
       }
       catch (Exception e)
       {
           return (e.toString());
       }
   }

   @Path("/{eventid}/person/")
   @GET
   public String retrievePeopleForAnEvent(@PathParam("eventid") String eventId)
   {
       try
       {
          if (eventId == null)
                   return
                   ("Please specify an event to get a list of persons registered for");

           Event e=(Event)EntityBase.selectByID(eventId);
           if ((e==null))
               return("Event does not exist.");
          Set <Person> sPerson =  e.getPeople();        
          JSONArray jsonArray =new JSONArray();
          Iterator itr = sPerson.iterator();
          while(itr.hasNext()){
             JSONObject json = new JSONObject();
             Person p = (Person) itr.next() ;
             json.put("Email", p.getEmail());
             json.put("Phone", p.getPhone());
             json.put("FirstName",p.getFirstName());
             json.put("LastName", p.getLastName());
             jsonArray.put(json);
          }
           return (jsonArray.toString());
       }
       catch (Exception e)
       {
           return (e.toString());
       }
   }

    @Path("/list/all")
    @GET
    @Produces("application/json")
    public String retrieveAllEvents() {
        //TODO return proper representation object

      JSONArray jsonArray =new JSONArray();
      try {
        Event [] e =EntityBase.getAllEvents();
         for(int i = 0;i<e.length;i++)
        {
           Event event = e[i] ;
           JSONObject json = new JSONObject();
           json.put("StartDate", event.getStartDate());
           json.put("EndDate", event.getEndDate());
           json.put("EntityID", event.getEntityId());
           jsonArray.put(json);
        }

     //   json.put("Users:", user);
        } catch (Exception ex){
            return ex.toString();
        }
        return jsonArray.toString();
    }

  @Path("/")
    @GET
    @Produces("application/json")
    public String getEventsAsJsonArray() {
        JSONArray uriArray = new JSONArray();
        for (Event eventEntity : EntityBase.getAllEvents()) {
            UriBuilder ub = context.getAbsolutePathBuilder();
            URI userUri = ub.path(eventEntity.getEntityId()).build();
            uriArray.put(userUri.toASCIIString());
        }
        return uriArray.toString();
    }


  @Path("/{eventid}/person/list")
   @GET
   public String getPeopleListForAnEvent(@PathParam("eventid") String eventId)
   {
       try
       {
          if (eventId == null)
                   return
                   ("Please specify an event to get a list of persons registered for");

           Event e=(Event)EntityBase.selectByID(eventId);
           if ((e==null))
               return("Event does not exist.");
          JSONArray uriArray = new JSONArray();
        for (Person personEntity : e.getPeople()) {
            UriBuilder ub = PersonResource.context.getAbsolutePathBuilder();
            URI userUri = ub.path(personEntity.getEntityId()).build();
            uriArray.put(userUri.toASCIIString());
        }
        return uriArray.toString();
       }
       catch (Exception e)
       {
           return (e.toString());
       }
   }


}
