package gdl.date_manager.data;

import gdl.date_manager.model.UserModel;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<UserModel, Integer> {

    Optional<UserModel> findByUserName(String userName);

    @Modifying
    @Transactional
    @Query("UPDATE UserModel u SET u.userName = :userName, u.password = :password WHERE u.id = :id")
    int update(@Param("userName") String userName,
               @Param("password") String password,
               @Param("id") Integer id);
}