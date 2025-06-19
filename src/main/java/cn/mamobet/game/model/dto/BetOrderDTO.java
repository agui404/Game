package cn.mamobet.game.model.dto;


import cn.mamobet.game.common.BaseEnum;
import cn.mamobet.game.entity.BetOrder;
import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * DTO: 前端下单时的参数对象
 */
@Data
@Schema(description = "下注请求参数 DTO")
@AllArgsConstructor
@NoArgsConstructor
public class BetOrderDTO {

    @Schema(description = "用户ID", example = "1")
    @NotNull(message = "用户ID不能为空")
    private Long userId;

    @Schema(description = "下注金额", example = "1.0001")
    @NotNull(message = "投注额不能为空")
    @Min(value = 0, message = "投注额必须大于0")
    private BigDecimal betAmount;

    @Schema(description = "派彩值", example = "1.0102")
    @NotNull(message = "派彩值不能为空")
    private BigDecimal payoutRatio;

    @Schema(description = "投注方式（0：手动/1:自动）", example = "0")
    @NotNull(message = "投注方式不能为空")
    private BetOrder.BetType betType;

    @Schema(description = "投注条件（0：大于/1:小于）", example = "0")
    @NotNull(message = "投注条件不能为空")
    private BetCondition betCondition;


    /**
     * 投注条件
     */
    @Getter
    public enum BetCondition implements BaseEnum {
        OVER("0", "大于"),
        UNDER("1", "小于");
        @EnumValue
        @JsonValue
        private final String value;
        private final String desc;

        BetCondition(String value, String desc) {
            this.value = value;
            this.desc = desc;
        }

        @JsonCreator
        public BetCondition valOf(String value) {
            for (BetCondition item : values()) {
                if (item.getValue().equals(value)) {
                    return item;
                }
            }
            return null;
        }

        public static BetCondition toEnumObject(String value) {
            for (BetCondition item : values()) {
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
