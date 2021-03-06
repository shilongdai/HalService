package net.viperfish.halService;

import java.util.UUID;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;

public class PreSescLoggingFilter implements Filter {

	private Logger logger;

	public PreSescLoggingFilter() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) {
		String id = UUID.randomUUID().toString();
		ThreadContext.put("id", id);
		try {
			HttpServletResponse resp = (HttpServletResponse) response;
			resp.setHeader("Log-ID", id);
			chain.doFilter(request, response);
		} catch (Throwable e) {
			logger.error("error", e);
		} finally {
			ThreadContext.remove("id");
		}

	}

	@Override
	public void init(FilterConfig filterConfig) {
		logger = LogManager.getLogger();
	}

}
