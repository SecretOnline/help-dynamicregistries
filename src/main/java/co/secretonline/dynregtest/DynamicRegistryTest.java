package co.secretonline.dynregtest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.fabricmc.fabric.api.event.registry.DynamicRegistries;
import net.fabricmc.fabric.api.event.registry.DynamicRegistrySetupCallback;
import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;

public class DynamicRegistryTest implements ModInitializer {
	public static final String MOD_ID = "dynamic-registry-test";

	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static ResourceKey<Registry<TestValue>> REGISTRY_KEY = ResourceKey
			.createRegistryKey(Identifier.fromNamespaceAndPath(DynamicRegistryTest.MOD_ID, "test_registry"));

	private long tickNumber = 0;

	static {
		DynamicRegistries.registerSynced(REGISTRY_KEY, TestValue.CODEC);
	}

	@Override
	public void onInitialize() {

		ServerWorldEvents.LOAD.register((minecraft, level) -> {
			level.registryAccess().get(REGISTRY_KEY).ifPresentOrElse(
					registry -> LOGGER.info("Server load: registry has {} values", registry.value().size()),
					() -> LOGGER.warn("Server load: registry not found"));
		});

		ServerTickEvents.END_WORLD_TICK.register((level) -> {
			tickNumber++;
			if (tickNumber != 100) {
				return;
			}

			level.registryAccess().get(REGISTRY_KEY).ifPresentOrElse(
					registry -> LOGGER.info("Server 100th tick: registry has {} values", registry.value().size()),
					() -> LOGGER.warn("Server 100th tick: registry not found"));
		});

		DynamicRegistrySetupCallback.EVENT.register((view) -> {
			view.registerEntryAdded(DynamicRegistryTest.REGISTRY_KEY,
					(int rawId, Identifier id, DynamicRegistryTest.TestValue object) -> DynamicRegistryTest.LOGGER
							.info("Server registering: {}", id.toString()));

			view.getOptional(DynamicRegistryTest.REGISTRY_KEY).ifPresentOrElse(
					registry -> DynamicRegistryTest.LOGGER.info("Server registry setup: registry has {} values", registry.size()),
					() -> DynamicRegistryTest.LOGGER.warn("Server registry setup: registry not found"));
		});
	}

	public record TestValue(Identifier id) {
		public static Codec<TestValue> CODEC = RecordCodecBuilder.create(instance -> instance.group(
				Identifier.CODEC.fieldOf("id").forGetter(TestValue::id)).apply(instance, TestValue::new));
	}
}
