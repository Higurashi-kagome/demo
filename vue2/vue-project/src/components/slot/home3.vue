<template>
  <slotTest3>
    <template v-slot:header>
      <h1>Here might be a page title</h1>
    </template>

    <p>A paragraph for the main content.</p>
    <p>And another one.</p>

    <template v-slot:footer>
      <p>Here some contact info</p>
    </template>
  </slotTest3>
</template>

<script>
import slotTest3 from "./test3.vue";
export default {
  name: 'slotHome3',
  components: {
    slotTest3
  },
  data() {
    return {
      
    }
  }
}
</script>
<style lang="less" scoped>

</style>