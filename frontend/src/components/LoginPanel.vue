<template>
  <div class="login-panel">
    <h3>用户登录</h3>
    <p>使用企业账号访问系统</p>
    <form @submit.prevent="submit">
      <label>
        用户名
        <input v-model="form.username" placeholder="请输入用户名" required />
      </label>
      <label>
        密码
        <input v-model="form.password" type="password" placeholder="请输入密码" required />
      </label>
      <button class="primary" type="submit">登录</button>
      <p class="hint">默认账号由管理员创建，支持 JWT 认证。</p>
      <p v-if="message" class="message">{{ message }}</p>
    </form>
  </div>
</template>

<script setup>
import { reactive, ref } from 'vue';
import { login } from '../services/api';

const emit = defineEmits(['login-success']);
const form = reactive({
  username: '',
  password: ''
});
const message = ref('');

const submit = async () => {
  try {
    await login(form.username, form.password);
    message.value = '登录成功，正在刷新资产...';
    emit('login-success');
  } catch (error) {
    message.value = '登录失败，请检查账号或网络连接。';
  }
};
</script>
