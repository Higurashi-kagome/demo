<template>
  <div>
    <slot v-bind:userText="user">{{user.lastName}}</slot>
  </div>
</template>

<style lang="less" scoped>

</style>

<script>
export default {
  name: 'slotTest4',
  data(){
    return{
      user:{
        firstName:"Fan",
        lastName:"Jun"
      }
    }
  }
}
</script>