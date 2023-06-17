<a class="flex" href={destination}>
    {#if icon !== undefined}
        <Fa class="pl-2 py-2" size="3x" icon={icon} />
    {/if}
    <p class="font-bold ml-4 py-2">{heading}</p>
</a>
  
<script lang="ts">
	import type { IconDefinition } from "@fortawesome/free-solid-svg-icons";
	import Fa from "svelte-fa";

    export let heading :string;
    export let icon: IconDefinition | undefined = undefined;
    export let destination: string;
    
</script>