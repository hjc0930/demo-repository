package com.soybean.admin.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.soybean.admin.data.entity.SysClient;
import com.soybean.admin.system.dto.ClientDTO;

import java.util.List;

/**
 * 客户端服务接口
 */
public interface SysClientService extends IService<SysClient> {

    /**
     * 分页查询客户端
     */
    IPage<SysClient> selectClientPage(Page<SysClient> page, ClientDTO query);

    /**
     * 查询客户端列表
     */
    List<SysClient> selectClientList(ClientDTO query);

    /**
     * 根据ID查询客户端
     */
    SysClient selectClientById(Long id);

    /**
     * 根据客户端ID查询
     */
    SysClient selectClientByClientId(String clientId);

    /**
     * 新增客户端
     */
    boolean insertClient(ClientDTO clientDTO);

    /**
     * 修改客户端
     */
    boolean updateClient(ClientDTO clientDTO);

    /**
     * 删除客户端
     */
    boolean deleteClient(Long id);

    /**
     * 校验客户端ID是否唯一
     */
    boolean checkClientIdUnique(ClientDTO clientDTO);
}
