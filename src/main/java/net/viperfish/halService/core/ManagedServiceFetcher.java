package net.viperfish.halService.core;

import java.util.concurrent.Future;
import net.viperfish.crawler.html.engine.PrioritizedConcurrentHttpFetcher;
import org.springframework.core.task.AsyncTaskExecutor;

public class ManagedServiceFetcher extends PrioritizedConcurrentHttpFetcher {

	private AsyncTaskExecutor fetcherPool;

	public ManagedServiceFetcher(String userAgent, AsyncTaskExecutor executor) {
		super(userAgent);
		this.fetcherPool = executor;
	}

	@Override
	protected Future<?> runDelegator(Runnable delegator) {
		return fetcherPool.submit(delegator);
	}

	@Override
	protected Future<?> runFetcher(Runnable fetcher) {
		return fetcherPool.submit(fetcher);
	}

	@Override
	protected void cleanup() {

	}

	@Override
	public boolean isEndReached() {
		return isClosed();
	}

	@Override
	public boolean isClosed() {
		return closeCalled();
	}

}
