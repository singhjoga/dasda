package net.devoat.common.context;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import net.devoat.common.UserContext;

@Component
public class ContextHandler implements HandlerInterceptor {
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		UserContext ctx = UserContext.getInstance();
		ctx.init();
		return true;
	}

}
