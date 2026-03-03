package com.soybean.admin.system.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.soybean.admin.common.annotation.Log;
import com.soybean.admin.common.annotation.RequirePermission;
import com.soybean.admin.common.response.Result;
import com.soybean.admin.data.entity.SysPost;
import com.soybean.admin.system.dto.PostDTO;
import com.soybean.admin.system.service.SysPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 岗位控制器
 */
@RestController
@RequestMapping("/api/system/post")
@RequiredArgsConstructor
public class SysPostController {

    private final SysPostService postService;

    /**
     * 分页查询岗位
     */
    @GetMapping("/page")
    @RequirePermission("system:post:list")
    public Result<IPage<SysPost>> page(Page<SysPost> page, PostDTO query) {
        IPage<SysPost> result = postService.selectPostPage(page, query);
        return Result.ok(result);
    }

    /**
     * 查询岗位列表
     */
    @GetMapping("/list")
    @RequirePermission("system:post:list")
    public Result<List<SysPost>> list(PostDTO query) {
        List<SysPost> list = postService.selectPostList(query);
        return Result.ok(list);
    }

    /**
     * 根据ID查询岗位
     */
    @GetMapping("/{postId}")
    @RequirePermission("system:post:query")
    public Result<SysPost> getPost(@PathVariable Long postId) {
        SysPost post = postService.selectPostById(postId);
        return Result.ok(post);
    }

    /**
     * 新增岗位
     */
    @PostMapping
    @RequirePermission("system:post:add")
    @Log(title = "岗位管理", businessType = Log.BusinessType.INSERT)
    public Result<Void> add(@RequestBody PostDTO postDTO) {
        postService.insertPost(postDTO);
        return Result.ok();
    }

    /**
     * 修改岗位
     */
    @PutMapping
    @RequirePermission("system:post:edit")
    @Log(title = "岗位管理", businessType = Log.BusinessType.UPDATE)
    public Result<Void> edit(@RequestBody PostDTO postDTO) {
        postService.updatePost(postDTO);
        return Result.ok();
    }

    /**
     * 删除岗位
     */
    @DeleteMapping("/{postId}")
    @RequirePermission("system:post:remove")
    @Log(title = "岗位管理", businessType = Log.BusinessType.DELETE)
    public Result<Void> remove(@PathVariable Long postId) {
        postService.deletePost(postId);
        return Result.ok();
    }

    /**
     * 校验岗位编码是否唯一
     */
    @GetMapping("/check/{postCode}")
    @RequirePermission("system:post:query")
    public Result<Boolean> checkPostCodeUnique(@PathVariable String postCode) {
        PostDTO postDTO = new PostDTO();
        postDTO.setPostCode(postCode);
        boolean result = postService.checkPostCodeUnique(postDTO);
        return Result.ok(result);
    }
}
