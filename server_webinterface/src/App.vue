<template>
  <div id="root" class="">
    <HeaderBar/>
    <MainPage/>
  </div>
</template>


<script>
import HeaderBar from './components/HeaderBar.vue'
import MainPage from './components/MainPage.vue'


export default {
  name: 'App',
  components: {
    MainPage,
    HeaderBar, 
  }
}
</script>
