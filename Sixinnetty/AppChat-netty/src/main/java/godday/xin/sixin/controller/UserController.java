package godday.xin.sixin.controller;
import godday.xin.sixin.Application;
import godday.xin.sixin.mapper.UsersMapper;
import godday.xin.sixin.pojo.Users;
import godday.xin.sixin.pojo.bo.UsersBo;
import godday.xin.sixin.pojo.vo.UsersVo;
import godday.xin.sixin.service.UserService;
import godday.xin.sixin.utils.FastDFSClient;
import godday.xin.sixin.utils.IMoocJSONResult;
import godday.xin.sixin.utils.MD5Utils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Base64Utils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@RestController
@RequestMapping("u")
public class UserController {
    private static final String basePath;
    @Autowired
    private FastDFSClient fastDFSClient;
    static{
            basePath= Application.class.getResource("").getPath();
    }
    @Autowired
    private UserService userService;
    @PostMapping("/registOrLogin")
    public IMoocJSONResult registOrLogin(@RequestBody Users users) throws Exception {
        System.out.println("登录进来了！！！");
        if(StringUtils.isBlank(users.getUsername())||StringUtils.isBlank(users.getPassword())){
            return IMoocJSONResult.errorMsg("用户名和密码不能为空");
        }
        Users user_res=null;
        boolean users_flag=userService.queryUsernameIsExist(users.getUsername());
        System.out.println("username:"+users.getUsername());
        System.out.println("pwd:"+users.getPassword());
        if(users_flag){
            user_res=userService.queryUserForLogin(users.getUsername(), MD5Utils.getMD5Str(users.getPassword()));
            if(user_res==null){
                System.out.println("用户名或者密码不正确");
                return IMoocJSONResult.errorMsg("用户名或者密码不正确");
            }
        }else{
            users.setNickname(users.getUsername());
            users.setFaceImage("");
            users.setFaceImageBig("");
            users.setPassword(MD5Utils.getMD5Str(users.getPassword()));
            user_res=userService.saveusers(users);
        }
        UsersVo usersVo = new UsersVo();
        BeanUtils.copyProperties(user_res, usersVo);
        System.out.println("usersVo.name="+usersVo.getUsername());
        return IMoocJSONResult.ok(usersVo);
    }
    @GetMapping("/u_123")
    public String gostring(){
        System.out.println("进来了");
        return "yes";
    }
    @PostMapping("/uploadFaceBase64")
    public IMoocJSONResult uploadFaceBase64(@RequestBody UsersBo usersBo) throws Exception {
        //获取前端base64字符串
        System.out.println("开始上传图片");
        System.out.println(basePath);
        String img64data=usersBo.getFaceData();
        String userid=usersBo.getUserId();
        String userFacePath=basePath+userid+".png";
        godday.xin.sixin.utils.FileUtils.base64ToFile(userFacePath, img64data);
        MultipartFile multipartFile=godday.xin.sixin.utils.FileUtils.fileToMultipart(userFacePath);
        String url=fastDFSClient.uploadBase64(multipartFile);
        System.out.println("url:"+url);
        // 获取缩略图的url
        String thump = "_80x80.";
        String arr[]= url.split("\\.");
        String thumpImgUrl=arr[0]+thump+arr[1];
        Users users = new Users();
        users.setId(usersBo.getUserId());
        users.setFaceImageBig(url);
        users.setFaceImage(thumpImgUrl);
        Users res_user=userService.updateUserInfo(users);
        Users users1=userService.queryUserById(res_user.getId());
        return IMoocJSONResult.ok(users1);
    }


    @PostMapping("/setNickName")
    public IMoocJSONResult setNickName(@RequestBody UsersBo usersBo) {
        Users users = new Users();
        users.setId(usersBo.getUserId());
        users.setNickname(usersBo.getNickName());
        Users res=userService.updateUserInfo(users);
        return IMoocJSONResult.ok(res);
    }
}
