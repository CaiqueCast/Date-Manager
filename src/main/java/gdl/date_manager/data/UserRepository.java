package gdl.date_manager.data;

import gdl.date_manager.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserModel, Integer> {
}
