package services;

import models.People;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.LinkedList;
import java.util.List;

public class PeopleService {
    private static Session session;
    private static SessionFactory sessionFactory;

    /**
     * It is unclear if this initialization method is necessary now that we are
     * using hibernate rather than a direct repo.
     */
    public static void init(){}

    /**
     * This method saves a People instance to our database
     * @param person
     */
    public static void saveNewPerson(People person){
        session.save(person);
    }

    /**
     * This method fetches a person from our database based on their people_id.
     * @param people_id
     * @return
     */
    public static People getPersonById(int people_id){
        return session.get(People.class, people_id);
    }

    /**
     * This method returns a list of all people in our database
     * @return
     */
    public static List<People> getAllPeople(){
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<People> query = builder.createQuery(People.class);
        Root<People> root = query.from(People.class);
        query.select(root);
        return session.createQuery(query).getResultList();
    }

    /**
     * This method is incomplete, we need to complete the hibernate logic on it.
     * @param flight_id
     * @return
     */
    public static List<People> getPassengersByFlight(int flight_id){
        List<People> manifest = new LinkedList<>();
        return manifest;
    }


}