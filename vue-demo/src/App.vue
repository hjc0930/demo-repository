<script setup lang="ts">
import { ref } from "vue";
import ButtonComponent from './components/ButtonComponent.vue';

const enum BusinessStatusEnum {
  SUCCESS = "SUCCESS",
  WARNING = "WARNING",
  ERROR = "ERROR"
}

interface aaa {
  b?: BusinessStatusEnum
}

const a: aaa = {
  b: BusinessStatusEnum.WARNING
}

const is = a.b === BusinessStatusEnum.ERROR

const count = ref(0);

const onAdd = () => {
  count.value++;
}

</script>

<template>
  <h1>You did it!</h1>
  <p>{{ count }}</p>
  <p>
    Visit <a href="https://vuejs.org/" target="_blank" rel="noopener">vuejs.org</a> to read the
    documentation
  </p>
  <ButtonComponent @parent-count="onAdd">
    <!-- <template #header>
      header123123
    </template> -->
  </ButtonComponent>
</template>

<style scoped></style>
