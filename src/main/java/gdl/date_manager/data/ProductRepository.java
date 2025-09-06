package gdl.date_manager.data;

import gdl.date_manager.model.ProductModel;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<ProductModel, Integer> {

    List<ProductModel> findByUserIdOrderByValidityAsc(Integer userId);
    List<ProductModel> findAllByOrderByValidityAsc();

    // Busca por nome (contém, case-insensitive) OU barcode exato
    @Query("""
           SELECT p FROM ProductModel p
           WHERE p.user.id = :userId
             AND (LOWER(p.name) LIKE LOWER(CONCAT('%', :q, '%'))
                  OR p.barcode = :q)
           ORDER BY p.validity ASC
           """)
    List<ProductModel> search(@Param("userId") Integer userId, @Param("q") String q);

    // Filtro por janela de meses (1 ou 2)
    @Query("""
           SELECT p FROM ProductModel p
           WHERE p.user.id = :userId
             AND p.validity BETWEEN :start AND :end
           ORDER BY p.validity ASC
           """)
    List<ProductModel> expiringBetween(@Param("userId") Integer userId,
                                       @Param("start") LocalDate start,
                                       @Param("end") LocalDate end);

    // Atualizar produtos cadastrados
    @Modifying
    @Transactional
    @Query("UPDATE ProductModel p SET p.name = :name, p.validity = :validity, p.barcode = :barcode WHERE p.id = :id AND p.user.id = :userId")
    int updateForOwner(@Param("name") String name,
                       @Param("validity") LocalDate validity,
                       @Param("barcode") String barcode,
                       @Param("id") Integer id,
                       @Param("userId") Integer userId);
}
