<template>
  <slotTest>
    {{text}}
  </slotTest>
</template>

<script>
import slotTest from "./test1.vue";
export default {
  name: 'slotHome',
  components: {
    slotTest
  },
  data() {
    return {
      text: 'Hello Word'
    }
  }
}
</script>
<style lang="less" scoped>

</style>