package com.gtcafe.asimov.web.utils;

import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SessionUtil {

    public static void printSessionInfo(Model model, HttpServletRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        
        if (auth != null) {
            Object principal = auth.getPrincipal();
            if (principal instanceof UserDetails) {
                UserDetails userDetails = (UserDetails) principal;

                model.addAttribute("roles", userDetails.getAuthorities());
                log.debug("username: [{}], roles: [{}]", userDetails.getUsername(), userDetails.getAuthorities());

            }
        }

        HttpSession session = request.getSession(false); // false: 如果沒有 Session 就回傳 null
        if (session != null) {
            
            // model.addAttribute("session", session);
            model.addAttribute("sessionId", session.getId());
            model.addAttribute("sessionTimeout", session.getMaxInactiveInterval());
            model.addAttribute("sessionCreationTime", session.getCreationTime());
            model.addAttribute("sessionLastAccessTime", session.getLastAccessedTime());

            log.debug("sessionId: [{}], timeout: [{}], creationTime: [{}], lastAccessTime: [{}]", session.getId(), session.getMaxInactiveInterval(), session.getCreationTime(), session.getLastAccessedTime());
        }
    }

    public static void setHeaderInfo(Model model, HttpServletRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        String hostname = NetworkUtil.getHostName();
        String clientIp = NetworkUtil.getClientIp(request);

        model.addAttribute("username", auth.getName());
        model.addAttribute("computeNodeHostname", hostname);
        model.addAttribute("clientIp", clientIp);

        SessionUtil.printSessionInfo(model, request);
    }


    public static void setSessionInfo(Model model, HttpSession session) {
        // Map<String, Object> sessionInfo = new HashMap<>();

        // 獲取 Session ID
        model.addAttribute("sessionId", session.getId());

        // 獲取 Session 創建時間
        model.addAttribute("creationTime", new Date(session.getCreationTime()));

        // 獲取最後訪問時間
        model.addAttribute("lastAccessedTime", new Date(session.getLastAccessedTime()));

        // 獲取 Session 最大閒置時間（秒）
        model.addAttribute("maxInactiveInterval", session.getMaxInactiveInterval());

        // 獲取 Session 是否為新建
        model.addAttribute("isNew", session.isNew());

        // 獲取 Session 中所有屬性
        Enumeration<String> attributeNames = session.getAttributeNames();
        Map<String, Object> attributes = new HashMap<>();

        while (attributeNames.hasMoreElements()) {
            String name = attributeNames.nextElement();
            attributes.put(name, session.getAttribute(name));
        }

        model.addAttribute("attributes", attributes);

        // return sessionInfo;
    }


}