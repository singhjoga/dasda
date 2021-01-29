package com.harbois.common.validation.spring;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.harbois.common.validation.ValidationContext;
@Component
public class ValidationContextInitFilter extends OncePerRequestFilter{

	@Override
	protected void doFilterInternal(HttpServletRequest req, HttpServletResponse resp, FilterChain chain) throws ServletException, IOException {
		if (req.getMethod().equals("PATCH")) {
			ValidationContext.getInstance().setUpdate(true);
		}else {
			ValidationContext.getInstance().setUpdate(false);
		}

		chain.doFilter(req, resp);
	}

}
