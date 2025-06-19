package cn.mamobet.game.service.impl;

import cn.mamobet.game.entity.BetUser;
import cn.mamobet.game.mapper.BetUserMapper;
import cn.mamobet.game.service.BetUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BetUserServiceImpl extends ServiceImpl<BetUserMapper, BetUser> implements BetUserService {


    /**
     * 获取余额
     *
     * @param userId 用户id
     * @return
     */
    @Override
    public BetUser getBalance(Long userId) {
        BetUser betUser = baseMapper.selectById(userId);
        return betUser;
    }
}