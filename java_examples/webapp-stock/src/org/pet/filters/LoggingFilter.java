package org.pet.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;

@WebFilter("/*") // this filter will get invoked on every request
// @WebFilter("/car")
public class LoggingFilter implements Filter {

	private ServletContext context;

	@Override
	public void destroy() {
	}

	// invoked at each request
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		System.out.println("LoggingFilter invoked");

		HttpServletRequest req = (HttpServletRequest) request;
		String uri = req.getRequestURI();
		this.context.log("Requested Resource::" + uri);

		// pass the request along the filter chain
		chain.doFilter(request, response);
	}

	// invoked only once
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		this.context = filterConfig.getServletContext();
	}
}
