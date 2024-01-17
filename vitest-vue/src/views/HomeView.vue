<script setup lang="ts">
import TheWelcome from '../components/TheWelcome.vue'
import {ref} from "vue"

const value = ref("");

const onInput = (event: any) => {
  console.log(event);

  value.value = event.target.value
}

const onSet = () => {
  value.value = "555555555"
}
</script>

<template>
  <main>
    <TheWelcome />
    <input type="text" :value="value"
  @input="onInput">
    <div>{{ value }}</div>
    <button @click="onSet">Click</button>
  </main>
</template>
