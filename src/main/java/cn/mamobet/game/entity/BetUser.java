package cn.mamobet.game.entity;

import cn.mamobet.game.common.BaseEntity;
import cn.mamobet.game.common.BaseEnum;
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
 * 用户实体类
 */
@Data
@TableName("bet_user")
public class BetUser extends BaseEntity {

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户名称
     */
    private String userName;

    /**
     * 用户余额
     */
    private BigDecimal balance;

    /**
     * 货币类型
     */
    private CurrencyType currencyType;

    /**
     * 货币类型
     */
    @Getter
    public enum CurrencyType implements BaseEnum {
        DOLLAR("1", "美元");
        @EnumValue
        @JsonValue
        private final String value;
        private final String desc;

        CurrencyType(String value, String desc) {
            this.value = value;
            this.desc = desc;
        }

        @JsonCreator
        public CurrencyType valOf(String value) {
            for (CurrencyType item : values()) {
                if (item.getValue().equals(value)) {
                    return item;
                }
            }
            return null;
        }

        public static CurrencyType toEnumObject(String value) {
            for (CurrencyType item : values()) {
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