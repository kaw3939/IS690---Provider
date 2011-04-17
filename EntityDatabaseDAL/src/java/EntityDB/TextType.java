/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package EntityDB;

import java.util.*;
import javax.persistence.*;
import java.io.Serializable;
import org.hibernate.Session;
import org.hibernate.*;
import org.hibernate.cfg.*;
/**
 *
 * @author Mayank Desai
 */
@Entity
@Table(name="entitytexttype",catalog="entitydb")

public class TextType extends PersistableObject implements Serializable
{

    @Id
    @Column(name="EntityTextTypeID", length=10)
    private Integer texttypeID;

    @Column(name = "EntityTextTypeDesc", length=100)
    private String texttypeDesc;


    public Integer getTexttypeID() {
        return texttypeID;
    }

    public void setTexttypeID(Integer texttypeID)
    {
        this.texttypeID = texttypeID;
    }

    public String getTexttypeDesc()
    {
        return this.texttypeDesc;
    }

    public void setTexttypeDesc(String texttypeDesc)
    {
        this.texttypeDesc = texttypeDesc;
    }

    public static TextType findByID(Integer id)
    {
        SessionFactory sessionFactory = SessionFactoryUtil.getInstance();
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();

        List l = session.createQuery("from TextType t where t.texttypeID=" + id ).list();

        TextType[] t = new TextType[l.size()];

        for(int i = 0;i<l.size();i++)
        {
            t[i] = (TextType)l.get(i);
        }
        tx.commit();
        if(t.length==0)
        {
            return null;
        }
        return t[0];
    }

    public static TextType findByDesc(String data)
    {
        SessionFactory sessionFactory = SessionFactoryUtil.getInstance();
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();

        List l = session.createQuery("from TextType t where t.texttypeDesc=:desc").setString("desc", data).list();

        TextType[] t = new TextType[l.size()];

        for(int i = 0;i<l.size();i++)
        {
            t[i] = (TextType)l.get(i);
        }
        tx.commit();
        if(t.length==0)
        {
            return null;
        }
        return t[0];
    }

    @Override
    protected Serializable getID() {
        return texttypeID;
    }
}
