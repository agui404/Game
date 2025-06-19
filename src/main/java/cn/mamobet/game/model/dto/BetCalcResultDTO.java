package cn.mamobet.game.model.dto;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 派彩 & 几率 & 掷数 计算结果
 */
@Data
@Builder
public class BetCalcResultDTO  implements Serializable {

    /**
     * 派彩值
     */
    private BigDecimal payoutRatio;

    /**
     * 获胜几率（%）
     */
    private BigDecimal winRate;

    /**
     * 掷小于
     */
    private BigDecimal rollUnder;

    /**
     * 掷大于
     */
    private BigDecimal rollOver;

}