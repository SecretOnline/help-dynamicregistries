package co.secretonline.dynregtest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.fabricmc.fabric.api.event.registry.DynamicRegistries;
import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;

public class DynamicRegistryTest implements ModInitializer {
	public static final String MOD_ID = "dynamic-registry-test";

	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static ResourceKey<Registry<TestValue>> REGISTRY_KEY = ResourceKey
			.createRegistryKey(Identifier.fromNamespaceAndPath(DynamicRegistryTest.MOD_ID, "test_registry"));

	@Override
	public void onInitialize() {
		DynamicRegistries.registerSynced(REGISTRY_KEY, TestValue.CODEC);

		ServerWorldEvents.LOAD.register((minecraft, level) -> {
			level.registryAccess().get(REGISTRY_KEY).ifPresentOrElse(
					registry -> LOGGER.info("Server: registry has {} values", registry.value().size()),
					() -> LOGGER.warn("Server: registry not found"));
		});
	}

	public record TestValue(Identifier id) {
		public static Codec<TestValue> CODEC = RecordCodecBuilder.create(instance -> instance.group(
				Identifier.CODEC.fieldOf("id").forGetter(TestValue::id)).apply(instance, TestValue::new));
	}
}
