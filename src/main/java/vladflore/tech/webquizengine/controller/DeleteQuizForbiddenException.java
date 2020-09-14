package vladflore.tech.webquizengine.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class DeleteQuizForbiddenException extends RuntimeException {
}
