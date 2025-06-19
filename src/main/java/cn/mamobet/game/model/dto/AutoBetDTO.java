package cn.mamobet.game.model.dto;

import cn.mamobet.game.common.CommonEnum;
import cn.mamobet.game.entity.BetOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 自动投注 DTO
 */
@Data
@Schema(description = "自动投注参数")
public class AutoBetDTO {

    @Schema(description = "用户ID", example = "2")
    @NotNull(message = "用户ID不能为空")
    private Long userId;

    @Schema(description = "派彩值", example = "1.1150")
    @NotNull(message = "派彩值不能为空")
    @DecimalMin(value = "1.0102", message = "派彩值不能小于 1.0102")
    private BigDecimal payoutRatio;

    @Schema(description = "初始投注额", example = "0.0001")
    @NotNull(message = "初始投注额不能为空")
    @DecimalMin(value = "0.0001", message = "初始投注额不能小于 0.0001")
    private BigDecimal betAmount;

    @Schema(description = "投注条件（大于/小于）", example = "0")
    @NotNull(message = "投注条件不能为空")
    private BetOrderDTO.BetCondition betCondition;

    @Schema(description = "投注次数，0表示无限次", example = "100")
    @NotNull(message = "投注次数不能为空")
    @Min(value = 0, message = "投注次数不能为负数")
    private Integer betCount;

    @Schema(description = "止盈金额，达到后停止自动投注", example = "10.00")
    @NotNull(message = "止盈金额不能为空")
    private BigDecimal stopProfit;

    @Schema(description = "止损金额，达到后停止自动投注", example = "5.00")
    @NotNull(message = "止损金额不能为空")
    private BigDecimal stopLoss;

    @Schema(description = "赢后递增百分比，如0.05表示+5%", example = "0.05")
    @NotNull(message = "赢后递增百分比不能为空")
    @DecimalMin(value = "0.0001", message = "赢后递增百分比最小值为 0.0001")
    private BigDecimal winIncrement;

    @Schema(description = "输后递增百分比，如0.10表示+10%", example = "0.10")
    @NotNull(message = "输后递增百分比不能为空")
    @DecimalMin(value = "0.0001", message = "输后递增百分比最小值为 0.0001")
    private BigDecimal lossIncrement;

    @Schema(description = "赢了是否重置 0=否，1=重置", example = "0")
    private CommonEnum.YesOrNo winReset;

    @Schema(description = "输了是否重置 0=否，1=重置", example = "0")
    private CommonEnum.YesOrNo lossReset;

    /**
     * 转换为单局下注 DTO
     *
     * @param currentBetAmount 当前局投注金额（可能动态递增）
     */
    public BetOrderDTO toBetOrderDTO(BigDecimal currentBetAmount) {
        BetOrderDTO dto = new BetOrderDTO();
        dto.setUserId(this.userId);
        dto.setPayoutRatio(this.payoutRatio);
        dto.setBetAmount(currentBetAmount);
        dto.setBetCondition(this.betCondition);
        dto.setBetType(BetOrder.BetType.AUTOMATIC);
        return dto;
    }
}

