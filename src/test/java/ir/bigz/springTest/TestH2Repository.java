package ir.bigz.springTest;

import ir.bigz.springTest.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TestH2Repository extends JpaRepository<Product, Long> {

    Optional<Product> getProductByName(String name);
}
