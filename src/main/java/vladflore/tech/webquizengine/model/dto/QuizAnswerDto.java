package vladflore.tech.webquizengine.model.dto;

import javax.validation.constraints.NotNull;
import java.util.Arrays;

public class QuizAnswerDto {
    @NotNull
    private int[] answer;

    public int[] getAnswer() {
        return answer;
    }

    public void setAnswer(int[] answer) {
        this.answer = answer;
    }

    @Override
    public String toString() {
        return "QuizAnswerDto{" +
                "answer=" + Arrays.toString(answer) +
                '}';
    }
}




