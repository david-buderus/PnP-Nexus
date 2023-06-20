<script>
    import Fa from 'svelte-fa'
    import { faBookAtlas } from '@fortawesome/free-solid-svg-icons'

	import NavbarEntry from './NavbarEntry.svelte';
  </script>
  

<div id="navbar" class="h-12 bg-violet-400 flex">
    
    <NavbarEntry icon={faBookAtlas} heading="P&P Manager" destination="/"></NavbarEntry>
    <NavbarEntry heading="Test Me" destination="/test"></NavbarEntry>
</div>

