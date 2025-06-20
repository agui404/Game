package cn.mamobet.game.controller;

import cn.mamobet.game.aop.RateLimit;
import cn.mamobet.game.common.R;
import cn.mamobet.game.entity.BetOrder;
import cn.mamobet.game.entity.BetUser;
import cn.mamobet.game.model.dto.BetOrderDTO;
import cn.mamobet.game.model.vo.BetOrderVO;
import cn.mamobet.game.service.BetOrderService;
import cn.mamobet.game.service.BetUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 入口
 */
@RestController
@RequestMapping("/api/game")
@Tag(name = "游戏接口", description = "提供余额、下注、查询等功能")
@RequiredArgsConstructor
public class GameController {

    private final BetUserService betUserService;
    private final BetOrderService betOrderService;

    /**
     * 查询余额
     */
    @RateLimit(limit = 2,time = 1)
    @Operation(summary = "查询余额")
    @GetMapping("/balance/{userId}")
    public R<BetUser> getBalance(@PathVariable Long userId) {
        return R.ok(betUserService.getBalance(userId));
    }

    /**
     * 下单投注
     */
    @Operation(summary = "下注")
    @PostMapping("/bet")
    public R<BetOrderVO> placeOrder(@RequestBody @Validated BetOrderDTO dto) {
        BetOrderVO vo = betOrderService.placeOrder(dto);
        return R.ok(vo);
    }

    /**
     * 查询单个注单
     */
    @Operation(summary = "查询单个注单")
    @GetMapping("/order/{orderId}")
    public R<BetOrder> getOrder(@PathVariable Long orderId) {
        return R.ok(betOrderService.getById(orderId));
    }

    /**
     * 查询某个用户的所有注单
     */
    @Operation(summary = "查询用户所有注单")
    @GetMapping("/orders/{userId}")
    public R<List<BetOrder>> getOrdersByUser(@PathVariable Long userId) {
        return R.ok(betOrderService.getOrdersByUserId(userId));
    }
}