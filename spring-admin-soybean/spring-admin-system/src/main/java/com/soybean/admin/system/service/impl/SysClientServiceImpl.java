package com.soybean.admin.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.soybean.admin.common.exception.BusinessException;
import com.soybean.admin.common.response.ResponseCode;
import com.soybean.admin.data.entity.SysClient;
import com.soybean.admin.data.mapper.SysClientMapper;
import com.soybean.admin.system.dto.ClientDTO;
import com.soybean.admin.system.service.SysClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 客户端服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SysClientServiceImpl extends ServiceImpl<SysClientMapper, SysClient> implements SysClientService {

    private final SysClientMapper clientMapper;

    @Override
    public IPage<SysClient> selectClientPage(Page<SysClient> page, ClientDTO query) {
        LambdaQueryWrapper<SysClient> wrapper = new LambdaQueryWrapper<>();

        if (StringUtils.hasText(query.getClientId())) {
            wrapper.like(SysClient::getClientId, query.getClientId());
        }
        if (StringUtils.hasText(query.getClientKey())) {
            wrapper.like(SysClient::getClientKey, query.getClientKey());
        }
        if (StringUtils.hasText(query.getDeviceType())) {
            wrapper.eq(SysClient::getDeviceType, query.getDeviceType());
        }
        if (StringUtils.hasText(query.getStatus())) {
            wrapper.eq(SysClient::getStatus, query.getStatus());
        }
        if (StringUtils.hasText(query.getKeyword())) {
            wrapper.and(w -> w.like(SysClient::getClientId, query.getKeyword())
                    .or().like(SysClient::getClientKey, query.getKeyword()));
        }

        // 查询未删除的数据
        wrapper.eq(SysClient::getDelFlag, "0");
        wrapper.orderByDesc(SysClient::getCreateTime);
        return this.page(page, wrapper);
    }

    @Override
    public List<SysClient> selectClientList(ClientDTO query) {
        LambdaQueryWrapper<SysClient> wrapper = new LambdaQueryWrapper<>();

        if (StringUtils.hasText(query.getClientId())) {
            wrapper.like(SysClient::getClientId, query.getClientId());
        }
        if (StringUtils.hasText(query.getClientKey())) {
            wrapper.like(SysClient::getClientKey, query.getClientKey());
        }
        if (StringUtils.hasText(query.getDeviceType())) {
            wrapper.eq(SysClient::getDeviceType, query.getDeviceType());
        }
        if (StringUtils.hasText(query.getStatus())) {
            wrapper.eq(SysClient::getStatus, query.getStatus());
        }

        wrapper.eq(SysClient::getDelFlag, "0");
        wrapper.orderByDesc(SysClient::getCreateTime);
        return this.list(wrapper);
    }

    @Override
    public SysClient selectClientById(Long id) {
        return this.getById(id);
    }

    @Override
    public SysClient selectClientByClientId(String clientId) {
        LambdaQueryWrapper<SysClient> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysClient::getClientId, clientId);
        wrapper.eq(SysClient::getDelFlag, "0");
        return this.getOne(wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean insertClient(ClientDTO clientDTO) {
        // 检查客户端ID是否已存在
        if (!checkClientIdUnique(clientDTO)) {
            throw new BusinessException(ResponseCode.DATA_ALREADY_EXISTS, "新增客户端失败，客户端ID已存在");
        }

        SysClient client = new SysClient();
        BeanUtils.copyProperties(clientDTO, client);
        client.setDelFlag("0");

        return this.save(client);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateClient(ClientDTO clientDTO) {
        SysClient existingClient = this.getById(clientDTO.getId());
        if (existingClient == null) {
            throw new BusinessException(ResponseCode.DATA_NOT_FOUND);
        }

        // 检查客户端ID是否唯一
        if (!checkClientIdUnique(clientDTO)) {
            throw new BusinessException(ResponseCode.DATA_ALREADY_EXISTS, "修改客户端失败，客户端ID已存在");
        }

        SysClient client = new SysClient();
        BeanUtils.copyProperties(clientDTO, client);

        return this.updateById(client);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteClient(Long id) {
        // 逻辑删除
        SysClient client = new SysClient();
        client.setId(id);
        client.setDelFlag("2");

        return this.updateById(client);
    }

    @Override
    public boolean checkClientIdUnique(ClientDTO clientDTO) {
        Long id = clientDTO.getId() == null ? -1L : clientDTO.getId();
        LambdaQueryWrapper<SysClient> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysClient::getClientId, clientDTO.getClientId());
        SysClient client = this.getOne(wrapper);
        if (client != null && !client.getId().equals(id)) {
            return false;
        }
        return true;
    }
}
