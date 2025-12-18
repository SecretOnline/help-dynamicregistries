# dynregtest

This repo is me trying to get help with Dynamic Registries.

My issue was that I was calling `level.registryAccess().get(REGISTRY_KEY)`. I should have been calling `level.registryAccess().lookup(REGISTRY_KEY)`. This confusion may have come from me reading code using Yarn mappings and then trying to apply it to Mojmaps, but it's been many hours over two days of trying to figure this out and I have lost my original context.

I will keep this repo public, but archived, in case anybody needs to find this in the future.
