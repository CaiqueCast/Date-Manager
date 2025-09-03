package gdl.date_manager.data;

import gdl.date_manager.model.ProductModel;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface ProductRepository extends JpaRepository <ProductModel, Integer> {
    @Modifying
    @Transactional
    @Query("UPDATE ProductModel p SET p.name = :name, p.validity = :validity, p.user.id = :userId  WHERE p.id = :id")
    public int update(@Param("name") String name,
                      @Param("validity") LocalDate validity,
                      @Param("userId") Integer userId,
                      @Param("id")Integer id);
}
