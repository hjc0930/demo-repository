<script setup lang="ts">
defineEmits<{
  parentCount: VoidFunction
}>()
</script>

<template>
  <h1 v-if="$slots.header">
    <slot name="header" />
  </h1>
  <button @click="$emit('parentCount')">
    <slot>Count</slot>
  </button>

</template>