/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package EntityManager;

import EntityDB.PersistableObject;
import EntityDB.TextType;
import java.net.URI;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.PathParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.core.UriBuilder;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * REST Web Service
 *
 * @author Mayank Desai
 */

@Path("texttype")
public class TexttypeResource {
    @Context
    private UriInfo context;

    /** Creates a new instance of TexttypeResource */
    public TexttypeResource() {
    }

    /**
     * Retrieves representation of an instance of EntityDB.TexttypeResource
     * @return an instance of java.lang.String
     */
    @GET
    @Produces("application/json")
    public String getTextType()
    {
        //TODO return proper representation object
        JSONArray uriArray = new JSONArray();
        PersistableObject [] po =  PersistableObject.getAllObjects("TextType");
        for (int i =0;i<po.length;i++)
        {
            TextType t = (TextType) po[i];
            UriBuilder ub = context.getAbsolutePathBuilder();
            URI tUri = ub.path(t.getTexttypeID().toString()).build();
            uriArray.put(tUri.toASCIIString());
        }
        return uriArray.toString();    
       //throw new UnsupportedOperationException();
    }

     /**
     * PUT method for updating or creating an instance of TexttypeResource
     * @param content representation for the resource
     * @return an HTTP response with content of the updated or created resource.
     */

    @PUT
    @Consumes("application/json")
    public String createTextType(String t)
    {
        Integer id = null;
        String textTypeDesc = null;
      try
      {
          JSONObject data = new JSONObject(t);
          TextType t1 = new TextType();
          id = (Integer) data.get("id");
          t1.setTexttypeID(id);
          textTypeDesc = (String) data.get("desc");
          t1.setTexttypeDesc(textTypeDesc);
          t1.save();
       } 
      catch (Exception E)
      {
             return E.toString();
      }
       return  "Successfully added new Text Type with ID of: " + id + " and Description of:" + textTypeDesc;
    }

    @Path("{id}")
    @DELETE
    public String deleteTextType(@PathParam ("id") Integer id)
    {
        TextType t = TextType.findByID(id);
        t.delete(true);
        return ("Successfully delete Text Type with id of:"+ id );
    }

    @POST
    @Path("{id}")
    @Consumes("application/json")
    public String updateTextType(@PathParam ("id") Integer i, String t)
    {
        try
        {
            JSONObject data = new JSONObject(t);
            Integer id = (Integer) data.get("Id");
            String desc = data.getString("Desc");
            TextType t1 = TextType.findByID(id);
            if  (desc.length() !=0)
               t1.setTexttypeDesc(desc);
            t1.save();
            return ("Successfully updated Text Type Description of ID:"+ id + " to "+ desc);
        }
        catch (Exception E)
        {
             return (E.toString());
        }
    }

    @Path("{id}")
    @GET
    @Produces("application/json")
    public String retrieveTextType(@PathParam ("id") Integer id)
    {
        //TODO return proper representation object
        JSONObject json= new JSONObject();
        try
        {
           TextType t = (TextType) TextType.findByID(id);
           if (t==null)
                return "No Text Type found in the system";
           json.put("id", t.getTexttypeID());
           json.put("desc", t.getTexttypeDesc());
           return json.toString();
        } 
        catch (Exception ex)
        {
            return (ex.toString());
        }
    }

}
