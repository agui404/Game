package cn.mamobet.game.controller;

import cn.mamobet.game.common.R;
import cn.mamobet.game.model.dto.AutoBetDTO;
import cn.mamobet.game.service.task.AutoBetResultHolder;
import cn.mamobet.game.service.task.AutoBetTaskManager;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "自动投注")
@RestController
@RequestMapping("/api/auto-bet")
@RequiredArgsConstructor
public class AutoBetController {

    private final AutoBetTaskManager taskManager;
    private final AutoBetResultHolder resultHolder;

    @Operation(summary = "启动自动投注")
    @PostMapping("/start")
    public R<?> start(@RequestBody AutoBetDTO dto) {
        taskManager.startTask(dto);
        return R.ok("开启自动投注");
    }

    @Operation(summary = "停止自动投注")
    @PostMapping("/stop/{userId}")
    public R<?> stop(@PathVariable Long userId) {
        taskManager.stopTask(userId);
        return R.ok("停止自动投注");
    }

    @Operation(summary = "获取进度")
    @GetMapping("/progress/{userId}")
    public R<?> progress(@PathVariable Long userId) {
        AutoBetResultHolder.AutoBetSession session = resultHolder.getSession(userId);
        if (session == null) {
            return R.failed("没有进行中的自动投注");
        }
        return R.ok(session);
    }
}

