<template>
  <div id="app">
    <img alt="Vue logo" src="./assets/logo.png">
    <HelloWorld msg="Welcome to Your Vue.js App"/>
    <hr/>
    <div><slotHome/></div>
    <div><slotHome2/></div>
    <div><slotHome3/></div>
    <div><slotHome4/></div>
  </div>
</template>

<script>
import HelloWorld from './components/HelloWorld.vue'
import slotHome from "./components/slot/home1.vue"
import slotHome2 from "./components/slot/home2.vue"
import slotHome3 from "./components/slot/home3.vue"
import slotHome4 from "./components/slot/home4.vue"

export default {
  name: 'App',
  components: {
    HelloWorld,
    slotHome,
    slotHome2,
    slotHome3,
    slotHome4
  }
}
</script>

<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  margin-top: 60px;
}
</style>
