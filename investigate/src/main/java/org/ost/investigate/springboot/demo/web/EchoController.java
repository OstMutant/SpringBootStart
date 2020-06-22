package org.ost.investigate.springboot.demo.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.*;

@RestController
@Slf4j
public class EchoController {
    private final HttpServletRequest request;

    public EchoController(HttpServletRequest request) {
        this.request = request;
    }

    @RequestMapping(value = "/echo", consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
            method = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS})
    public ResponseEntity<Map<String, Object>> echoBack(@RequestBody(required = false) byte[] rawBody) throws IOException {

        Map<String, String> headers = new HashMap<String, String>();
        for (String headerName : Collections.list(request.getHeaderNames())) {
            headers.put(headerName, request.getHeader(headerName));
        }

        Map<String, Object> responseMap = new HashMap<String, Object>();
        responseMap.put("protocol", request.getProtocol());
        responseMap.put("method", request.getMethod());
        responseMap.put("headers", headers);
        responseMap.put("cookies", request.getCookies());
        responseMap.put("parameters", request.getParameterMap());
        responseMap.put("path", request.getServletPath());
        responseMap.put("Base64_body", rawBody != null ? Base64.getEncoder().encodeToString(rawBody) : null);
        responseMap.put("raw_Body", rawBody != null ? new String(rawBody) : null);
        log.info("REQUEST: " + request.getParameterMap());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseMap);
    }

    @RequestMapping(value = "/salesforce/callback", consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
            method = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS})
    @ResponseStatus(HttpStatus.OK)
    public void logRequest(@RequestBody(required = false) byte[] rawBody) throws IOException {

        Map<String, String> headers = new HashMap<>();
        for (String headerName : Collections.list(request.getHeaderNames())) {
            headers.put(headerName, request.getHeader(headerName));
        }

        Map<String, Object> responseMap = new HashMap<String, Object>();
        responseMap.put("protocol", request.getProtocol());
        responseMap.put("method", request.getMethod());
        responseMap.put("headers", headers);
        responseMap.put("cookies", request.getCookies());
        responseMap.put("parameters", request.getParameterMap());
        responseMap.put("path", request.getServletPath());
        responseMap.put("Base64_body", rawBody != null ? Base64.getEncoder().encodeToString(rawBody) : null);
        responseMap.put("raw_Body", rawBody != null ? new String(rawBody) : null);
        log.info("REQUEST: " + responseMap);
    }
}
