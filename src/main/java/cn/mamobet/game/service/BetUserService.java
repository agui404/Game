package cn.mamobet.game.service;

import cn.mamobet.game.entity.BetUser;
import com.baomidou.mybatisplus.extension.service.IService;

public interface BetUserService extends IService<BetUser> {
    /**
     * 获取余额
     *
     * @param userId 用户id
     * @return
     */
    BetUser getBalance(Long userId);
}