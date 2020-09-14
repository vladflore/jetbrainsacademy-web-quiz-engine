package vladflore.tech.webquizengine.model.mapper;


import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import vladflore.tech.webquizengine.model.dto.CompletedQuizDto;
import vladflore.tech.webquizengine.model.dto.QuizDto;
import vladflore.tech.webquizengine.model.entity.CompletedQuiz;
import vladflore.tech.webquizengine.model.entity.Option;
import vladflore.tech.webquizengine.model.entity.Quiz;

import java.util.stream.Collectors;

@Component
public class QuizMapper {
    public Quiz toEntity(QuizDto quizDto) {
        Quiz quiz = new Quiz();
        quiz.setTitle(quizDto.getTitle());
        quiz.setText(quizDto.getText());
        quiz.getOptions().addAll(quizDto.getOptions().stream().map(opt -> {
            Option option = new Option();
            option.setName(opt);
            return option;
        }).collect(Collectors.toList()));
        if (!CollectionUtils.isEmpty(quizDto.getAnswer())) {
            quiz.setAnswer(quizDto.getAnswer().stream().map(String::valueOf).collect(Collectors.joining(",")));
        } else {
            quiz.setAnswer("");
        }

        return quiz;
    }

    public QuizDto toDto(Quiz entity) {
        QuizDto quizDto = new QuizDto();
        quizDto.setId(entity.getId());
        quizDto.setTitle(entity.getTitle());
        quizDto.setText(entity.getText());
        quizDto.setOptions(entity.getOptions().stream().map(Option::getName).collect(Collectors.toList()));

        return quizDto;
    }

    public CompletedQuizDto toDto(CompletedQuiz completedQuiz) {
        CompletedQuizDto completedQuizDto = new CompletedQuizDto();
        completedQuizDto.setId(completedQuiz.getQuizId());
        completedQuizDto.setCompletedAt(completedQuiz.getCompletedAt());

        return completedQuizDto;
    }
}
