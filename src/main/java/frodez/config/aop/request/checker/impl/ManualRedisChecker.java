package frodez.config.aop.request.checker.impl;

import frodez.config.aop.request.checker.facade.ManualChecker;
import frodez.service.cache.base.BaseRedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 阻塞型重复请求检查REDIS实现
 * @author Frodez
 * @date 2019-01-21
 */
@Component("repeatRedisChecker")
public class ManualRedisChecker implements ManualChecker {

	/**
	 * redis服务
	 */
	@Autowired
	private BaseRedisCache redisService;

	@Override
	public boolean check(String key) {
		return redisService.exists(key);
	}

	@Override
	public void lock(String key) {
		redisService.set(key, true);
	}

	@Override
	public void free(String key) {
		redisService.delete(key);
	}

}
