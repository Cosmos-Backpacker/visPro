package com.cosmos.system.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cosmos.common.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author cosmosBackpacker
 * @since 2025-11-18
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

}
