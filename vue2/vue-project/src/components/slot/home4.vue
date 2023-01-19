<template>
  <div>
    <slot-test-4 v-slot:default="slotProps">
      {{slotProps.userText.firstName}}
    </slot-test-4>
    <slot-test-4 v-slot="slotProps">
      {{slotProps.userText.firstName}}
    </slot-test-4>
  </div>
</template>

<script>
import slotTest4 from "./test4.vue";
export default {
  name: 'slotHome4',
  components: {
    slotTest4
  },
  data() {
    return {
      
    }
  }
}
</script>
<style lang="less" scoped>

</style>