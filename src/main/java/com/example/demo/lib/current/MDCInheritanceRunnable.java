package com.example.demo.lib.current;

import org.slf4j.MDC;

import java.util.Map;

public class MDCInheritanceRunnable implements Runnable {

	private Map<String, String> copyOfContextMap;

	private Runnable work;

	public MDCInheritanceRunnable(Runnable r) {
		this.copyOfContextMap = MDC.getCopyOfContextMap();
		this.work = r;
	}

	@Override
	public void run() {
		MDC.clear();
		if (copyOfContextMap != null) {
			MDC.setContextMap(copyOfContextMap);
		}

		try {
			work.run();
		} finally {
			MDC.clear();
		}
	}
}
