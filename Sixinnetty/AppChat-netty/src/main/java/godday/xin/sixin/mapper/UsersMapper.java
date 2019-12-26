package godday.xin.sixin.mapper;

import godday.xin.sixin.pojo.Users;
import godday.xin.sixin.utils.MyMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

@Mapper
@Component("usermapper")
public interface UsersMapper extends MyMapper<Users> {
}