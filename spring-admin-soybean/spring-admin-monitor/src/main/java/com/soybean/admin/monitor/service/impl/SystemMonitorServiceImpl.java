package com.soybean.admin.monitor.service.impl;

import com.soybean.admin.monitor.service.SystemMonitorService;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.GlobalMemory;
import oshi.hardware.HWDiskStore;
import oshi.hardware.NetworkIF;
import oshi.software.os.OperatingSystem;
import org.springframework.stereotype.Service;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.ThreadMXBean;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 系统监控服务实现
 */
@Service
public class SystemMonitorServiceImpl implements SystemMonitorService {

    private final SystemInfo systemInfo = new SystemInfo();
    private final DecimalFormat df = new DecimalFormat("#.##");

    @Override
    public Map<String, Object> getServerInfo() {
        Map<String, Object> serverInfo = new HashMap<>();

        OperatingSystem os = systemInfo.getOperatingSystem();
        CentralProcessor processor = systemInfo.getHardware().getProcessor();

        // 系统信息
        serverInfo.put("os", os.getFamily());
        serverInfo.put("osVersion", os.getVersionInfo().getVersion());
        serverInfo.put("osArch", os.getBitness() + " bit");
        serverInfo.put("computerName", os.getNetworkParams().getHostName());
        serverInfo.put("computerIp", getLocalIpAddress());

        // CPU信息
        serverInfo.put("processor", processor.getProcessorIdentifier().getName());
        serverInfo.put("processorCores", processor.getLogicalProcessorCount());
        serverInfo.put("processorPhysicalCores", processor.getPhysicalProcessorCount());

        // 运行时间
        serverInfo.put("systemUptime", formatUptime(os.getSystemUptime()));

        return serverInfo;
    }

    @Override
    public Map<String, Object> getCpuInfo() {
        Map<String, Object> cpuInfo = new HashMap<>();

        CentralProcessor processor = systemInfo.getHardware().getProcessor();

        // 获取CPU使用率
        double[] loadAverage = processor.getSystemLoadAverage(3);
        cpuInfo.put("loadAverage", loadAverage);

        // 获取CPU使用率百分比
        double cpuUsage = processor.getSystemCpuLoadBetweenTicks() * 100;
        cpuInfo.put("cpuUsage", df.format(cpuUsage));

        // CPU核心数
        cpuInfo.put("logicalCores", processor.getLogicalProcessorCount());
        cpuInfo.put("physicalCores", processor.getPhysicalProcessorCount());

        // CPU型号
        cpuInfo.put("model", processor.getProcessorIdentifier().getName());
        cpuInfo.put("vendor", processor.getProcessorIdentifier().getVendor());
        cpuInfo.put("mhz", processor.getProcessorIdentifier().getVendorFreq());

        return cpuInfo;
    }

    @Override
    public Map<String, Object> getMemoryInfo() {
        Map<String, Object> memInfo = new HashMap<>();

        GlobalMemory memory = systemInfo.getHardware().getMemory();

        long total = memory.getTotal();
        long available = memory.getAvailable();
        long used = total - available;

        memInfo.put("total", formatBytes(total));
        memInfo.put("available", formatBytes(available));
        memInfo.put("used", formatBytes(used));
        memInfo.put("usage", df.format((double) used / total * 100) + "%");

        // 交换区
        long swapTotal = memory.getVirtualMemory().getSwapTotal();
        long swapUsed = memory.getVirtualMemory().getSwapUsed();
        memInfo.put("swapTotal", formatBytes(swapTotal));
        memInfo.put("swapUsed", formatBytes(swapUsed));
        memInfo.put("swapUsage", df.format((double) swapUsed / swapTotal * 100) + "%");

        return memInfo;
    }

    @Override
    public Map<String, Object> getDiskInfo() {
        Map<String, Object> diskInfo = new HashMap<>();

        List<HWDiskStore> diskStores = systemInfo.getHardware().getDiskStores();
        List<Map<String, Object>> disks = new ArrayList<>();

        long total = 0;
        long used = 0;

        for (HWDiskStore disk : diskStores) {
            Map<String, Object> diskData = new HashMap<>();
            diskData.put("name", disk.getName());
            diskData.put("model", disk.getModel());
            diskData.put("size", formatBytes(disk.getSize()));

            long diskSize = disk.getSize();
            total += diskSize;

            // 获取分区信息
            var partitions = disk.getPartitions();
            if (!partitions.isEmpty()) {
                long partitionSize = partitions.get(0).getSize();
                used += partitionSize;
                diskData.put("used", formatBytes(partitionSize));
                diskData.put("usage", df.format((double) partitionSize / diskSize * 100) + "%");
            }

            disks.add(diskData);
        }

        diskInfo.put("disks", disks);
        diskInfo.put("total", formatBytes(total));
        diskInfo.put("used", formatBytes(used));
        diskInfo.put("usage", df.format((double) used / total * 100) + "%");

        return diskInfo;
    }

