package com.coder.community.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.coder.community.util.CommunityUtil;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/alpha")
public class alphaController {
    @RequestMapping("/hello")
    @ResponseBody
    public String sayHallo() {
        return "Hello";
    }

    @RequestMapping("/http")
    public void http(HttpServletRequest request, HttpServletResponse response) {
        System.out.println(request.getMethod());
        System.out.println(request.getServletPath());
        Enumeration<String> enumeration = request.getHeaderNames();
        while (enumeration.hasMoreElements()) {
            String name = enumeration.nextElement();
            String value = request.getHeader(name); 
            System.out.println(name + ": " + value);
        }
        System.out.println(request.getParameter("code"));
        response.setContentType("text/html;charset=utf-8");
        try (
            PrintWriter writer = response.getWriter(); 
        ) {
            writer.write("<h1>Coder Community</h1>");
        } catch (IOException e) {
            e.printStackTrace( );
        }
    }

    
    @RequestMapping(path = "/students", method = RequestMethod.GET)
    @ResponseBody
    public String getStudents(
        @RequestParam(name = "current", required = false, defaultValue = "2") int current, 
        @RequestParam(name = "lim", required = false, defaultValue = "100") int limit) {
            System.out.println(current);
            System.out.println(limit);
        return "some students";
    }


    @RequestMapping(path = "/student/{id}", method = RequestMethod.GET)
    @ResponseBody
    public int getStudent(@PathVariable("id") int id) {
            System.out.println("student" + id);
        return id;
    }

    @RequestMapping(path = "/student", method = RequestMethod.POST)
    @ResponseBody
    public String saveStudent(String name, int age) {
        System.out.println(name);
        System.out.println(age);
        return "Success";
    }

    @RequestMapping(path = "/teacher", method = RequestMethod.GET)
    public ModelAndView getTeacher() {
        ModelAndView mav = new ModelAndView();
        mav.addObject("name", "张三");
        mav.addObject("age", "30");
        mav.setViewName("/demo/view");
        return mav;
    }

    @RequestMapping(path = "/school", method = RequestMethod.GET)
    public String getSchool(Model model) {
        model.addAttribute("name", "北京大学");
        model.addAttribute("age", "60");
        return "demo/view";
    }

    @RequestMapping(path = "/emp", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getEmp() {
        Map<String, Object> emp = new HashMap<String, Object>();
        emp.put("name", "张三");
        emp.put("age", 24);
        return emp;
    }

    @RequestMapping(path = "/cookie/set", method = RequestMethod.GET)
    @ResponseBody
    public String setCookie(HttpServletResponse response) {
        Cookie cookie = new Cookie("code", CommunityUtil.generateUUID());
        cookie.setPath("/community/alpha");
        cookie.setMaxAge(60 * 10);
        response.addCookie(cookie);
        return "Set cookie";
    }
    
    @RequestMapping(path = "/cookie/get", method = RequestMethod.GET)
    @ResponseBody
    public String getCookie(@CookieValue("code") String code) {
        return "Get cookie";
    }

    @RequestMapping(path = "session/set", method = RequestMethod.GET) 
    @ResponseBody
    public String setSession(HttpSession session) {
        session.setAttribute("id", 2);
        session.setAttribute("name", "niuke");
        return "set session";
    }

    @RequestMapping(path = "session/get", method = RequestMethod.GET) 
    @ResponseBody
    public String getSession(HttpSession session) {
        System.out.println(session.getAttribute("id"));
        return "get session";
    }
}
