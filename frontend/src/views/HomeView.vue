<template>
  <div>
    <div v-if="loading">Loading...</div>
    <div v-else-if="error">Error: {{ error.message }}</div>
    <div v-else>
      <h2>Post Title: {{ data.title }}</h2>
      <p>Content: {{ data.body }}</p>
    </div>
  </div>
</template>

<script setup>
import { useAxios } from '@/js/useApi';

const { data, error, loading } = useAxios('/test');
// api 끌어쓰기 예시
</script>

<style scoped>
</style>