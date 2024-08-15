package sg.nus.iss.adproject.common.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import sg.nus.iss.adproject.entities.Result;

/**
 * 全局处理异常 Handling exceptions globally
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public Result<Void> Exception(Exception e) {
        return Result.error(e.getMessage());
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public Result<Void> MethodArgumentNotValidException(MethodArgumentNotValidException e) {
        BindingResult bindingResult = e.getBindingResult();
        ObjectError error = bindingResult.getAllErrors().get(0);
        return Result.error(error.getDefaultMessage());
    }

    @ExceptionHandler(value = RuntimeException.class)
    public Result<Void> RuntimeException(RuntimeException e) {
        return Result.error(e.getMessage());
    }

    @ExceptionHandler(value = NullPointerException.class)
    public Result<Void> NullPointerException(NullPointerException e) {
        return Result.error(e.getMessage());
    }

    @ExceptionHandler(value = IllegalArgumentException.class)
    public Result<Void> IllegalArgumentException(IllegalArgumentException e) {
        return Result.error(e.getMessage());
    }
}
