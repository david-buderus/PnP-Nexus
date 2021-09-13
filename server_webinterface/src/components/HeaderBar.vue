<template>
  <div id="header" class="w-full h-24 bg-gray-900 dark:bg-gray-900 border-b-2 border-black flex justify-between">
    <Menu/>
    <div class="flex justify-end items-center">
        <h1 class="heading hidden sm:flex text-gray-300" >David's fancy PnP-Server</h1>
        <img class="h-3/4 mx-4 cursor-pointer" src="../assets/Icon.png" title="David's fancy PnP-Server"/>
    </div>
  </div>
</template>


<script>
import Menu from './Menu.vue'

export default {
  name: 'HeaderBar',
  components: {
    Menu
  }
}
</script>
