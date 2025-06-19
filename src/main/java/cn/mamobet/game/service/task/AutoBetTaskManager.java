package cn.mamobet.game.service.task;

import cn.mamobet.game.common.BusinessCode;
import cn.mamobet.game.exception.BusinessException;
import cn.mamobet.game.model.dto.AutoBetDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;

/**
 * 自动投注任务管理器
 */
@Component
@RequiredArgsConstructor
public class AutoBetTaskManager {

    private final AutoBetExecutor executor;

    private final ConcurrentHashMap<Long, Future<?>> taskMap = new ConcurrentHashMap<>();

    /**
     * 启动任务
     */
    public void startTask(AutoBetDTO autoBetDTO) {
        Long userId = autoBetDTO.getUserId();
        if (taskMap.containsKey(userId)) {
            throw new BusinessException(BusinessCode.EXIST_AUTO_BET_TASK);
        }
        Future<?> future = executor.start(autoBetDTO,()-> removeTask(userId));
        taskMap.put(userId, future);
    }

    /**
     * 停止任务
     */
    public void stopTask(Long userId) {
        Future<?> future = taskMap.remove(userId);
        if (future != null) {
            future.cancel(true);
        }
    }

    /**
     *
     * @param userId
     */
    public void removeTask(Long userId) {
        taskMap.remove(userId);
    }

    /**
     * 检查是否存在
     */
    public boolean isRunning(Long userId) {
        return taskMap.containsKey(userId);
    }
}
