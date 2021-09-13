<template>
    <Slide title="Menu">
        <div id="appearance" class="flex flex-col my-4">
            <label class="mb-4">Appearance</label>
            <dark-mode-toggle class="ml-10"/>
        </div>
    </Slide>     
</template>

<script>
import { Slide } from 'vue3-burger-menu'
import DarkModeToggle from './DarkModeToggle.vue'
export default {
  name: 'BurgerMenu',
  components: {
    Slide,
    DarkModeToggle
  },
  setup() {
  
  }
}
</script>