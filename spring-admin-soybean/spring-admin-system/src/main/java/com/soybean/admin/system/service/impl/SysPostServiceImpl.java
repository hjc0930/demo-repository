package com.soybean.admin.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.soybean.admin.common.exception.BusinessException;
import com.soybean.admin.common.response.ResponseCode;
import com.soybean.admin.data.entity.SysPost;
import com.soybean.admin.data.mapper.SysPostMapper;
import com.soybean.admin.system.dto.PostDTO;
import com.soybean.admin.system.service.SysPostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 岗位服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SysPostServiceImpl extends ServiceImpl<SysPostMapper, SysPost> implements SysPostService {

    private final SysPostMapper postMapper;

    @Override
    public IPage<SysPost> selectPostPage(Page<SysPost> page, PostDTO query) {
        LambdaQueryWrapper<SysPost> wrapper = new LambdaQueryWrapper<>();

        if (StringUtils.hasText(query.getPostName())) {
            wrapper.like(SysPost::getPostName, query.getPostName());
        }
        if (StringUtils.hasText(query.getPostCode())) {
            wrapper.like(SysPost::getPostCode, query.getPostCode());
        }
        if (StringUtils.hasText(query.getPostCategory())) {
            wrapper.eq(SysPost::getPostCategory, query.getPostCategory());
        }
        if (StringUtils.hasText(query.getStatus())) {
            wrapper.eq(SysPost::getStatus, query.getStatus());
        }
        if (query.getDeptId() != null) {
            wrapper.eq(SysPost::getDeptId, query.getDeptId());
        }
        if (StringUtils.hasText(query.getKeyword())) {
            wrapper.and(w -> w.like(SysPost::getPostName, query.getKeyword())
                    .or().like(SysPost::getPostCode, query.getKeyword()));
        }

        wrapper.orderByAsc(SysPost::getPostSort);
        return this.page(page, wrapper);
    }

    @Override
    public List<SysPost> selectPostList(PostDTO query) {
        LambdaQueryWrapper<SysPost> wrapper = new LambdaQueryWrapper<>();

        if (StringUtils.hasText(query.getPostName())) {
            wrapper.like(SysPost::getPostName, query.getPostName());
        }
        if (StringUtils.hasText(query.getPostCode())) {
            wrapper.like(SysPost::getPostCode, query.getPostCode());
        }
        if (StringUtils.hasText(query.getPostCategory())) {
            wrapper.eq(SysPost::getPostCategory, query.getPostCategory());
        }
        if (StringUtils.hasText(query.getStatus())) {
            wrapper.eq(SysPost::getStatus, query.getStatus());
        }
        if (query.getDeptId() != null) {
            wrapper.eq(SysPost::getDeptId, query.getDeptId());
        }

        wrapper.orderByAsc(SysPost::getPostSort);
        return this.list(wrapper);
    }

    @Override
    public SysPost selectPostById(Long postId) {
        return this.getById(postId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean insertPost(PostDTO postDTO) {
        // 检查岗位编码是否已存在
        if (!checkPostCodeUnique(postDTO)) {
            throw new BusinessException(ResponseCode.DATA_ALREADY_EXISTS, "新增岗位'" + postDTO.getPostName() + "'失败，岗位编码已存在");
        }

        SysPost post = new SysPost();
        BeanUtils.copyProperties(postDTO, post);

        return this.save(post);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updatePost(PostDTO postDTO) {
        SysPost existingPost = this.getById(postDTO.getPostId());
        if (existingPost == null) {
            throw new BusinessException(ResponseCode.DATA_NOT_FOUND);
        }

        // 检查岗位编码是否唯一
        if (!checkPostCodeUnique(postDTO)) {
            throw new BusinessException(ResponseCode.DATA_ALREADY_EXISTS, "修改岗位'" + postDTO.getPostName() + "'失败，岗位编码已存在");
        }

        SysPost post = new SysPost();
        BeanUtils.copyProperties(postDTO, post);

        return this.updateById(post);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deletePost(Long postId) {
        return this.removeById(postId);
    }

    @Override
    public boolean checkPostCodeUnique(PostDTO postDTO) {
        Long postId = postDTO.getPostId() == null ? -1L : postDTO.getPostId();
        LambdaQueryWrapper<SysPost> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysPost::getPostCode, postDTO.getPostCode());
        SysPost post = this.getOne(wrapper);
        if (post != null && !post.getPostId().equals(postId)) {
            return false;
        }
        return true;
    }
}
