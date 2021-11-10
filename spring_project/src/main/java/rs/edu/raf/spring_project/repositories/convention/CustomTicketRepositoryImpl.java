package rs.edu.raf.spring_project.repositories.convention;

import org.springframework.stereotype.Repository;
import rs.edu.raf.spring_project.model.entities.Ticket;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Repository
public class CustomTicketRepositoryImpl implements CustomTicketRepository{

    @PersistenceContext
    private EntityManager entityManager;


    @Override
    public List<Ticket> searchTickets(String oneway, String departDate, String returnDate, String origin, String destination, String airline) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date dd= null;
        Date rd= null;

        System.out.println(oneway);
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Ticket> query = criteriaBuilder.createQuery(Ticket.class);

        Root<Ticket> root = query.from(Ticket.class);
        List<Predicate> predicates = new ArrayList<>();

        if(oneway.equals("oneWay")) predicates.add(criteriaBuilder.equal(root.get("oneway"), true));
        if(oneway.equals("twoWay")) predicates.add(criteriaBuilder.equal(root.get("oneway"), false));

        if(oneway.equals("all")){
            Predicate p1 = criteriaBuilder.equal(root.get("oneway"), true);
            Predicate p2 = criteriaBuilder.equal(root.get("oneway"), false);
            Predicate p = criteriaBuilder.or(p1,p2);
            predicates.add(p);
        }

        if(!(departDate.equals("") || returnDate.equals(""))){
            try {
                dd = sdf.parse(departDate);
                rd= sdf.parse(returnDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Predicate p1 = criteriaBuilder.lessThan(root.<Date>get("departDate"), dd);
            Predicate p2 = criteriaBuilder.greaterThanOrEqualTo(root.<Date>get("returnDate"), rd);
            Predicate p = criteriaBuilder.and(p1,p2);
            predicates.add(p);
        }else if(!departDate.equals("")){
            try {
                dd = sdf.parse(departDate);
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.<Date>get("departDate"), dd));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }else if(!returnDate.equals("")){
            try {
                rd= sdf.parse(returnDate);
                predicates.add(criteriaBuilder.lessThan(root.<Date>get("returnDate"), rd));
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }else{

        }


        if(!(origin.equals("") || origin==null)) predicates.add(criteriaBuilder.equal(root.get("flight").get("origin").get("name"),origin));
        if(!(destination.equals("") || destination==null)) predicates.add(criteriaBuilder.equal(root.get("flight").get("destination").get("name"),destination));
        if(!(airline.equals("") || airline==null)) predicates.add(criteriaBuilder.equal(root.get("airline").get("name"),airline));
        query.select(root).where(predicates.toArray(new Predicate[]{}));

        return entityManager.createQuery(query).getResultList();
    }


}
