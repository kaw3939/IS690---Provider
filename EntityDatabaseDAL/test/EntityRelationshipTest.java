/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import EntityDB.*;
import java.util.*;

/**
 *
 * @author Rick Shaub
 */
public class EntityRelationshipTest {

    public EntityRelationshipTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
    @Test
    public void testEventPersonRelationship()
    {
        Person p = new Person();
        p.setEntityAccessStatus("1");
        p.createNewID();
        p.setTypeId(0);
        p.setEmail("person@testing.com");
        p.setFirstName("Mark");
        p.setLastName("HopeWorks");
        p.setPhone("800-587-6309");
        p.save();

        Event e = new Event();
        e.setEntityId(EntityBase.generateNewID());
        Calendar cal = Calendar.getInstance();
        cal.set(2011, 4, 5, 11, 0);

        Date date1 = new Date(cal.getTimeInMillis());

        cal.set(2011, 4, 5, 17, 0);

        Date date2 = new Date(cal.getTimeInMillis());


        e.setStartDate(date1);
        e.setEndDate(date2);

        e.addPerson(p);
        e.save();

    }
    

}