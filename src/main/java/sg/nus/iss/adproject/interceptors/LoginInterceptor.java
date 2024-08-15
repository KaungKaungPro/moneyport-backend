package sg.nus.iss.adproject.interceptors;

import java.io.IOException;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import sg.nus.iss.adproject.entities.User;


public class LoginInterceptor implements HandlerInterceptor{

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
		String uri = request.getRequestURI();
		if(uri.equalsIgnoreCase("/") || uri.equalsIgnoreCase("") || uri.equalsIgnoreCase("/login")) {
			return true;
		}
		User loggedInUser = (User) request.getSession().getAttribute("loggedInUser");
		if(loggedInUser == null) {
			try {
				response.sendRedirect("/");
			} catch(IOException e) {
				System.out.println("IOException: Cannot send redirect to /");
			}
		}
		
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) {
		User loggedInUser = (User) request.getSession().getAttribute("loggedInUser");
		if(loggedInUser != null && modelAndView != null) {
			modelAndView.addObject("isLoggedIn", true);
			modelAndView.addObject("userFirstName", loggedInUser.getFirstName());
			modelAndView.addObject("userLastName", loggedInUser.getLastName());
		}
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
			Exception ex) {
		
	}
	
}
