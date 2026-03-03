package com.soybean.admin.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.soybean.admin.common.exception.BusinessException;
import com.soybean.admin.common.response.ResponseCode;
import com.soybean.admin.data.entity.SysDept;
import com.soybean.admin.data.mapper.SysDeptMapper;
import com.soybean.admin.system.dto.DeptDTO;
import com.soybean.admin.system.dto.TreeSelectDTO;
import com.soybean.admin.system.service.SysDeptService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 部门服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SysDeptServiceImpl extends ServiceImpl<SysDeptMapper, SysDept> implements SysDeptService {

    private final SysDeptMapper deptMapper;

    @Override
    public List<SysDept> selectDeptList(SysDept dept) {
        LambdaQueryWrapper<SysDept> wrapper = new LambdaQueryWrapper<>();

        if (StringUtils.hasText(dept.getDeptName())) {
            wrapper.like(SysDept::getDeptName, dept.getDeptName());
        }
        if (StringUtils.hasText(dept.getStatus())) {
            wrapper.eq(SysDept::getStatus, dept.getStatus());
        }

        wrapper.orderByAsc(SysDept::getParentId, SysDept::getOrderNum);
        return this.list(wrapper);
    }

    @Override
    public List<SysDept> selectDeptsByUserId(Long userId) {
        return deptMapper.selectDeptsByUserId(userId);
    }

    @Override
    public List<SysDept> selectDeptTree(SysDept dept) {
        List<SysDept> depts = this.selectDeptList(dept);
        return buildDeptTree(depts, 0L);
    }

    @Override
    public SysDept selectDeptById(Long deptId) {
        return this.getById(deptId);
    }

    @Override
    public boolean insertDept(DeptDTO deptDTO) {
        SysDept dept = new SysDept();
        BeanUtils.copyProperties(deptDTO, dept);

        // 处理父部门ID
        if (deptDTO.getParentId() == null || deptDTO.getParentId() == 0) {
            dept.setParentId(0L);
        }

        // 生成ancestors
        if (dept.getParentId() != null && dept.getParentId() > 0) {
            SysDept parentDept = this.getById(dept.getParentId());
            if (parentDept != null) {
                dept.setAncestors(parentDept.getAncestors() + "," + dept.getParentId());
            } else {
                dept.setAncestors("0");
            }
        } else {
            dept.setAncestors("0");
        }

        return this.save(dept);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateDept(DeptDTO deptDTO) {
        SysDept dept = new SysDept();
        BeanUtils.copyProperties(deptDTO, dept);

        // 检查是否存在子部门
        if (dept.getParentId().equals(dept.getDeptId())) {
            throw new BusinessException(ResponseCode.OPERATION_FAILED, "修改部门'" + dept.getDeptName() + "'失败，上级部门不能是自己");
        }

        // 检查父部门是否存在
        if (dept.getParentId() != null && dept.getParentId() > 0) {
            SysDept parentDept = this.getById(dept.getParentId());
            if (parentDept == null) {
                throw new BusinessException(ResponseCode.DATA_NOT_FOUND, "父部门不存在");
            }

            // 更新ancestors
            dept.setAncestors(parentDept.getAncestors() + "," + dept.getParentId());

            // 更新所有子部门的ancestors
            updateChildrenAncestors(dept.getDeptId(), dept.getAncestors());
        } else {
            dept.setAncestors("0");
        }

        return this.updateById(dept);
    }

    @Override
    public boolean deleteDept(Long deptId) {
        // 检查是否存在子部门
        LambdaQueryWrapper<SysDept> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysDept::getParentId, deptId);
        long count = this.count(wrapper);
        if (count > 0) {
            throw new BusinessException(ResponseCode.DATA_IN_USE, "存在子部门,不允许删除");
        }

        // 检查是否存在用户
        // 这里需要检查sys_user表

        return this.removeById(deptId);
    }

    @Override
    public List<TreeSelectDTO> buildDeptTreeSelect(List<SysDept> depts) {
        List<TreeSelectDTO> trees = new ArrayList<>();
        List<SysDept> rootDepts = depts.stream()
            .filter(d -> d.getParentId() == 0)
            .collect(Collectors.toList());

        for (SysDept dept : rootDepts) {
            TreeSelectDTO dto = new TreeSelectDTO();
            dto.setId(dept.getDeptId());
            dto.setLabel(dept.getDeptName());
            dto.setChildren(buildDeptTreeSelectChildren(depts, dept.getDeptId()));
            trees.add(dto);
        }

        return trees;
    }

    @Override
    public boolean hasChildByDeptId(Long deptId) {
        LambdaQueryWrapper<SysDept> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysDept::getParentId, deptId);
        return this.count(wrapper) > 0;
    }

    /**
     * 构建部门树
     */
    private List<SysDept> buildDeptTree(List<SysDept> depts, Long parentId) {
        List<SysDept> treeList = new ArrayList<>();

        for (SysDept dept : depts) {
            if (parentId.equals(dept.getParentId())) {
                treeList.add(dept);
                // 递归查找子部门
                dept.setChildren(buildDeptTree(depts, dept.getDeptId()));
            }
        }

        return treeList;
    }

    /**
     * 构建部门树选择
     */
    private List<TreeSelectDTO> buildDeptTreeSelectChildren(List<SysDept> depts, Long parentId) {
        List<TreeSelectDTO> children = new ArrayList<>();

        for (SysDept dept : depts) {
            if (parentId.equals(dept.getParentId())) {
                TreeSelectDTO dto = new TreeSelectDTO();
                dto.setId(dept.getDeptId());
                dto.setLabel(dept.getDeptName());
                dto.setChildren(buildDeptTreeSelectChildren(depts, dept.getDeptId()));
                children.add(dto);
            }
        }

        return children;
    }

    /**
     * 更新子部门的ancestors
     */
    private void updateChildrenAncestors(Long deptId, String ancestors) {
        List<SysDept> children = this.list(new LambdaQueryWrapper<SysDept>()
            .eq(SysDept::getParentId, deptId));

        for (SysDept child : children) {
            child.setAncestors(ancestors + "," + child.getDeptId());
            this.updateById(child);
            // 递归更新
            updateChildrenAncestors(child.getDeptId(), child.getAncestors());
        }
    }
}
