package cn.ictt.zhanghui.springboot_test.base.advice;

import cn.ictt.zhanghui.springboot_test.base.exception.BaseException;
import cn.ictt.zhanghui.springboot_test.base.exception.BusinessException;
import cn.ictt.zhanghui.springboot_test.base.exception.enums.ArgumentResponseEnum;
import cn.ictt.zhanghui.springboot_test.base.exception.enums.ServletResponseEnum;
import cn.ictt.zhanghui.springboot_test.base.exception.enums.SystemResponseEnum;
import cn.ictt.zhanghui.springboot_test.base.resp.ResponseInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;

@Slf4j
@ConditionalOnWebApplication
@RestControllerAdvice
public class SystemExceptionAdvice {
    /**
     * 生产环境
     */
    private final static String ENV_PROD = "prod";

    /**
     * 当前环境
     */
    @Value("${spring.profiles.active}")
    private String profile;


    /**
     * 业务异常
     *
     * @param e 异常
     * @return 异常结果
     */
    @ExceptionHandler(value = BusinessException.class)
    @ResponseBody
    @SuppressWarnings("unchecked")
    public ResponseInfo<String> handleBusinessException(BaseException e) {
        log.error(e.getMessage(), e);
        return ResponseInfo.FAIL(e.getResponseEnum().getCode(), getMessage(e));
    }

    /**
     * 自定义异常
     *
     * @param e 异常
     * @return 异常结果
     */
    @ExceptionHandler(value = BaseException.class)
    @ResponseBody
    public ResponseInfo handleBaseException(BaseException e) {
        log.error(e.getMessage(), e);

        return ResponseInfo.FAIL(e.getResponseEnum().getCode(), getMessage(e));
    }

    /**
     * Controller上一层相关异常
     *
     * @param e 异常
     * @return 异常结果
     */
    @ExceptionHandler({
            NoHandlerFoundException.class,
            HttpRequestMethodNotSupportedException.class,
            HttpMediaTypeNotSupportedException.class,
            HttpMediaTypeNotAcceptableException.class,
            MissingPathVariableException.class,
            MissingServletRequestParameterException.class,
            TypeMismatchException.class,
            HttpMessageNotReadableException.class,
            HttpMessageNotWritableException.class,
            ServletRequestBindingException.class,
            ConversionNotSupportedException.class,
            MissingServletRequestPartException.class,
            AsyncRequestTimeoutException.class
    })
    @ResponseBody
    public ResponseInfo handleServletException(Exception e) {
        log.error(e.getMessage(), e);
        String code = "";
        String message = "";

        // 当为生产环境, 不适合把具体的异常信息展示给用户, 比如404.
        if (ENV_PROD.equals(profile)) {
            code = SystemResponseEnum.SERVER_ERROR.getCode();
            BaseException baseException = new BaseException(SystemResponseEnum.SERVER_ERROR);
            message = getMessage(baseException);
        } else {
            try {
                ServletResponseEnum servletExceptionEnum = ServletResponseEnum.valueOf(e.getClass().getSimpleName());
                code = servletExceptionEnum.getCode();
            } catch (IllegalArgumentException e1) {
                log.error("class [{}] not defined in enum {}", e.getClass().getName(), ServletResponseEnum.class.getName());
            }
            message = e.getMessage();
        }

        return ResponseInfo.FAIL(code, message);
    }

    /**
     * 参数绑定异常
     *
     * @param e 异常
     * @return 异常结果
     */
    @ExceptionHandler({
            BindException.class,
            MethodArgumentNotValidException.class
    })
    @ResponseBody
    public ResponseInfo handleBindException(Exception e) {
        log.error("参数绑定校验异常", e);

        StringBuilder msg = new StringBuilder();
        BindingResult bindingResult;
        if(e instanceof BindException){
            bindingResult = ((BindException)e).getBindingResult();
        }else{
            bindingResult = ((MethodArgumentNotValidException) e).getBindingResult();
        }

        for (ObjectError error : bindingResult.getAllErrors()) {
            msg.append(", ");
            if (error instanceof FieldError) {
                msg.append(((FieldError) error).getField()).append(": ");
            }
            msg.append(error.getDefaultMessage() == null ? "" : error.getDefaultMessage());
        }

        return ResponseInfo.FAIL(ArgumentResponseEnum.VALID_ERROR.getCode(), msg.substring(2));
    }

    /**
     * 未定义异常
     *
     * @param e 异常
     * @return 异常结果
     */
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public ResponseInfo handleException(Exception e) {
        log.error(e.getMessage(), e);
        String code = SystemResponseEnum.SERVER_ERROR.getCode();
        String message = "";
        // 当为生产环境, 不适合把具体的异常信息展示给用户, 比如数据库异常信息.
        if (ENV_PROD.equals(profile)) {
            BaseException baseException = new BaseException(SystemResponseEnum.SERVER_ERROR);
            message = getMessage(baseException);
        } else {
            message = e.getMessage();
        }

        return ResponseInfo.FAIL(code, message);
    }

    /**
     * 根据 异常 返回 异常信息
     * <p>
     * 可以加入国际化处理
     *
     * @param e 异常信息
     * @return
     */
    private String getMessage(BaseException e) {
        return e.getMessage();
    }

}