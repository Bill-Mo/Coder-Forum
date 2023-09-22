package com.coder.community.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

import com.coder.community.annotation.LoginRequired;
import com.coder.community.entity.User;
import com.coder.community.service.FollowService;
import com.coder.community.service.LikeService;
import com.coder.community.service.UserService;
import com.coder.community.util.CommunityConstant;
import com.coder.community.util.CommunityUtil;
import com.coder.community.util.HostHolder;

import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/user")
public class UserController implements CommunityConstant{
    
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @LoginRequired
    @RequestMapping(path = "/setting", method = RequestMethod.GET)
    public String getSettingPage() {
        return "/site/setting";
    }

    @Value("${community.path.upload}")
    private String uploadPath;

    @Value("${community.path.domain}")
    private String domain;

    @Value("${server.servlet.context-path}")
    private String contextPath;

    @Autowired
    private UserService userService;

    @Autowired
    private HostHolder hostHolder;

    @LoginRequired
    @RequestMapping(path = "/upload", method = RequestMethod.POST)
    public String uploadHeader(MultipartFile headerImage, Model model) {
        if (headerImage == null) {
            model.addAttribute("error", "No image!");
            return "/site/setting";
        }

        String fileName = headerImage.getOriginalFilename();
        String suffix = null;
        if (fileName != null) {
            suffix = fileName.substring(fileName.lastIndexOf("."));
        } else {
            model.addAttribute("error", "No file found!");
        }
        if (StringUtils.isBlank(suffix)) {
            model.addAttribute("error", "No suffix!");
        }
        // Generate random file name
        fileName = CommunityUtil.generateUUID() + suffix;
        // File path
        File dest = new File(uploadPath + "/" + fileName);
        // Save file        
        try {
            headerImage.transferTo(dest);
        } catch (IOException e) {
            logger.error("Upload fail!", e.getMessage());
            throw new RuntimeException("Upload fail!", e);
        }
        // Update image
        // http://localhost:8080/community/user/header/xxx.png
        User user = hostHolder.getUser();
        String headerUrl = domain + contextPath + "/user/header/" + fileName;
        userService.updateHeader(user.getId(), headerUrl);

        return "redirect:/index";
    }

    @RequestMapping(path = "/header/{fileName}", method = RequestMethod.GET)
    public void getHeader(@PathVariable("fileName") String fileName, HttpServletResponse response) {
        // Server path
        fileName = uploadPath + "/" + fileName;
        // Suffix
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        response.setContentType("image/" + suffix);
        try (
            OutputStream os = response.getOutputStream();
            FileInputStream fis = new FileInputStream(fileName);
        ) {
            byte[] buffer = new byte[1024];
            int b = 0;
            while ((b = fis.read(buffer)) != -1) {
                os.write(buffer, 0, b);
            }
        } catch (IOException e) {
            logger.error("Get header fail!", e.getMessage());
        }
    }

    @LoginRequired
    @RequestMapping(path = "/changePassword", method = RequestMethod.POST)
    public String changePassword(Model model, String oldPassword, String newPassword) {
        User user = hostHolder.getUser();
        if (StringUtils.isBlank(newPassword)) {
            model.addAttribute("newError", "Password cannot be blank!");
            System.out.println("++++++++++++++++++++++");
            System.out.println(model);
            return "/site/setting";
        }
        newPassword = CommunityUtil.md5(newPassword + user.getSalt());
        oldPassword = CommunityUtil.md5(oldPassword + user.getSalt());
        if (!oldPassword.equals(user.getPassword())) {
            model.addAttribute("oldError", "Old password wrong!");
            System.out.println("+++++++++++++++++++++++++++");
            System.out.println(model);
            return "/site/setting";
        }
        if (newPassword.equals(oldPassword)) {
            model.addAttribute("newError", "New password and old password cannot be same!");
            return "/site/setting";
        }
        System.out.println("123574890987762345678");
        userService.changePassword(user.getId(), newPassword);

        return "redirect:/index";
    }

    @Autowired
    private LikeService likeService;

    @Autowired
    private FollowService followService;

    // Profile
    @RequestMapping(path = "/profile/{userId}", method = RequestMethod.GET)
    public String getProfilePage(@PathVariable("userId") int userId, Model model) {
        User user = userService.findUserById(userId);
        if (user == null) {
            throw new RuntimeException("User not exist!");
        }

        model.addAttribute("user", user);
        
        int likeCount = likeService.findUserLikeCount(userId);
        model.addAttribute("likeCount", likeCount);

        // 关注数量
        long followeeCount = followService.findFolloweeCount(userId, ENTITY_TYPE_USER);
        model.addAttribute("followeeCount", followeeCount);
        // 粉丝数量
        long followerCount = followService.findFollowerCount(ENTITY_TYPE_USER, userId);
        model.addAttribute("followerCount", followerCount);
        // 是否已关注
        boolean hasFollowed = false;
        if (hostHolder.getUser() != null) {
            hasFollowed = followService.hasFollowed(hostHolder.getUser().getId(), ENTITY_TYPE_USER, userId);
        }
        model.addAttribute("hasFollowed", hasFollowed);

        return "/site/profile";
    }
}
