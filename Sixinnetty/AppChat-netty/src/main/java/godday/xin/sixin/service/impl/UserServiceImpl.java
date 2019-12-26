package godday.xin.sixin.service.impl;

import godday.xin.org.n3r.idworker.Sid;
import godday.xin.sixin.controller.UserController;
import godday.xin.sixin.mapper.UsersMapper;
import godday.xin.sixin.pojo.Users;
import godday.xin.sixin.service.UserService;
import godday.xin.sixin.utils.FastDFSClient;
import godday.xin.sixin.utils.QRCodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import tk.mybatis.mapper.entity.Example;

import java.io.File;
import java.io.IOException;


@Service
public class UserServiceImpl implements UserService {
   static String basePath;
    @Autowired
    private FastDFSClient fastDFSClient;

   @Autowired
    godday.xin.sixin.utils.FileUtils fileUtils;
    static{
        basePath= UserServiceImpl.class.getResource("").getPath();
    }
    @Autowired
    private  UsersMapper usersMapper;


    @Autowired
    private QRCodeUtils qrCodeUtils;
    @Autowired
    private Sid sid;
    @Override
    public boolean queryUsernameIsExist(String username) {
        Users users= new Users();
        users.setUsername(username);
        Users users1=usersMapper.selectOne(users);
        return users1!=null?true:false;
    }

    @Override
    public Users queryUserForLogin(String username, String pwd) {
        Example example = new Example(Users.class);
        Example.Criteria criteria=example.createCriteria();
        criteria.andEqualTo("username",username);
        criteria.andEqualTo("password",pwd);
        return usersMapper.selectOneByExample(example);
    }
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Users saveusers(Users users) {
        // 为每一个用户生成唯一的二维码
        String userid=sid.nextShort();
        //生成用户到唯一二维码
        String qrCodePath=basePath+"QRCode";
        File file=new File(qrCodePath);
        if(!file.exists()){
            file.mkdirs();
        }
        String CodeName=userid+"_qrcode.png";
       //sixin_qrcode:[username]
        qrCodeUtils.createQRCode(qrCodePath,"sixin_qrcode:"+ users.getUsername());
        MultipartFile qrCodeFile=fileUtils.fileToMultipart(qrCodePath);
        String qrCodeurl="";
        try {
            qrCodeurl=fastDFSClient.uploadBase64(qrCodeFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        users.setQrcode(qrCodeurl);
        users.setId(userid);
        usersMapper.insert(users);
        return users;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Users updateUserInfo(Users users) {
        usersMapper.updateByPrimaryKeySelective(users);
        return queryUserById(users.getId());
    }
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Users queryUserById(String userid){

        return usersMapper.selectByPrimaryKey(userid);
    }
}
