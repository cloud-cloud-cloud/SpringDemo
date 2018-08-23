package com.example.demo.cache;

import com.example.demo.entity.CallbackCfg;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HanXiong on 2017/8/21.
 */
@Component
public class CallbackConfigCache extends IncrementalCache<CallbackCfg> implements InitializingBean, DisposableBean {


	@Override
	public void destroy() throws Exception {
		stop();
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		start();
	}

	@Override
	protected List<CallbackCfg> doLoadData(long lastUpdateTime) {
		List<CallbackCfg> callbackCfgs = null;//todo  调用sql mapper查询

		List<CallbackCfg> result = new ArrayList<>();
		for (CallbackCfg c : callbackCfgs) {

			if (c.lastUpdateTime() > lastUpdateTime) {
				result.add(c);
			}
		}
		return result;
	}
}
