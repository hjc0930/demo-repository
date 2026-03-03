package com.soybean.admin.system.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.soybean.admin.common.annotation.Log;
import com.soybean.admin.common.annotation.RequirePermission;
import com.soybean.admin.common.response.Result;
import com.soybean.admin.data.entity.SysClient;
import com.soybean.admin.system.dto.ClientDTO;
import com.soybean.admin.system.service.SysClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 客户端控制器
 */
@RestController
@RequestMapping("/api/system/client")
@RequiredArgsConstructor
public class SysClientController {

    private final SysClientService clientService;

    /**
     * 分页查询客户端
     */
    @GetMapping("/page")
    @RequirePermission("system:client:list")
    public Result<IPage<SysClient>> page(Page<SysClient> page, ClientDTO query) {
        IPage<SysClient> result = clientService.selectClientPage(page, query);
        return Result.ok(result);
    }

    /**
     * 查询客户端列表
     */
    @GetMapping("/list")
    @RequirePermission("system:client:list")
    public Result<List<SysClient>> list(ClientDTO query) {
        List<SysClient> list = clientService.selectClientList(query);
        return Result.ok(list);
    }

    /**
     * 根据ID查询客户端
     */
    @GetMapping("/{id}")
    @RequirePermission("system:client:query")
    public Result<SysClient> getClient(@PathVariable Long id) {
        SysClient client = clientService.selectClientById(id);
        return Result.ok(client);
    }

    /**
     * 根据客户端ID查询
     */
    @GetMapping("/clientId/{clientId}")
    @RequirePermission("system:client:query")
    public Result<SysClient> getClientByClientId(@PathVariable String clientId) {
        SysClient client = clientService.selectClientByClientId(clientId);
        return Result.ok(client);
    }

    /**
     * 新增客户端
     */
    @PostMapping
    @RequirePermission("system:client:add")
    @Log(title = "客户端管理", businessType = Log.BusinessType.INSERT)
    public Result<Void> add(@RequestBody ClientDTO clientDTO) {
        clientService.insertClient(clientDTO);
        return Result.ok();
    }

    /**
     * 修改客户端
     */
    @PutMapping
    @RequirePermission("system:client:edit")
    @Log(title = "客户端管理", businessType = Log.BusinessType.UPDATE)
    public Result<Void> edit(@RequestBody ClientDTO clientDTO) {
        clientService.updateClient(clientDTO);
        return Result.ok();
    }

    /**
     * 删除客户端
     */
    @DeleteMapping("/{id}")
    @RequirePermission("system:client:remove")
    @Log(title = "客户端管理", businessType = Log.BusinessType.DELETE)
    public Result<Void> remove(@PathVariable Long id) {
        clientService.deleteClient(id);
        return Result.ok();
    }

    /**
     * 校验客户端ID是否唯一
     */
    @GetMapping("/check/{clientId}")
    @RequirePermission("system:client:query")
    public Result<Boolean> checkClientIdUnique(@PathVariable String clientId) {
        ClientDTO clientDTO = new ClientDTO();
        clientDTO.setClientId(clientId);
        boolean result = clientService.checkClientIdUnique(clientDTO);
        return Result.ok(result);
    }
}
