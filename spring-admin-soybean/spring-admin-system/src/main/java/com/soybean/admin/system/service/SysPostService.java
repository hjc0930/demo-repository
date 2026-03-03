package com.soybean.admin.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.soybean.admin.data.entity.SysPost;
import com.soybean.admin.system.dto.PostDTO;

import java.util.List;

/**
 * 岗位服务接口
 */
public interface SysPostService extends IService<SysPost> {

    /**
     * 分页查询岗位
     */
    IPage<SysPost> selectPostPage(Page<SysPost> page, PostDTO query);

    /**
     * 查询岗位列表
     */
    List<SysPost> selectPostList(PostDTO query);

    /**
     * 根据ID查询岗位
     */
    SysPost selectPostById(Long postId);

    /**
     * 新增岗位
     */
    boolean insertPost(PostDTO postDTO);

    /**
     * 修改岗位
     */
    boolean updatePost(PostDTO postDTO);

    /**
     * 删除岗位
     */
    boolean deletePost(Long postId);

    /**
     * 校验岗位编码是否唯一
     */
    boolean checkPostCodeUnique(PostDTO postDTO);
}
