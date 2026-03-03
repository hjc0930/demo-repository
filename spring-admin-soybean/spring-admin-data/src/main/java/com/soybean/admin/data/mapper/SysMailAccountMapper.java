package com.soybean.admin.data.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.soybean.admin.data.entity.SysMailAccount;
import org.apache.ibatis.annotations.Mapper;

/**
 * 邮箱账号表 Mapper接口
 */
@Mapper
public interface SysMailAccountMapper extends BaseMapper<SysMailAccount> {
}
