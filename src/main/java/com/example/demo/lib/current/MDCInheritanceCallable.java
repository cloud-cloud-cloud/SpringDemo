package com.example.demo.lib.current;

import org.slf4j.MDC;

import java.util.Map;
import java.util.concurrent.Callable;

public class MDCInheritanceCallable<V> implements Callable<V> {

	private Map<String, String> copyOfContextMap;

	private Callable<V> work;

	public MDCInheritanceCallable(Callable<V> r) {
		this.copyOfContextMap = MDC.getCopyOfContextMap();
		this.work = r;
	}

	@Override
	public V call() throws Exception {
		MDC.clear();
		MDC.setContextMap(copyOfContextMap);

		try {
			return work.call();
		} finally {
			MDC.clear();
		}
	}
}
