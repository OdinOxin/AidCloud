package de.odinoxin.aidcloud;

import com.sun.net.httpserver.HttpHandler;
import de.odinoxin.aidcloud.plugins.people.Person;
import de.odinoxin.aidcloud.plugins.people.Person_;
import org.hibernate.Session;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

@WebService
public class Login {
    private static final int SESSION_DURATION = 10000;
    private static Map<Integer, String> sessions = new Hashtable<>();

    public static boolean checkSession(WebServiceContext wsCtx) {
        MessageContext msgCtx = wsCtx.getMessageContext();
        Map<String, List<String>> httpHeaders = (Map<String, List<String>>) msgCtx.get(MessageContext.HTTP_REQUEST_HEADERS);
        List<String> users = httpHeaders.get("Username");
        List<String> pwds = httpHeaders.get("Password");
        int id = 0;
        String pwd = "";
        if (users != null && users.size() > 0 && users.get(0).matches("-?\\d+"))
            id = Integer.parseInt(users.get(0));
        if (pwds != null && pwds.size() > 0)
            pwd = pwds.get(0);
        return Login.sessions.containsKey(id) && Login.sessions.get(id).equals(pwd);
    }

    @WebMethod
    public String getSession(@WebParam(name = "id") int id, @WebParam(name = "pwd") String pwd) {
        if (this.checkLogin(id, pwd)) {
            Login.sessions.put(id, pwd);
//            Login.sessions.put(id, UUID.randomUUID().toString());
//            new Thread(() -> {
//                try {
//                    Thread.sleep(SESSION_DURATION);
//                } catch (InterruptedException ex) {
//                    ex.printStackTrace();
//                }
//                Login.sessions.remove(id);
//            }).start();
            return "OK"; //Login.sessions.get(id);
        }
        return null;
    }

    @WebMethod
    public boolean checkLogin(@WebParam(name = "id") int id, @WebParam(name = "pwd") String pwd) {
        boolean access = false;
        Session session = DB.open();
        CriteriaBuilder builder = session.getEntityManagerFactory().getCriteriaBuilder();
        CriteriaQuery<Person> criteria = builder.createQuery(Person.class);
        Predicate predicates = builder.conjunction();
        Root<Person> root = criteria.from(Person.class);
        predicates = builder.and(predicates, builder.equal(root.get(Person_.id), id));
        Predicate pwdPredicates = builder.disjunction();
        pwdPredicates = builder.or(pwdPredicates, builder.equal(root.get(Person_.pwd), pwd));
        if(pwd == null || pwd.isEmpty())
            pwdPredicates = builder.or(pwdPredicates, builder.isNull(root.get(Person_.pwd)));
        predicates = builder.and(predicates, pwdPredicates);
        criteria.where(predicates);
        List<Person> tmpList = session.getEntityManagerFactory().createEntityManager().createQuery(criteria).getResultList();
        if (tmpList != null && tmpList.size() == 1)
            access = true;
        session.close();
        return access;
    }

    @WebMethod
    public List<Person> searchLogin(@WebParam(name = "expr") String[] expr, @WebParam(name = "max") int max) {
        Session session = DB.open();
        List<Person> result = new ArrayList<>();
        CriteriaBuilder builder = session.getEntityManagerFactory().getCriteriaBuilder();
        CriteriaQuery<Person> criteria = builder.createQuery(Person.class);
        Predicate predicates = builder.conjunction();
        Root<Person> root = criteria.from(Person.class);
        if (expr != null && expr.length > 0) {
            for (int i = 0; i < expr.length; i++)
                expr[i] = "%" + expr[i].toLowerCase() + "%";
            predicates = builder.disjunction();
            for (int i = 0; i < expr.length; i++) {
                predicates = builder.or(predicates, builder.like(builder.lower(root.get(Person_.forename)), expr[i]));
                predicates = builder.or(predicates, builder.like(builder.lower(root.get(Person_.name)), expr[i]));
                predicates = builder.or(predicates, builder.like(builder.lower(root.get(Person_.code)), expr[i]));
            }
        }
        criteria.where(predicates);
        List<Person> tmpList = session.getEntityManagerFactory().createEntityManager().createQuery(criteria).setMaxResults(Math.max(0, max) + 1).getResultList();
        if (tmpList != null)
            for (Person tmp : tmpList)
                result.add(new Person(tmp.getId(), tmp.getName(), tmp.getForename(), tmp.getCode(), null, null, null));
        session.close();
        return result;
    }
}
