
import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;


import static java.nio.charset.StandardCharsets.UTF_8;

public class DatabaseStatistics {
    public static final String RESIDENTS_WITH_OWNERSHIP_AND_ACCESS_PERMISSIONS_QUERY = "SELECT" +
            " p.name AS name," +
            " p.email AS email," +
            " b.address AS address," +
            " a.number AS number," +
            " a.area AS area" +
            " FROM" +
            " person AS p" +
            " JOIN aparnment_ownership AS o ON p.id = o.person_id" +
            " JOIN apartment AS a ON o.apartment_id = a.id" +
            " JOIN building AS b ON a.building_id = b.id" +
            " JOIN resident AS r ON p.id = r.person_id" +
            " WHERE" +
            " r.entry_right = 1" +
            " GROUP BY" +
            " p.id, b.address, a.number, a.area" +
            " HAVING" +
            " COUNT(a.id) < 2";

    private final String url;
    private final String username;
    private final String password;

    public DatabaseStatistics(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    public void printResidentsWithOwnershipAndAccessPermissions() {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("osbb");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();

        System.out.println("_______________");
        List<Building> buildingsN = buildings_processWithNativeSQL(entityManager);
        for (Building building : buildingsN) {
            System.out.println(building.getAddress());

            for (Flat flat : building.getFlats())
                System.out.println("\t:" + flat.getNumber());
        }

        System.out.println("_______________");
        List<Building> buildings = buildings_processWithJPQL(entityManager);
        for (Building building : buildings) {
            System.out.println(building.getAddress());

            for (Flat flat : building.getFlats()) {
                System.out.println("\t:" + flat.getNumber());
            }
        }


        System.out.println("_______________");
        List<Building> buildingsD = buildings_processWithNameByAddress(entityManager, "Sajevisha");
        for (Building building : buildings) {
            System.out.println(building.getAddress());

        }


        System.out.println("_______________");
        List<Flat> flats = ftats_processWithCriterAPI(entityManager, 1, 32.f);
        for (Flat flat : flats)
            System.out.println("\t:" + flat.getNumber());

        try {
            transaction.begin();

            // операції з базаю тут

            Building building = entityManager.find(Building.class, 1L);
            entityManager.remove(building);

            Building newBuilding = new Building();
            newBuilding.setAddress("Kudry, 11");

            Flat newFlat1 = new Flat();
            newFlat1.setNumber("1");
            newFlat1.setBuilding(newBuilding);

            Flat newFlat2 = new Flat();
            newFlat1.setNumber("2");
            newFlat1.setBuilding(newBuilding);

            newBuilding.getFlats()
                    .add(newFlat1);
            newBuilding.getFlats()
                    .add(newFlat2);

            entityManager.persist(newBuilding);

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.getRollbackOnly()) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            entityManager.close();
            entityManagerFactory.close();
        }
    }


    static List<Building> buildings_processWithNativeSQL(EntityManager entityManager) {
        Query query = entityManager.createNativeQuery(
                "SELECT * FROM buildings b WHERE id = 1",
                Building.class);
        return query.getResultList();

    }

    static List<Building> buildings_processWithJPQL(EntityManager entityManager) {
        TypedQuery<Building> query = entityManager.createQuery(
                "SELECT * FROM Building b WHERE id = :id",
                Building.class);
        query.setParameter("id", 1L);
        return query.getResultList();
    }

    static List<Building> buildings_processWithNameByAddress(EntityManager entityManager, String address) {
        TypedQuery<Building> query = entityManager.createNamedQuery(
                "Building.findAddress",
                Building.class);
        query.setParameter("address", address);
        return query.getResultList();
    }

    static List<Flat> ftats_processWithCriterAPI(EntityManager entityManager, int floor, float reqSqure) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Flat> criteriaQuery = criteriaBuilder.createQuery(Flat.class);

        Root<Flat> root = criteriaQuery.from(Flat.class);
        criteriaQuery.select(root);

        List<Predicate> predicates = new ArrayList<>();

        predicates.add(
                criteriaBuilder.equal(root.get("stage").as(Integer.class), floor));

        if(reqSqure > 0.f)
            predicates.add(
                    criteriaBuilder.ge(root.get("square").as(Float.class), reqSqure));

        criteriaQuery.where(
                predicates.toArray(new Predicate[0]));

        TypedQuery<Flat> query = entityManager.createQuery(criteriaQuery);

        return query.getResultList();
    }
}
