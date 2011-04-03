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
import org.json.JSONObject;
import EntityDB.*;


/**
 * REST Web Service
 *
 * @author ramyabalaji
 */

@Path("/person/")
public class PersonResource {
    @Context
    private UriInfo context;

    /** Creates a new instance of PersonResource */
    public PersonResource() {
    }

    /**
     * Retrieves representation of an instance of EntityDB.PersonResource
     * @return an instance of EntityDB.Person
     */
    @Path("{email}")
    @GET     @Produces("application/json")
    public String retrievePerson(@PathParam ("email") String strPersonEmail) {
        //TODO return proper representation object
        JSONObject json= new JSONObject();
        try
        {
           Person person=Person.selectByPersonEmail(strPersonEmail);
           if (person==null)
                return "This Person does not exist in the system";
           json.put("Email", person.getEmail());
           json.put("Phone", person.getPhone());
           json.put("FirstName",person.getFirstName());
           json.put("LastName", person.getLastName());
           return json.toString();
        } catch (Exception ex){
            return (ex.toString());
           // return json;
        }                    
}

    /** POST method to update fields. Email is not updateable**/
    @Path("{email}")
    @POST @Consumes("application/json")
    public String updatePerson(String personInfo)
    {
        try
        {
            JSONObject content=new JSONObject(personInfo);
            String firstName=content.getString("FirstName");
            String lastName=content.getString("LastName");
            String phone=content.getString("Phone");
            Person person=Person.selectByPersonEmail(content.getString("Email"));
            if  (firstName.length() !=0)
               person.setFirstName(firstName);
            if  (lastName.length() !=0)
               person.setLastName(lastName);
            if  (phone.length() >9)
               person.setPhone(phone);
            person.save();
            return ("Successfully Updated People- Non system User");

        }
        catch (Exception E)
        {
             return (E.toString());
        }
    }
   

    
    @Path("{id}")
    @DELETE
    public String deletePerson(@PathParam ("id") String id){
    {
      try {

          System.out.println("ID is "+id);
          //Note: make sure this works.  Right now it is untested.
          Person person = (Person)EntityBase.selectByID(id);//getEntityByID(id, "Person");
          if(person instanceof User)
          {
               return "Error: Person is a system user.  Please delete through the User interface.";
          }

           //TODO: Create a parsible message for the application devs.
          if(person == null)
          {
              return "Error: Person doesn't exist.";
          }
          User owner=person.getOwner();
          if (owner !=null)
          {
              if (owner.getEntityId()== id )//Owner is the same Entity
              {
                 person.delete(true);
              }
              else
              {
                  //Authenticate For Owner and then delete- include code for authentication
                  person.delete(true);
              }
          }
          else
          {
          person.delete(true);
          }
           return ("Person Delete Successful");
        } catch (Exception E){
           return (E.getMessage());
        }
     }
    }


    
    
    
    /**
     * PUT method for creating an instance of PersonResource
     * @param content representation for the resource
     * @return an HTTP response with content of the updated or created resource.
     */
    @Path("/")
    @PUT
    @Consumes("application/json")
    public String createPerson(String json) {
      String personEmail  =null;
      try {
          JSONObject content = new JSONObject(json);
          Person person = new Person();
          person.createNewID();
          String strEmail = (String) content.get("Email");
          person.setEmail(strEmail);
          person.setFirstName((String) content.get("FirstName"));
          person.setLastName((String) content.get("LastName"));
          person.setPhone((String) content.get("Phone"));
          person.save();
          personEmail= (String) content.get("Email");
       } catch (Exception E){
             return E.toString();
       }
       return  "Successfully Added Non System User(Person): " + personEmail;
    }


    }

