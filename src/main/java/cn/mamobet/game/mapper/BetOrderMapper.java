package cn.mamobet.game.mapper;

import cn.mamobet.game.entity.BetOrder;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

public interface BetOrderMapper extends BaseMapper<BetOrder> {
    /**
     * 查询用户的订单
     * @param userId
     * @return 用户Id
     */
    List<BetOrder> selectByUserId(Long userId);
}