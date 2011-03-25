/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package EntityManager;

import EntityDB.EntityBase;
import EntityDB.User;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.PathParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.DELETE;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import org.json.JSONObject;

/**
 * REST Web Service
 *
 * @author mpayyappilly
 */

@Path("user")
public class UserResource {
    @Context
    private UriInfo context;

    /** Creates a new instance of UserResource */
    public UserResource() {
    }

    /**
     * Retrieves representation of an instance of EntityDB.UserResource
     * @return an instance of EntityDB.User
     */
    @Path("/retrieve")
    @GET
    @Produces("application/json")
    public String retrieveUser(@QueryParam("email") String astrEmail) {
        //TODO return proper representation object
          JSONObject json = new JSONObject();
      try {
           JSONObject content = new JSONObject(astrEmail);
           User u = User.getUserByPassword((String) content.getString("Email"), (String) content.getString("Password"));
           json.put("FirstName", u.getFirstName());
           json.put("LastName", u.getLastName());
           json.put("Email", u.getEmail());
           json.put("Phone", u.getEmail());
           json.put("EntityId", u.getEntityId());
        } catch (Exception ex){
            return ex.toString();
        }
        return json.toString();
    }

    /**
     * Delete method for deleting  an instance of User
     * @param content representation for the resource
     */

    @Path("/delete")
    @DELETE
    public String  deleteUser(@QueryParam("email") String astrEmail) {
        String strEntityId=null;
      try {
      
      //    User u = User.selectByUsername(content.getString("Email")) ;
       //    User u = User.getUserByPassword(content.getString("Email"),content.getString("Password"));
           JSONObject content = new JSONObject(astrEmail);
           User u = User.getUserByPassword((String) content.getString("Email"), (String) content.getString("Password"));
           strEntityId= u.getEntityId();
           u.delete(true);
        } catch (Exception ex){
           return ex.toString();
        }
      return strEntityId;
    }

    /**
     * PUT method for  creating an instance of UserResource
     * @param content representation for the resource
     * @return an HTTP response with content of the  created resource.
     */
    @Path("/create")
    @PUT
    @Consumes("application/json")
    public String  addUser(String  json) {
        String strName  =null;
      try {
          JSONObject content = new JSONObject(json);
          User u = new User();
          u.createNewID();
          String strEmail = (String) content.get("Email");
          u.setEmail(strEmail);
          u.setFirstName((String) content.get("FirstName"));
          u.setLastName((String) content.get("LastName"));
          u.setPhone((String) content.get("Phone"));
           String pwd2 = EntityBase.generateNewID();
          u.setPassword(pwd2);
          u.save();
          strName= (String) content.get("FirstName");
       } catch (Exception ex){
             return ex.toString();
       }
          return  "Successfully Added User: " + strName;
    }

    /**
     * Post method for updating an instance of UserResource
     * @param content representation for the resource
     * @return an HTTP response with content of the  created resource.
     */
    @Path("/update")
    @POST
    @Consumes("application/json")
    public String  updateUser(String  json) {
        String strName  =null;
      try {
          JSONObject content = new JSONObject(json);
         // User u = User.selectByUsername(content.getString("Email")) ;
          User u = User.getUserByPassword((String) content.getString("Email"), (String) content.getString("Password"));

          u.setFirstName((String) content.get("FirstName"));
          u.setLastName((String) content.get("LastName"));
          u.setPhone((String) content.get("Phone"));
          u.setPassword((String) content.get("Password"));
          u.save();
       //   User uUpdated = User.selectByUsername(content.getString("Email")) ;
          User uUpdated = User.getUserByPassword((String) content.getString("Email"), (String) content.getString("Password"));

          strName= uUpdated.getFirstName();
       } catch (Exception ex){
             return ex.toString();
       }
          return  "Successfully Updated User: " + strName;
    }
}
