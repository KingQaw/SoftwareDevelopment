<script setup>
import '@wangeditor/editor/dist/css/style.css';
import { Editor, Toolbar } from '@wangeditor/editor-for-vue';
import { ref, onBeforeUnmount, onMounted, shallowRef, watch } from 'vue';
import { DomEditor } from '@wangeditor/editor';
const props = defineProps({
  height: {
    type: String,
    default: 'calc(100vh - 300px)'
  },
  receiveContent: {
    type: String,
    default: '',
    required: true
  }
})

const emit = defineEmits(['on-receive'])

const editorRef = shallowRef();
const valueHtml = ref('');
const toolbarConfig = {
  excludeKeys: ['fullScreen', 'insertVideo', 'uploadVideo']
};
const editorConfig = {
  placeholder: '期待与你共赴星河。。。',
  MENU_CONF: {
    uploadImage: {
      base64LimitSize: 1024 * 1024 * 1024 * 1024
    }
  }
};
// editorConfig.MENU_CONF['uploadImage'] = {
//   base64LimitSize: 1024 * 1024 * 1024 * 1024
// }
const mode = ref('default');

watch(() => props.receiveContent, (val) => {
  valueHtml.value = val;
}, {
  immediate: true, deep: true
});

watch(valueHtml, (val) => {
  emit('on-receive', val);
})




// 官网要求
// 编辑器创建完成后，保存编辑器实例
const handleCreated = (editor) => {
  editorRef.value = editor;
  console.log(editor.getAllMenuKeys());
}
// 组件销毁时，销毁编辑器
onBeforeUnmount(() => {
  const editor = editorRef.value;
  if (editor) {
    editor.destroy();
  }
});
</script>



<template>
  <div style="border: 1px solid #ccc">
    <Toolbar style="border-bottom: 1px solid #ccc" :editor="editorRef" :defaultConfig="toolbarConfig" :mode="mode" />
    <Editor :style="{ height: height, overflowY: 'hidden', }" v-model="valueHtml" :defaultConfig="editorConfig" :mode="mode"
      @onCreated="handleCreated" />
  </div>
</template>



<style lang="scss" scoped>

</style>