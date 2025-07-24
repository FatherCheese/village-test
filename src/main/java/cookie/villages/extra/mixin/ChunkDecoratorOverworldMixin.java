package cookie.villages.extra.mixin;

import cookie.villages.core.world.generate.feature.WorldFeatureNotchVillage;
import net.minecraft.core.world.World;
import net.minecraft.core.world.chunk.Chunk;
import net.minecraft.core.world.generate.chunk.ChunkDecorator;
import net.minecraft.core.world.generate.chunk.perlin.overworld.ChunkDecoratorOverworld;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

@Mixin(value = ChunkDecoratorOverworld.class, remap = false)
public abstract class ChunkDecoratorOverworldMixin implements ChunkDecorator {

	@Shadow
	@Final
	private World world;

	@Inject(method = "decorate", at = @At("HEAD"))
	private void villages_decorate(@NotNull Chunk chunk, CallbackInfo ci) {
		world.scheduledUpdatesAreImmediate = true;
		int chunkX = chunk.xPosition;
		int chunkZ = chunk.zPosition;

		int x = chunkX * 16;
		int z = chunkZ * 16;
		int y = world.getHeightValue(x + 16, z + 16);

		Random rand = new Random(world.getRandomSeed());
		long l1 = rand.nextLong() / 2L * 2L + 1L;
		long l2 = rand.nextLong() / 2L * 2L + 1L;
		rand.setSeed((long)chunkX * l1 + (long)chunkZ * l2 ^ world.getRandomSeed());

		int regionX = chunkX / 4;  // Every 4x4 chunk area becomes one "region"
		int regionZ = chunkZ / 4;

		Random regionRandom = new Random(world.getRandomSeed() ^ (regionX * 341873128712L + regionZ * 132897987541L));

		if (regionRandom.nextInt(12) == 0) {  // 12.5% chance for villages in this region
			int villageCount = regionRandom.nextInt(3) + 1;  // 1-3 villages per region
			for (int i = 0; i < villageCount; i++) {
				// Randomize position within the region
				int offsetX = regionRandom.nextInt(32);  // 0-31 block offset
				int offsetZ = regionRandom.nextInt(32);
				new WorldFeatureNotchVillage().place(world, rand, x + offsetX, y, z + offsetZ);
			}
		}

		world.scheduledUpdatesAreImmediate = false;
	}
}
