package foderking.speculate;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface LaptopRepository extends CrudRepository<Laptop, Long>{
    Iterable<Laptop> findAllByRatingGreaterThanEqual(int rating);

    @Query(value = """
SELECT * FROM LAPTOP
WHERE WEIGHT <= ?1 AND WEIGHT > 0
AND THICKNESS <= ?2 AND THICKNESS > 0
AND MAX_TEMPERATURE_LOAD <= ?3 AND  MAX_TEMPERATURE_LOAD > 0
AND MAX_TEMPERATURE_IDLE <= ?4 AND  MAX_TEMPERATURE_IDLE > 0
""", nativeQuery = true)
    Iterable<Laptop> filterAllColumns(
            float weight, float thickness, float max_temperature_load, float max_temperature_idle
    );
}
