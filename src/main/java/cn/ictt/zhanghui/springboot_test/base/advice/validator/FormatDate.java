package cn.ictt.zhanghui.springboot_test.base.advice.validator;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Description:
 *
 * @author createdBy huizhang43.
 * @date createdAt 2021/11/9 15:01
 **/
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = FormatDate.DateTimeRangeValidator.class)
public @interface FormatDate {

    /**
     * 作用于字符串时，指定的格式，包含年月日时分秒
     */
    String pattern() default "yyyy-MM-dd";

    /**
     * 错误提示
     */
    String message() default "请求入参有误";

    /**
     * 用于分组校验
     */
    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    class DateTimeRangeValidator implements ConstraintValidator<FormatDate, Object> {

        private FormatDate dateTimeRange;

        @Override
        public void initialize(FormatDate dateRange) {
            this.dateTimeRange = dateRange;
        }

        @Override
        public boolean isValid(Object value, ConstraintValidatorContext context) {
            if (value == null) {
                return false;
            }
            return getByValue(value) != null;
//            LocalDateTime now = LocalDateTime.now();
//            return !ta.isAfter(now.toLocalDate());
        }

        private LocalDate getByValue(Object value) {
            if (value instanceof LocalDateTime) {
                return (LocalDate) value;
            }
            if (value instanceof String) {
                try {
                    return LocalDate.parse((String) value, DateTimeFormatter.ofPattern(dateTimeRange.pattern()));
                } catch (Exception e) {
                    return null;
                }
            }
            return null;
        }
    }
}