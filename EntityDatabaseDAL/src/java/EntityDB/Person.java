/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package EntityDB;
import java.util.*;
import javax.persistence.*;
import java.io.Serializable;
import org.hibernate.*;




/**
 *
 * @author Rick Shaub
 */
@Entity
@Table(name="people"
    ,catalog="entitydb")
public class Person extends EntityBase
{
    @Column(name = "PeopleFirstName", length = 50)
    private String peopleFirstName;
    @Column(name = "PeopleLastName", length = 50)
    private String peopleLastName;
    @Column(name = "PeopleEmail", length = 100)
    private String peopleEmail;
    @Column(name="PeoplePhone", length=20)
    private String peoplePhone;
    //private String userID = "0";
    public Person(){}
    
    public String getFirstName() {
        return this.peopleFirstName;
    }
    
    public void setFirstName(String peopleFirstName) {
        this.peopleFirstName = peopleFirstName;
    }
    
    public String getLastName() {
        return this.peopleLastName;
    }
    
    public void setLastName(String peopleLastName) {
        this.peopleLastName = peopleLastName;
    }
    
    public String getEmail() {
        return this.peopleEmail;
    }
    
    public void setEmail(String peopleEmail) {
        this.peopleEmail = peopleEmail;
    }
    
    
    public String getPhone() {
        return this.peoplePhone;
    }
    
    public void setPhone(String peoplePhone) {
        this.peoplePhone = peoplePhone;
    }

   
    /***
     * Selects a Person by email)
     * @param name Person's email address
     * @return The Person with the matching username or null if no user exists.
     */
    public static Person selectByPersonEmail(String Email)
    {
        try
        {
        SessionFactory sessionFactory =SessionFactoryUtil.getInstance();
        // new AnnotationConfiguration().configure().buildSessionFactory();
        Session session =sessionFactory.openSession();
        Transaction tx = session.beginTransaction();

        List l = session.createQuery("from Person p where p.peopleEmail=:personEmail")
                .setString("personEmail", Email).list();

        Person[] person = new Person[l.size()];

        for(int i = 0;i<l.size();i++)
        {
            person[i] = (Person)l.get(i);
        }

        tx.commit();

        if(person.length==0)
        {
            return null;
        }
        return person[0];
        }//try

        catch( Exception E)
        {
            E.printStackTrace();
            return null;
        }

        }
   /***
     * Deletes the Person if Person is not a user
     */
    @Override
    public void delete(boolean load)
    {

        try
        {
        User PersonIfUser= (User)(this);
        }
        catch (ClassCastException E)
        {
           //This Person is not a User. Delete can proceed
           //Ensure that there is no Owner-User for this Person
            if (this.getOwner() == null)
            /* This is a stand alone Person Object, can be deleted with authentication
             person/user initiating action has to be checked or not??*/
            {
                super.delete(true);
            }

        }

        
        }
}
      



    
    



