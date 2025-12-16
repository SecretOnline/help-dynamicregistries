package co.secretonline.dynregtest;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientWorldEvents;

public class DynamicRegistryTestClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		ClientWorldEvents.AFTER_CLIENT_WORLD_CHANGE.register((minecraft, level) -> {
			level.registryAccess().get(DynamicRegistryTest.REGISTRY_KEY).ifPresentOrElse(
					registry -> DynamicRegistryTest.LOGGER.info("Client: registry has {} values", registry.value().size()),
					() -> DynamicRegistryTest.LOGGER.warn("Client: registry not found"));
		});
	}
}
