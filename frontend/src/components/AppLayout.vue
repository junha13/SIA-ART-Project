<template>
  <div id="kt_app_root" class="d-flex flex-column flex-root app-root">
    <!-- Main Content: Content will fill the entire screen, allowing fixed elements (like Footer Nav) to work correctly -->
    <main id="kt_app_content" class="flex-column-fluid app-content">
      <!-- slot 으로 자식 콘텐츠 출력 -->
      <slot />
    </main>
  </div>
</template>

<script setup>
// 이 레이아웃은 이제 Header와 Footer 없이 <slot>만 감쌉니다.
</script>

<style scoped>
/* AppLayout 스타일은 최소화하여 full-screen 레이아웃을 보장합니다. */
.app-content {
  padding: 0 !important; /* 자식 뷰에서 개별 패딩 적용 */
}
</style>
