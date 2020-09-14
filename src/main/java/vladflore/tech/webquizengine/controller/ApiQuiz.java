package vladflore.tech.webquizengine.controller;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import vladflore.tech.webquizengine.model.dto.*;
import vladflore.tech.webquizengine.model.entity.CompletedQuiz;
import vladflore.tech.webquizengine.model.entity.Quiz;
import vladflore.tech.webquizengine.model.entity.User;
import vladflore.tech.webquizengine.model.mapper.QuizMapper;
import vladflore.tech.webquizengine.model.mapper.UserMapper;
import vladflore.tech.webquizengine.repo.CompletedQuizRepository;
import vladflore.tech.webquizengine.repo.QuizRepository;
import vladflore.tech.webquizengine.repo.UserRepository;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
class ApiQuiz {

    private final QuizRepository quizRepository;
    private final UserRepository userRepository;
    private final CompletedQuizRepository completedQuizRepository;

    private final QuizMapper quizMapper;
    private final UserMapper userMapper;

    ApiQuiz(QuizRepository quizRepository, UserRepository userRepository, CompletedQuizRepository completedQuizRepository, QuizMapper quizMapper, UserMapper userMapper) {
        this.quizRepository = quizRepository;
        this.userRepository = userRepository;
        this.completedQuizRepository = completedQuizRepository;

        this.quizMapper = quizMapper;
        this.userMapper = userMapper;
    }

    @DeleteMapping("/api/quizzes/{id}")
    ResponseEntity<Void> deleteQuiz(@PathVariable Integer id) {
        Optional<Quiz> quiz = quizRepository.findById(id);
        if (quiz.isPresent()) {
            String currentUser = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
            if (quiz.get().getUser().getEmail().equalsIgnoreCase(currentUser)) {
                quizRepository.deleteById(id);
                return ResponseEntity.noContent().build();
            } else {
                throw new DeleteQuizForbiddenException();
            }
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/api/register")
    ResponseEntity<UserDto> registerUser(@RequestBody @Valid UserDto userDto) {
        User userByEmail = userRepository.findByEmail(userDto.getEmail());
        if (userByEmail != null) {
            return ResponseEntity.badRequest().build();
        }
        User user = userMapper.toEntity(userDto);
        User savedEntity = userRepository.save(user);
        return ResponseEntity.ok(userMapper.toDto(savedEntity));
    }

    @PostMapping("/api/quizzes")
    ResponseEntity<QuizDto> createQuiz(@RequestBody @Valid  QuizDto quizDto) {
        String currentUserEmail = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        User creator = userRepository.findByEmail(currentUserEmail);
        Quiz entity = quizMapper.toEntity(quizDto);
        entity.setUser(creator);
        Quiz savedEntity = quizRepository.save(entity);
        return ResponseEntity.ok(quizMapper.toDto(savedEntity));
    }

    @GetMapping("/api/quizzes/{id}")
    ResponseEntity<QuizDto> getQuiz(@PathVariable Integer id) {
        Optional<Quiz> quiz = quizRepository.findById(id);
        // TODO figure it out when mapping returns null
        return quiz.map(quizMapper::toDto).map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/api/quizzes")
    ResponseEntity<Page<QuizDto>> getAllQuizzes(@RequestParam(name = "page", defaultValue = "0") Integer pageNo) {
        PageRequest pageRequest = PageRequest.of(pageNo, 10);
        Page<Quiz> page = quizRepository.findAll(pageRequest);
        PageImpl<QuizDto> dtoPage = new PageImpl<>(page.getContent().stream().map(quizMapper::toDto).collect(Collectors.toList()), pageRequest, page.getTotalElements());
        return ResponseEntity.ok(dtoPage);
    }

    @PostMapping("/api/quizzes/{id}/solve")
    ResponseEntity<SolvedQuizDto> solveQuiz(@PathVariable(name = "id") Integer quizId, @RequestBody QuizAnswerDto quizAnswerDto) {
        Optional<Quiz> quiz = quizRepository.findById(quizId);
        String currentUserEmail = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        User user = userRepository.findByEmail(currentUserEmail);
        Arrays.sort(quizAnswerDto.getAnswer());
        return quiz.map(q -> {
            SolvedQuizDto solvedQuizDto = new SolvedQuizDto();
            int[] dbQuizAnswer = StringUtils.isEmpty(q.getAnswer()) ? new int[0] : Arrays.stream(q.getAnswer().split(",")).mapToInt(Integer::parseInt).toArray();
            Arrays.sort(dbQuizAnswer);
            if (Arrays.equals(quizAnswerDto.getAnswer(), dbQuizAnswer)) {
                CompletedQuiz completedQuiz = new CompletedQuiz();
                completedQuiz.setCompletedAt(LocalDateTime.now());
                completedQuiz.setQuizId(q.getId());
                completedQuiz.setCompletedByUserId(user.getId());
                completedQuizRepository.save(completedQuiz);

                solvedQuizDto.setSuccess(true);
                solvedQuizDto.setFeedback("Congratulations, you're right!");
                return ResponseEntity.ok(solvedQuizDto);
            }
            solvedQuizDto.setSuccess(false);
            solvedQuizDto.setFeedback("Wrong answer! Please, try again.");
            return ResponseEntity.ok(solvedQuizDto);
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/api/quizzes/completed")
    ResponseEntity<Page<CompletedQuizDto>> getCompletedQuizzes(@RequestParam(name = "page", defaultValue = "0") Integer pageNo) {
        String currentUserEmail = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        User user = userRepository.findByEmail(currentUserEmail);
        PageRequest pageRequest = PageRequest.of(pageNo, 10, Sort.by("completedAt").descending());
        Page<CompletedQuiz> allCompletedQuizzesForUser = completedQuizRepository.findAllCompletedQuizzesForUser(user.getId(), pageRequest);

        PageImpl<CompletedQuizDto> dtoPage = new PageImpl<>(
                allCompletedQuizzesForUser
                        .getContent()
                        .stream()
                        .map(quizMapper::toDto)
                        .collect(Collectors.toList()), pageRequest, allCompletedQuizzesForUser.getTotalElements()
        );
        return ResponseEntity.ok(dtoPage);
    }
}
