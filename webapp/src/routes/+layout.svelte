<script>
	import Navbar from "$lib/Navbar.svelte";
  import "../app.css";

  export const ssr = false;
</script>

<Navbar/>
<slot />