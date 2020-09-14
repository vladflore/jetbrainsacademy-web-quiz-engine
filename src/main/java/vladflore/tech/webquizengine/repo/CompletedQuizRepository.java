package vladflore.tech.webquizengine.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import vladflore.tech.webquizengine.model.entity.CompletedQuiz;

public interface CompletedQuizRepository extends PagingAndSortingRepository<CompletedQuiz, Integer> {

    @Query(value = "select cq from CompletedQuiz cq where cq.completedByUserId = ?1")
    Page<CompletedQuiz> findAllCompletedQuizzesForUser(Long userId, Pageable pageRequest);
}
