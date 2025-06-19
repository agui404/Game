package cn.mamobet.game.service;

import cn.mamobet.game.entity.BetOrder;
import cn.mamobet.game.model.dto.BetOrderDTO;
import cn.mamobet.game.model.vo.BetOrderVO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface  BetOrderService extends IService<BetOrder> {
    /**
     * 创建投注单并开奖
     */
    BetOrderVO placeOrder(BetOrderDTO dto);

    /**
     * 根据ID查询
     */
    BetOrder getById(Long id);

    /**
     * 查询用户所有注单
     */
    List<BetOrder> getOrdersByUserId(Long userId);
}
