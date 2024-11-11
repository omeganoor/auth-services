package com.deloitte.demo.user.controller.advice;

import com.deloitte.demo.user.controller.dto.ResponseDto;
import com.deloitte.demo.user.domain.exception.DuplicateAccountException;
import com.deloitte.demo.user.domain.exception.UserNotFound;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;

@ControllerAdvice
@Slf4j
public class GlobalErrorHandler {

    @ExceptionHandler(value = {NullPointerException.class, IllegalArgumentException.class})
    public ResponseEntity<?> nullPointerException(Exception exception) {
        log.error(exception.getMessage(), exception);
        String msgErr = "missing required data";
        return new ResponseEntity<>(ResponseDto.error(400, msgErr), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    public ResponseEntity<?> notValidArgument(MethodArgumentNotValidException exception) {
        log.error(exception.getMessage(), exception);
        String msg = exception.getFieldError().getDefaultMessage();
        String msgErr = Strings.isNotBlank(msg) ? msg : String.format("%s is required", exception.getFieldError().getField());

        return new ResponseEntity<>(ResponseDto.error(400, msgErr), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {DuplicateAccountException.class})
    public ResponseEntity<?> duplicateAccountException (Exception exception) {
        log.error(exception.getMessage(), exception);
        String msgErr = exception.getMessage();
        return new ResponseEntity<>(ResponseDto.error(409, msgErr), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = {UserNotFound.class})
    public ResponseEntity<?> notFound(Exception exception) {
        log.error(exception.getMessage(), exception);
        String msgErr = exception.getMessage();

        return new ResponseEntity<>(ResponseDto.error(404, msgErr), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<?> exception(Exception exception) {
        log.error(exception.getMessage(), exception);
        String msgErr = HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase();

        return new ResponseEntity<>(ResponseDto.error(500, msgErr), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = {HttpClientErrorException.Unauthorized.class})
    public ResponseEntity<?> unauthorizedException (Exception exception) {
        log.error(exception.getMessage(), exception);
        String msgErr = exception.getMessage();

        return new ResponseEntity<>(ResponseDto.error(HttpStatus.FORBIDDEN.value(), msgErr), HttpStatus.FORBIDDEN);
    }
//
//    @ExceptionHandler(value = {})
//    public ResponseEntity<?> unauthorized (Exception exception) {
//        log.error(exception.getMessage(), exception);
//        String msgErr = exception.getMessage();
//
//        return new ResponseEntity<>(ResponseDto.error(401, msgErr), HttpStatus.UNAUTHORIZED);
//    }
}