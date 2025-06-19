package cn.mamobet.game.exception;

import cn.mamobet.game.common.BusinessCode;

/**
 * 自定义业务异常
 * 用于明确区分系统异常与业务异常
 */
public class BusinessException extends RuntimeException {

    /**
     * 错误码，可选
     */
    private final Integer code;

    /**
     * 错误提示信息
     */
    private final String message;


    public BusinessException(BusinessCode businessCode) {
        super(businessCode.getMessage());
        this.message = businessCode.getMessage();
        this.code = businessCode.getCode();
    }


    public BusinessException(String message) {
        super(message);
        this.message = message;
        this.code = 400; // 默认可自定义，如 400 表示业务错误
    }

    public BusinessException(Integer code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}