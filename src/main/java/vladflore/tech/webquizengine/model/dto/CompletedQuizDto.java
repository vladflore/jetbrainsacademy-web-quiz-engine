package vladflore.tech.webquizengine.model.dto;

import java.time.LocalDateTime;

public class CompletedQuizDto {
    private Integer id;
    private LocalDateTime completedAt;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDateTime getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(LocalDateTime completedAt) {
        this.completedAt = completedAt;
    }
}
