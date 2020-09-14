package vladflore.tech.webquizengine.repo;

import org.springframework.data.repository.PagingAndSortingRepository;
import vladflore.tech.webquizengine.model.entity.Quiz;

public interface QuizRepository extends PagingAndSortingRepository<Quiz, Integer> {


}

