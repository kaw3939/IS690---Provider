/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package EntityManager;

import EntityDB.EntityBase;
import EntityDB.User;
import java.util.List;
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
import org.json.JSONArray;
import org.json.JSONObject;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;


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
           User u = User.selectByUsername((String) content.getString("Email"));
           json.put("FirstName", u.getFirstName());
           json.put("LastName", u.getLastName());
           json.put("Email", u.getEmail());
           json.put("Phone", u.getPhone());
        } catch (Exception ex){
            return ex.toString();
        }
        return json.toString();
    }

    /**
     * Retrieves representation of an instance of EntityDB.UserResource
     * @return an instance of EntityDB.User
     */
    @Path("/list/all")
    @GET
    @Produces("application/json")
    public String retrieveAllUsers() {
        //TODO return proper representation object
    
      JSONArray jsonArray =new JSONArray();
      try {
        User[] user =EntityBase.getAllUsers();
         for(int i = 0;i<user.length;i++)
        {
           User u = user[i] ;
           JSONObject json = new JSONObject();
           json.put("FirstName", u.getFirstName());
           json.put("LastName", u.getLastName());
           json.put("Email", u.getEmail());
           json.put("Phone", u.getPhone());
           jsonArray.put(json);
        }

     //   json.put("Users:", user);
        } catch (Exception ex){
            return ex.toString();
        }
        return jsonArray.toString();
    }


    /**
     * Delete method for deleting  an instance of User
     * @param content representation for the resource
     */
   @Path("/delete")
   @DELETE
    public void  deleteUser(@QueryParam("email") String astrEmail,  @Context SecurityContext context) {
      try {
           JSONObject content = new JSONObject(astrEmail);
           User u = User.selectByUsername(content.getString("Email")) ;
           if(u==null) {
                throw new RuntimeException("Delete: User with " + astrEmail +  " not found");
            };
             u.delete(true);
        } catch (Exception ex){
            ex.printStackTrace();
        }
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
          if (content.getString("FirstName") != null) {
            u.setFirstName(content.getString("FirstName"));
          }
          if (content.getString("LastName") != null) {
              u.setLastName(content.getString("LastName"));
          }
          if (content.getString("Phone") != null) {
              u.setPhone(content.getString("Phone"));
          }
          if (content.getString("Password") != null) {
             u.setPassword(content.getString("Password"));
          }
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
          User u = User.selectByUsername(content.getString("Email")) ;
          if (content.getString("FirstName") != null) {
            u.setFirstName(content.getString("FirstName"));
          }
          if (content.getString("LastName") != null) {
              u.setLastName(content.getString("LastName"));
          }
          if (content.getString("Phone") != null) {
              u.setPhone(content.getString("Phone"));
          }
          if (content.getString("Password") != null) {
             u.setPassword(content.getString("Password"));
          }
          u.save();
          User uUpdated = User.selectByUsername(content.getString("Email")) ;
          strName= uUpdated.getFirstName();
       } catch (Exception ex){
             return ex.toString();
       }
          return  "Successfully Updated User: " + strName;
    }
}
