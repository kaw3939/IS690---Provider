/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package EntityDB;

import java.util.*;
import javax.persistence.*;
import java.io.Serializable;
import org.hibernate.*;
import org.hibernate.cfg.*;
/**
 * The base class for all entities.
 * @author Rick Shaub
 */
@Entity
@Table(name="entitybase"
    ,catalog="entitydb")
public class EntityBase extends PersistableObject implements Serializable
{
    
    @Id
    @Column(name="EntityID", unique=true, nullable=false, length=40)
     private String entityId;
    
    @Column(name = "EntityTypeID")
     private Integer entityTypeId;

    @Column(name = "EntityAccessStatus", length = 20)
     private String entityAccessStatus;

    @Column(name = "EntityParentEdit", length = 20)
     private String entityParentEdit;



    @ManyToOne
    @JoinColumn(name="OwnerID", referencedColumnName="EntityID")//, insertable=false, updatable=false)
     private User owner;


     /**
      * Default constructor for the EntityBase class
      */
     public EntityBase(){}
     /**
      *
      * @param entityId
      * Unique ID for this instance of EntityBase
      */
     public EntityBase(String entityId)
     {
        this.entityId = entityId;
     }

     /**
      *
      * @param entityId
      * Unique ID for this instance of EntityBase
      * @param entityTypeId
      * EntityType ID
      * @param entityAccessStatus
      *
      * @param entityParentEdit
      */
    public EntityBase(String entityId, Integer entityTypeId,
            String entityAccessStatus, String entityParentEdit)
    {
       this.entityId = entityId;
       this.entityTypeId = entityTypeId;
       this.entityAccessStatus = entityAccessStatus;
       this.entityParentEdit = entityParentEdit;
    }

    
    /**
     * Convenience method that generates a unique ID from a random UUID.
     * @return
     * A UUID to be used as an Entity ID.
     */
    public static String generateNewID()
    {
        return UUID.randomUUID().toString();
    }

    /**
     *Gets the Entity's ID
     * @return
     * The Entity's ID
     */
    public String getEntityId()
    {
        return this.entityId;
    }

    /**
     * Sets the entity's ID.
     * @param entityId
     */
    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }
    /**
     * Gets the entity type ID.
     * @return
     * The entity type ID.
     */
    public Integer getTypeId() {
        return this.entityTypeId;
    }
    /**
     * Sets the entity type ID.
     * @param entityTypeId
     */
    public void setTypeId(Integer entityTypeId) {
        this.entityTypeId = entityTypeId;
    }

    public String getAccessStatus() 
    {
        return this.entityAccessStatus;
    }

    public void setEntityAccessStatus(String entityAccessStatus)
    {
        this.entityAccessStatus = entityAccessStatus;
    }

    public String getParentEdit()
    {
        return this.entityParentEdit;
    }

    public void setParentEdit(String entityParentEdit) {
        this.entityParentEdit = entityParentEdit;
    }

    /**
     * Gets the User who created and/or manages this entity.
     * @return the owner
     */
    public User getOwner() {
        return owner;
    }

    /**
     * Sets the User who created and/or manages this entity.
     * @param owner the owner to set
     */
    public void setOwner(User owner) {
        this.owner = owner;
        
    }
    /**
     * Runs a select query on the ID given and returns the entity with that ID.
     * @param id
     * @return
     * The entity with the given ID or null if none exists.
     */
    protected static EntityBase selectByID(String id)
    {
        SessionFactory sessionFactory =SessionFactoryUtil.getInstance();
        // new AnnotationConfiguration().configure().buildSessionFactory();
        Session session =sessionFactory.openSession();
        Transaction tx = session.beginTransaction();

        List l = session.createQuery("from EntityBase b where b.entityId=:ID")
                .setString("ID", id).list();

        EntityBase[] entity = new EntityBase[l.size()];

        for(int i = 0;i<l.size();i++)
        {
            entity[i] = (User)l.get(i);
        }

        tx.commit();
        if(entity.length==0)
        {
            return null;
        }
        return entity[0];


    }

    /*
     * Creates a new ID for the entity. this should be called on a new entity
     * before saving it to the database.
     */
    public void createNewID()
    {
        this.setEntityId(generateNewID());
    }


    @Override
    protected Serializable getID()
    {
        return entityId;
    }

    
}


