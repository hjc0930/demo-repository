package com.soybean.admin.system.controller;

import com.soybean.admin.common.annotation.RequirePermission;
import com.soybean.admin.common.response.Result;
import com.soybean.admin.data.entity.SysDept;
import com.soybean.admin.system.dto.DeptDTO;
import com.soybean.admin.system.dto.TreeSelectDTO;
import com.soybean.admin.system.service.SysDeptService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 部门控制器
 */
@RestController
@RequestMapping("/api/system/dept")
@RequiredArgsConstructor
public class SysDeptController {

    private final SysDeptService deptService;

    /**
     * 查询部门列表
     */
    @GetMapping("/list")
    @RequirePermission("system:dept:list")
    public Result<List<SysDept>> list(SysDept dept) {
        List<SysDept> depts = deptService.selectDeptList(dept);
        return Result.ok(depts);
    }

    /**
     * 查询部门树
     */
    @GetMapping("/tree")
    @RequirePermission("system:dept:list")
    public Result<List<SysDept>> tree(SysDept dept) {
        List<SysDept> tree = deptService.selectDeptTree(dept);
        return Result.ok(tree);
    }

    /**
     * 根据用户ID查询部门列表
     */
    @GetMapping("/user/{userId}")
    @RequirePermission("system:dept:query")
    public Result<List<SysDept>> userDept(@PathVariable Long userId) {
        List<SysDept> depts = deptService.selectDeptsByUserId(userId);
        return Result.ok(depts);
    }

    /**
     * 根据部门ID查询部门
     */
    @GetMapping("/{deptId}")
    @RequirePermission("system:dept:query")
    public Result<SysDept> getDept(@PathVariable Long deptId) {
        SysDept dept = deptService.selectDeptById(deptId);
        return Result.ok(dept);
    }

    /**
     * 新增部门
     */
    @PostMapping
    @RequirePermission("system:dept:add")
    public Result<Void> add(@RequestBody DeptDTO deptDTO) {
        deptService.insertDept(deptDTO);
        return Result.ok();
    }

    /**
     * 修改部门
     */
    @PutMapping
    @RequirePermission("system:dept:edit")
    public Result<Void> edit(@RequestBody DeptDTO deptDTO) {
        deptService.updateDept(deptDTO);
        return Result.ok();
    }

    /**
     * 删除部门
     */
    @DeleteMapping("/{deptId}")
    @RequirePermission("system:dept:remove")
    public Result<Void> remove(@PathVariable Long deptId) {
        deptService.deleteDept(deptId);
        return Result.ok();
    }

    /**
     * 构建部门树选择
     */
    @GetMapping("/tree-select")
    @RequirePermission("system:dept:query")
    public Result<List<TreeSelectDTO>> treeSelect(SysDept dept) {
        List<SysDept> depts = deptService.selectDeptList(dept);
        List<TreeSelectDTO> tree = deptService.buildDeptTreeSelect(depts);
        return Result.ok(tree);
    }
}
