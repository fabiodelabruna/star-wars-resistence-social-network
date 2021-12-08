package fabiodelabruna.starwars.resistence.socialnetwork.exceptionhandler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;
import java.util.stream.Collectors;

import fabiodelabruna.starwars.resistence.socialnetwork.service.exception.DifferentPointsToTradeException;
import fabiodelabruna.starwars.resistence.socialnetwork.service.exception.InsuficientItemsToTradeException;
import lombok.AllArgsConstructor;
import lombok.Getter;

@ControllerAdvice
public class AppExceptionHandler extends ResponseEntityExceptionHandler {

    @Autowired
    private MessageSource messageSource;

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(final MethodArgumentNotValidException ex,
                    final HttpHeaders headers, final HttpStatus status, final WebRequest request) {

        final List<Error> errors = createErrorList(ex.getBindingResult());
        return handleExceptionInternal(ex, errors, headers, HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler({ EmptyResultDataAccessException.class })
    public ResponseEntity<Object> handleEmptyResultDataAccessException(final EmptyResultDataAccessException ex,
                    final WebRequest request) {

        final String message = messageSource.getMessage("resource.not-found", null, LocaleContextHolder.getLocale());
        final List<Error> errors = List.of(new Error(message, ex.toString()));
        return handleExceptionInternal(ex, errors, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler({ DifferentPointsToTradeException.class })
    public ResponseEntity<Object> handleDifferentPointsToTradeException(final DifferentPointsToTradeException ex,
                    final WebRequest request) {

        final String message = messageSource.getMessage("different.points.to.trade", null, LocaleContextHolder.getLocale());
        final List<Error> errors = List.of(new Error(message, ex.toString()));
        return handleExceptionInternal(ex, errors, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler({ InsuficientItemsToTradeException.class })
    public ResponseEntity<Object> handleInsuficientItemsToTradeException(final InsuficientItemsToTradeException ex,
                    final WebRequest request) {

        final Object[] messageParams = { ex.getRebelName(), ex.getAmount(), ex.getItemName() };
        final String message = messageSource.getMessage("insuficient.items.to.trade", messageParams,
                        LocaleContextHolder.getLocale());

        final List<Error> errors = List.of(new Error(message, ex.toString()));
        return handleExceptionInternal(ex, errors, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    private List<Error> createErrorList(final BindingResult bindingResult) {
        return bindingResult.getFieldErrors().stream().map(this::createError).collect(Collectors.toList());
    }

    private Error createError(final FieldError fieldError) {
        return new Error(messageSource.getMessage(fieldError, LocaleContextHolder.getLocale()), fieldError.toString());
    }

    @Getter
    @AllArgsConstructor
    public static class Error {
        private String message;
        private String detail;
    }

}
