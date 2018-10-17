package net.viperfish.halService.core;

import javax.annotation.PreDestroy;
import net.viperfish.crawler.html.engine.ServiceConcurrentHttpFetcher;

public class ManagedServiceFetcher extends ServiceConcurrentHttpFetcher {

	public ManagedServiceFetcher(int threadCount, String userAgent) {
		super(threadCount, userAgent);
	}

	public ManagedServiceFetcher(int threadCount) {
		super(threadCount);
	}

	@PreDestroy
	@Override
	public void close() {
		super.close();
	}
}
