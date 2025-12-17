package co.secretonline.dynregtest;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientWorldEvents;
import net.fabricmc.fabric.api.event.registry.DynamicRegistrySetupCallback;
import net.minecraft.resources.Identifier;

public class DynamicRegistryTestClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		ClientWorldEvents.AFTER_CLIENT_WORLD_CHANGE.register((minecraft, level) -> {
			level.registryAccess().get(DynamicRegistryTest.REGISTRY_KEY).ifPresentOrElse(
					registry -> DynamicRegistryTest.LOGGER.info("Client world change: registry has {} values",
							registry.value().size()),
					() -> DynamicRegistryTest.LOGGER.warn("Client world change: registry not found"));
		});
		DynamicRegistrySetupCallback.EVENT.register((view) -> {
			view.registerEntryAdded(DynamicRegistryTest.REGISTRY_KEY,
					(int rawId, Identifier id, DynamicRegistryTest.TestValue object) -> DynamicRegistryTest.LOGGER
							.info("Client registering: {}", id.toString()));

			view.getOptional(DynamicRegistryTest.REGISTRY_KEY).ifPresentOrElse(
					registry -> DynamicRegistryTest.LOGGER.info("Client registry setup: registry has {} values", registry.size()),
					() -> DynamicRegistryTest.LOGGER.warn("Client registry setup: registry not found"));
		});
	}
}
