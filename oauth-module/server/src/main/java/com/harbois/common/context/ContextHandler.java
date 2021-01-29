package com.harbois.common.context;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.harbois.oauth.api.v1.common.UserContext;

@Component
public class ContextHandler extends HandlerInterceptorAdapter {
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		UserContext ctx = UserContext.getInstance();
		ctx.init();
		return true;
	}

}
