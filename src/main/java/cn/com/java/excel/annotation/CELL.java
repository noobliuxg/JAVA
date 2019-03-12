package cn.com.java.excel.annotation;

import java.lang.annotation.*;

@Inherited
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface CELL {

    /**
     * 标题
     * @return
     */
    String title() default "";

    /**
     * 单元格下标
     * @return
     */
    int index() default 0;

    /**
     * 单元格数据类型
     * @return
     */
    CELLType type() default CELLType.STRING;

}
