/** 
 * Copyright (C) 2018 thinh ho
 * This file is part of 'sample-sso' which is released under the MIT license.
 * See LICENSE at the project root directory.
 */
package kkdt.sample.sso.romeo;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PublicController {
    
    @RequestMapping(path="/public", produces="application/json")
    public Map<Integer, Map<String, Object>> pub(HttpServletRequest request, HttpServletResponse response) {
        Map<Integer, Map<String, Object>> ret = new HashMap<>();
        if(request.getCookies() != null) {
            for(int i = 0; i < request.getCookies().length; i++) {
                Cookie c = request.getCookies()[i];
                c.setMaxAge(0);
                c.setVersion(2);
                
                Map<String, Object> cookie = new HashMap<>();
                cookie.put("name", c.getName());
                cookie.put("value", c.getValue());
                cookie.put("domain", c.getDomain());
                cookie.put("maxAge", c.getMaxAge());
                cookie.put("path", c.getPath());
                cookie.put("version", c.getVersion());
                cookie.put("comment", c.getComment());
                cookie.put("secure", c.getSecure());
                ret.put(i, cookie);
            }
        }
        return ret;
    }
}
