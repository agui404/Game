package cn.mamobet.game.entity;

import cn.mamobet.game.common.BaseEntity;
import cn.mamobet.game.common.BaseEnum;
import cn.mamobet.game.common.CommonEnum;
import com.baomidou.mybatisplus.annotation.EnumValue;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Data;
import lombok.Getter;

import java.math.BigDecimal;

/**
 * 注单实体类
 */
@Data
@TableName("bet_order")
public class BetOrder extends BaseEntity {

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 投注金额
     */
    private BigDecimal betAmount;

    /**
     * 派彩值（倍率）
     */
    private BigDecimal payoutRatio;

    /**
     * 投注方式（手动/自动）
     */
    private BetType betType;

    /**
     * 开奖数值
     */
    private BigDecimal resultNumber;

    /**
     * 是否中奖（1=中奖，0=未中奖）
     */
    private CommonEnum.YesOrNo isWin;

    /**
     * 返奖金额
     */
    private BigDecimal payoutAmount;

    /**
     * 状态（如：COMPLETED）
     */
    private Status status;


    /**
     * 投注方式
     */
    @Getter
    public enum BetType implements BaseEnum {
        HAND_MOVEMENT("0", "手动"), AUTOMATIC("1", "自动");
        @EnumValue
        @JsonValue
        private final String value;
        private final String desc;

        BetType(String value, String desc) {
            this.value = value;
            this.desc = desc;
        }

        @JsonCreator
        public BetType valOf(String value) {
            for (BetType item : values()) {
                if (item.getValue().equals(value)) {
                    return item;
                }
            }
            return null;
        }

        public static BetType toEnumObject(String value) {
            for (BetType item : values()) {
                if (item.getValue().equals(value)) {
                    return item;
                }
            }
            return null;
        }

        @Override
        public String value() {
            return value;
        }

        @Override
        public String desc() {
            return desc;
        }
    }


    /**
     * 订单状态
     */
    @Getter
    public enum Status implements BaseEnum {
        FAIL("0", "失败"),
        COMPLETED("1", "完成");
        @EnumValue
        @JsonValue
        private final String value;
        private final String desc;

        Status(String value, String desc) {
            this.value = value;
            this.desc = desc;
        }

        @JsonCreator
        public Status valOf(String value) {
            for (Status item : values()) {
                if (item.getValue().equals(value)) {
                    return item;
                }
            }
            return null;
        }

        public static Status toEnumObject(String value) {
            for (Status item : values()) {
                if (item.getValue().equals(value)) {
                    return item;
                }
            }
            return null;
        }

        @Override
        public String value() {
            return value;
        }

        @Override
        public String desc() {
            return desc;
        }
    }
}
