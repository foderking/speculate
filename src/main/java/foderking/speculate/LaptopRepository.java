package foderking.speculate;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.UUID;

public interface LaptopRepository extends CrudRepository<Laptop, UUID>{
    @Query(value = """
SELECT * FROM LAPTOP
WHERE WEIGHT <= ?1 AND WEIGHT > 0
AND THICKNESS <= ?2 AND THICKNESS > 0
AND MAX_TEMPERATURE_LOAD <= ?3 AND  MAX_TEMPERATURE_LOAD > 0
AND MAX_TEMPERATURE_IDLE <= ?4 AND  MAX_TEMPERATURE_IDLE > 0
AND BATTERY >= ?5
AND COVERAGE_SRGB >= ?6 AND COVERAGE_SRGB > 0
AND COVERAGE_ADOBERGB >= ?7 AND COVERAGE_ADOBERGB > 0
AND COVERAGE_P3 >= ?8 AND COVERAGE_P3 > 0
AND BRIGHTNESS >= ?9 AND BRIGHTNESS > 0
AND RESPONSE_GG <= ?10 AND RESPONSE_GG > 0
ORDER BY RATING DESC    
""", nativeQuery = true)
    Iterable<Laptop> filterAllColumns(
            float weight, float thickness, float max_temperature_load, float max_temperature_idle,
            int battery, float coverage_sRGB, float coverage_adobergb, float coverage_p3,
            float brightness, float response_time_gg
    );

    boolean existsByLink(String link);
}
