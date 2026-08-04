package com.example.petadoption.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
public class GlobalExceptionHandler {

    //exception handler that throw
    @ExceptionHandler({IllegalArgumentException.class, IllegalStateException.class})
    public String handlerThrowException(Exception e, RedirectAttributes redirectAttributes){
        redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        return "redirect:" + getBackUrl();
    }

    //check where is previous url
    private String getBackUrl(){
        ServletRequestAttributes a = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (a != null){
            HttpServletRequest request = a.getRequest();
            String url = request.getHeader("Referer");
            if(url != null){
                return url;
            }
        }
        return "/petAdoption";
    }

}
