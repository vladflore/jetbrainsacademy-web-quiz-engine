package vladflore.tech.webquizengine.repo;


import org.springframework.data.jpa.repository.JpaRepository;
import vladflore.tech.webquizengine.model.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
}