    @Override
    public Map<String, Object> getJvmInfo() {
        Map<String, Object> jvmInfo = new HashMap<>();

        Runtime runtime = Runtime.getRuntime();
        MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
        ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();

        // JVM内存
        long maxMemory = runtime.maxMemory();
        long totalMemory = runtime.totalMemory();
        long freeMemory = runtime.freeMemory();
        long usedMemory = totalMemory - freeMemory;

        jvmInfo.put("maxMemory", formatBytes(maxMemory));
        jvmInfo.put("totalMemory", formatBytes(totalMemory));
        jvmInfo.put("usedMemory", formatBytes(usedMemory));
        jvmInfo.put("freeMemory", formatBytes(freeMemory));
        jvmInfo.put("usage", df.format((double) usedMemory / maxMemory * 100) + "%");

        // JVM版本
        jvmInfo.put("version", System.getProperty("java.version"));
        jvmInfo.put("vendor", System.getProperty("java.vendor"));
        jvmInfo.put("home", System.getProperty("java.home"));

        // JVM运行时间
        long uptime = ManagementFactory.getRuntimeMXBean().getUptime();
        jvmInfo.put("uptime", formatUptime(uptime / 1000));

        // 线程数
        jvmInfo.put("threadCount", threadMXBean.getThreadCount());
        jvmInfo.put("peakThreadCount", threadMXBean.getPeakThreadCount());
        jvmInfo.put("daemonThreadCount", threadMXBean.getDaemonThreadCount());

        // 类加载
        jvmInfo.put("loadedClassCount", ManagementFactory.getClassLoadingMXBean().getLoadedClassCount());
        jvmInfo.put("totalLoadedClassCount", ManagementFactory.getClassLoadingMXBean().getTotalLoadedClassCount());

        // JVM参数
        jvmInfo.put("inputArguments", ManagementFactory.getRuntimeMXBean().getInputArguments());

        // GC信息
        List<Map<String, String>> gcInfos = new ArrayList<>();
        for (var gc : ManagementFactory.getGarbageCollectorMXBeans()) {
            Map<String, String> gcInfo = new HashMap<>();
            gcInfo.put("name", gc.getName());
            gcInfo.put("collectionCount", String.valueOf(gc.getCollectionCount()));
            gcInfo.put("collectionTime", String.valueOf(gc.getCollectionTime()) + "ms");
            gcInfos.add(gcInfo);
        }
        jvmInfo.put("gcInfos", gcInfos);

        return jvmInfo;
    }

    @Override
    public Map<String, Object> getNetworkInfo() {
        Map<String, Object> networkInfo = new HashMap<>();

        List<NetworkIF> networkIFs = systemInfo.getHardware().getNetworkIFs();
        List<Map<String, Object>> networks = new ArrayList<>();

        for (NetworkIF networkIF : networkIFs) {
            Map<String, Object> netData = new HashMap<>();
            netData.put("name", networkIF.getName());
            netData.put("displayName", networkIF.getDisplayName());
            netData.put("ipv4", networkIF.getIPv4addr());
            netData.put("ipv6", networkIF.getIPv6addr());
            netData.put("mac", networkIF.getMacaddr());
            netData.put("bytesRecv", formatBytes(networkIF.getBytesRecv()));
            netData.put("bytesSent", formatBytes(networkIF.getBytesSent()));
            netData.put("speed", formatSpeed(networkIF.getSpeed()));
            networks.add(netData);
        }

        networkInfo.put("networks", networks);

        return networkInfo;
    }

    /**
     * 格式化字节数
     */
    private String formatBytes(long bytes) {
        if (bytes < 1024) {
            return bytes + " B";
        } else if (bytes < 1024 * 1024) {
            return df.format(bytes / 1024.0) + " KB";
        } else if (bytes < 1024 * 1024 * 1024) {
            return df.format(bytes / 1024.0 / 1024) + " MB";
        } else if (bytes < 1024L * 1024 * 1024 * 1024) {
            return df.format(bytes / 1024.0 / 1024 / 1024) + " GB";
        } else {
            return df.format(bytes / 1024.0 / 1024 / 1024 / 1024) + " TB";
        }
    }

    /**
     * 格式化速度
     */
    private String formatSpeed(long speed) {
        if (speed == 0) {
            return "Unknown";
        }
        return formatBytes(speed) + "/s";
    }

    /**
     * 格式化运行时间
     */
    private String formatUptime(long seconds) {
        long days = seconds / 86400;
        long hours = (seconds % 86400) / 3600;
        long minutes = (seconds % 3600) / 60;

        return String.format("%d天 %d小时 %d分钟", days, hours, minutes);
    }

    /**
     * 获取本地IP地址
     */
    private String getLocalIpAddress() {
        try {
            List<NetworkIF> networkIFs = systemInfo.getHardware().getNetworkIFs();
            for (NetworkIF networkIF : networkIFs) {
                if (!networkIF.getIPv4addr().isEmpty()) {
                    String ip = networkIF.getIPv4addr().get(0);
                    if (!ip.startsWith("127.") && !ip.startsWith("169.254.")) {
                        return ip;
                    }
                }
            }
        } catch (Exception e) {
            // Ignore
        }
        return "Unknown";
    }
}
