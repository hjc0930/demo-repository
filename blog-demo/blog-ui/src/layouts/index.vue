<script setup lang="ts">
import { ref, onMounted } from "vue";
import { RouterView, useRoute } from "vue-router";
import { routes } from "@/router";
import LogoSvg from "@/assets/logo.svg";

const route = useRoute();

const isAsideExport = ref(true);
const defaultMenuActive = ref("/");

onMounted(() => {
  defaultMenuActive.value = route.path;
});

const onVisibleAside = () => {
  isAsideExport.value = !isAsideExport.value;
};
</script>

<template>
  <el-container class="container">
    <ElAside width="auto" class="aside">
      <div class="title-wrap">
        <img :src="LogoSvg" class="logo" />
        <h3
          :class="[
            'title',
            isAsideExport ? 'title-scale-enable' : 'title-scale-close',
          ]"
        >
          Back System
        </h3>
      </div>
      <el-menu
        mode="vertical"
        class="menu"
        :router="true"
        :default-active="defaultMenuActive"
        :collapse="!isAsideExport"
        active-text-color="#ffd04b"
        background-color="#545c64"
        text-color="#fff"
      >
        <ElMenuItem
          v-for="(item, index) in routes[0].children"
          :index="item.path"
          :key="index"
        >
          <el-icon>
            <component :is="(item as any).iconName"></component>
          </el-icon>
          <span>{{ item.name }}</span>
        </ElMenuItem>
      </el-menu>
    </ElAside>
    <el-container>
      <el-header class="header">
        <el-icon :size="22" @click="onVisibleAside" v-if="isAsideExport">
          <Fold style="cursor: pointer;" />
        </el-icon>
        <el-icon :size="22" @click="onVisibleAside" v-else>
          <Expand style="cursor: pointer;" />
        </el-icon>
      </el-header>
      <el-divider style="margin: 0" />
      <el-main>
        <RouterView />
      </el-main>
    </el-container>
  </el-container>
</template>

<style scoped>
.container {
  height: 100vh;

  & .aside {
    padding-top: 16px;
    transition: width 0.4s;
    background-color: #545c64;

    & .title-wrap {
      display: flex;
      justify-content: center;
      column-gap: 4px;
      & .logo {
        width: 22px;
      }
      & .title {
        height: 22px;
        color: #fff;
        text-align: center;
        font-size: 16px;
      }
      & .title-scale-enable {
        transform: scale(0);
        animation: scale-text-enable 0.2s forwards;
      }
      & .title-scale-close {
        animation: scale-text-close 0.2s forwards;
      }
    }

    & .menu {
      border-right: none;
    }
    & .menu:not(.el-menu--collapse) {
      width: 200px;
    }
  }

  & .header {
    display: flex;
    align-items: center;
  }
}

@keyframes scale-text-enable {
  0% {
    font-size: 0;
    transform: scale(0);
  }
  100% {
    font-size: inherit;
    transform: scale(1);
  }
}

@keyframes scale-text-close {
  0% {
    font-size: inherit;
    transform: scale(1);
  }
  100% {
    font-size: 0;
    transform: scale(0);
  }
}
</style>
