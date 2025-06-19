package cn.mamobet.game.model.vo;

import cn.mamobet.game.common.CommonEnum;
import cn.mamobet.game.entity.BetOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * VO: 返回给前端的下注单详情
 */
@Data
@Builder
@Schema(description = "下注单返回 VO")
public class BetOrderVO {

    @Schema(description = "注单ID", example = "1")
    private Long orderId;

    @Schema(description = "用户ID", example = "12345")
    private Long userId;

    @Schema(description = "下注金额", example = "1.0001")
    private BigDecimal betAmount;

    @Schema(description = "派彩值", example = "1.0001")
    private BigDecimal payoutRatio;

    @Schema(description = "投注方式", example = "0")
    private BetOrder.BetType betType;

    @Schema(description = "实际返奖金额", example = "1.0002")
    private BigDecimal payoutAmount;

    @Schema(description = "开奖金额", example = "26.25")
    private BigDecimal resultNumber;

    @Schema(description = "下注状态（1：中奖，0：未中奖）", example = "1")
    private CommonEnum.YesOrNo isWin;

    @Schema(description = "下注时间", example = "2024-06-18T10:15:30")
    private LocalDateTime createTime;

    @Schema(description = "状态", example = "2024-06-18T10:15:30")
    private BetOrder.Status status;

}