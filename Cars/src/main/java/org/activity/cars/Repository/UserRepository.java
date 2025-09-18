package org.activity.cars.Repository;

import org.activity.cars.Model.User; // ✅ use your own entity
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
