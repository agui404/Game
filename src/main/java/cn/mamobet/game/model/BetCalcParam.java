package cn.mamobet.game.model;

import cn.mamobet.game.common.CommonEnum;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class BetCalcParam {
    /**
     * 计算模式
     * - BY_PAYOUT：根据派彩值算
     * - BY_WIN_RATE：根据获胜几率算
     */
    private CommonEnum.CalcMode calcMode;

    /**
     * 数值
     * 例如：如果 mode = BY_PAYOUT，则 value = 派彩值
     * 如果 mode = BY_WIN_RATE，则 value = 获胜几率
     */
    private BigDecimal value;
}

