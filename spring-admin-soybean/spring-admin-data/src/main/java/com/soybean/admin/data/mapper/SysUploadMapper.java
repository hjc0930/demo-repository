package com.soybean.admin.data.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.soybean.admin.data.entity.SysUpload;
import org.apache.ibatis.annotations.Mapper;

/**
 * 文件上传表 Mapper接口
 */
@Mapper
public interface SysUploadMapper extends BaseMapper<SysUpload> {
}
