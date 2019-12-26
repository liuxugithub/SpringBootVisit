package godday.xin.sixin.service;

import godday.xin.sixin.pojo.Users;

public interface UserService {
     boolean queryUsernameIsExist(String username);


     Users queryUserForLogin(String username,String pwd);


     Users saveusers(Users users);


     Users updateUserInfo(Users users);

     Users queryUserById(String userid);


}
