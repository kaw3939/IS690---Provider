/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package EntityManager;

import EntityDB.EntityBase;
import EntityDB.PersistableObject;
import EntityDB.User;
import java.net.URI;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.PathParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.DELETE;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import org.json.JSONArray;
import org.json.JSONObject;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriBuilder;


/**
 * REST Web Service
 *
 * @author mpayyappilly
 */

@Path("/user/")
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
    @Path("{username}")
    @GET
    @Produces("application/json")
    public String retrieveUser(@PathParam("username") String astrEmail) {
        //TODO return proper representation object
          JSONObject json = new JSONObject();
      try {
           User u = User.selectByUsername(astrEmail) ;
           json.put("FirstName", u.getFirstName());
           json.put("LastName", u.getLastName());
           json.put("Email", u.getEmail());
           json.put("Phone", u.getPhone());
           json.put("UserName", u.getUserName());
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
        PersistableObject[] user = PersistableObject.getAllObjects("User");
         for(int i = 0;i<user.length;i++)
        {
           User u = (User) user[i] ;
           JSONObject json = new JSONObject();
           json.put("FirstName", u.getFirstName());
           json.put("LastName", u.getLastName());
           json.put("Email", u.getEmail());
           json.put("Phone", u.getPhone());
           json.put("UserName", u.getUserName());
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
   @Path("{username}")
   @DELETE
    public String  deleteUser(@PathParam("username") String astrEmail) {
      try {
           User u = User.selectByUsername(astrEmail) ;
           if(u==null) {
               // throw new RuntimeException("Delete: User with " + astrEmail +  " not found");
               return ("Delete: User with " + astrEmail +  " not found");
          }
          u.delete(true);
        } catch (Exception ex){
            return ex.getMessage();
        }
      return "Successfully deleted User :" + astrEmail;
     }

   @Path("/")
    /**
     * PUT method for  creating an instance of UserResource
     * @param content representation for the resource
     * @return an HTTP response with content of the  created resource.
     */
    
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
          if (content.getString("Email") != null) {
              u.setEmail(content.getString("Email"));
          }
          if (content.getString("Password") != null) {
             u.setPassword(content.getString("Password"));
          }
          if (content.getString("UserName") != null) {
             u.setUserName(content.getString("UserName"));
          }else {
              return "Cannot save a user without UserName!";
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
    @Path("{username}")
    @POST
    @Consumes("application/json")
    public String  updateUser(@PathParam("username") String astrEmail,String  json) {
        String strName  =null;
      try {
          JSONObject content = new JSONObject(json);
          User u = User.selectByUsername(astrEmail) ;
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
          if (content.getString("Email")!=null){
              u.setEmail(content.getString("Email"));
          }
          u.save();
          User uUpdated = User.selectByUsername(content.getString("UserName")) ;
          strName= uUpdated.getFirstName();
       } catch (Exception ex){
             return ex.toString();
       }
          return  "Successfully Updated User: " + strName;
    }

    @Path("/")
    @GET
    @Produces("application/json")
    public String getUsersAsJsonArray() {
        JSONArray uriArray = new JSONArray();
        PersistableObject [] po =  PersistableObject.getAllObjects("User");
        for (int i =0;i<po.length;i++){
            User u = (User) po[i];
            UriBuilder ub = context.getAbsolutePathBuilder();
            System.out.println(u.getUserName());
            URI userUri = ub.path(u.getUserName()).build();

            System.out.println(u.getUserName());
            uriArray.put(userUri.toASCIIString());
        }       
        return uriArray.toString();
    }

}
