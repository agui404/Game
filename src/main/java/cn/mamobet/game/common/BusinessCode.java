package cn.mamobet.game.common;

import lombok.Getter;

@Getter
public enum BusinessCode {
    SUCCESS(0, "成功"),
    FAIL(1, "失败"),
    UNKNOWN_ERROR(5001, "未知错误，请联系管理员"),
    TOO_MANY_REQUESTS(5002, "请求过于频繁"),
    BALANCE_NOT_ENOUGH(1001, "余额不足"),
    USER_NOT_FOUND(1002, "用户不存在"),
    INVALID_PAYOUT(1003, "派彩值非法"),
    INVALID_PROBABILITY(1004, "获胜概率非法"),
    BET_AMOUNT_TOO_LOW(1005, "投注金额过低"),
    BET_AMOUNT_TOO_HIGH(1006, "投注金额超过余额"),
    AUTO_BET_TASK_ERROR(1007, "自动投注任务失败"),
    EXIST_AUTO_BET_TASK(1008, "已存在自动投注任务"),
    ;

    private final int code;
    private final String message;

    BusinessCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * 根据 code 获取枚举
     */
    public static BusinessCode fromCode(int code) {
        for (BusinessCode bc : BusinessCode.values()) {
            if (bc.getCode() == code) {
                return bc;
            }
        }
        return UNKNOWN_ERROR;
    }

    /**
     * 根据 code 获取 message
     */
    public static String getMessageByCode(int code) {
        return fromCode(code).getMessage();
    }
}