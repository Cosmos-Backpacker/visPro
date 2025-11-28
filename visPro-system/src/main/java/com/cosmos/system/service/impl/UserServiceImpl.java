package com.cosmos.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cosmos.common.entity.User;
import com.cosmos.common.exception.BusinessException;
import com.cosmos.common.model.LoginUser;
import com.cosmos.common.utils.StringUtils;
import com.cosmos.system.mapper.UserMapper;
import com.cosmos.system.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import static com.cosmos.common.constant.UserConstants.SALT;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author cosmosBackpacker
 * @since 2025-11-18
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Autowired
    private UserMapper userMapper;


    @Override
    public User getLoginUser(String account) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUserAccount, account);
        User user = baseMapper.selectOne(queryWrapper);

        if (user == null) {
            throw new BusinessException("用户不存在");
        }

        return user;
    }

    @Override
    public boolean registerUser(String account, String password, String confirmPassword) {
        User user = new User();
        user.setUserAccount(account);
        // 密码加密
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + password).getBytes());
        user.setUserPassword(encryptPassword);

        return baseMapper.insert(user) > 0;

    }

    @Override
    public boolean updateUserInfo(User user) {
        User dbUser = baseMapper.selectById(user.getId());
        if (dbUser == null) {
            throw new BusinessException("用户不存在");
        }
        dbUser.setUsername(user.getUsername());

        dbUser.setAvatarUrl(user.getAvatarUrl());
        if (StringUtils.isNotEmpty(user.getUserPassword())) {
            dbUser.setUserPassword(DigestUtils.md5DigestAsHex((SALT + user.getUserPassword()).getBytes()));
        }
        dbUser.setPhone(user.getPhone());
        dbUser.setEmail(user.getEmail());
        dbUser.setGender(user.getGender());

        return baseMapper.updateById(dbUser) > 0;
    }


}
