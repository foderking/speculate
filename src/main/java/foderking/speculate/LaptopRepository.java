package foderking.speculate;

import org.springframework.data.repository.CrudRepository;

public interface LaptopRepository extends CrudRepository<Laptop, Long>{
    Iterable<Laptop> findAllByRatingGreaterThanEqual(int rating);
}
