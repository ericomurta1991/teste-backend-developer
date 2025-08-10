package com.desafio_dev.desafio_dev.resources.exceptions;

import java.time.Instant;
import java.util.Map;

import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class CustomErrorController implements ErrorController {

    private static final String ERROR_PATH = "/error";

    private final ErrorAttributes errorAttributes;

    public CustomErrorController(ErrorAttributes errorAttributes) {
        this.errorAttributes = errorAttributes;
    }

    @RequestMapping(ERROR_PATH)
    public ResponseEntity<StandardError> handleError(HttpServletRequest request) {
    	System.out.println(">>> CustomErrorController called");
    	  WebRequest webRequest = new ServletWebRequest(request);

          Map<String, Object> attrs = errorAttributes.getErrorAttributes(
              webRequest,
              ErrorAttributeOptions.of(
                  ErrorAttributeOptions.Include.MESSAGE,
                  ErrorAttributeOptions.Include.BINDING_ERRORS
              )
          );

          int status = (int) attrs.getOrDefault("status", 500);
          // força mensagem "Not Found" quando for 404
          String message = status == 404 ? "Not Found" : (String) attrs.getOrDefault("message", "Erro inesperado, entre em contato com o suporte.");
          String error = status == 404 ? "Not Found" : (String) attrs.getOrDefault("error", "Internal Server Error");
          String path = (String) attrs.getOrDefault("path", request.getRequestURI());

          StandardError err = new StandardError();
          err.setTimestamp(Instant.now());
          err.setStatus(status);
          err.setError(error);
          err.setMessage(message);
          err.setPath(path);

          return ResponseEntity.status(status).body(err);
    }

}