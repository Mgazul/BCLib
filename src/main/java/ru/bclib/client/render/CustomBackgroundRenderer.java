package ru.bclib.client.render;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.material.FogType;
import ru.bclib.api.BiomeAPI;
import ru.bclib.util.BackgroundInfo;
import ru.bclib.util.MHelper;
import ru.bclib.world.biomes.BCLBiome;

public class CustomBackgroundRenderer {
	private static final MutableBlockPos LAST_POS = new MutableBlockPos(0, -100, 0);
	private static final MutableBlockPos MUT_POS = new MutableBlockPos();
	private static final float[] FOG_DENSITY = new float[8];
	private static final int GRID_SIZE = 32;
	
	public static boolean applyFogDensity(Camera camera, FogRenderer.FogMode fogMode, float viewDistance, boolean thickFog) {
		Entity entity = camera.getEntity();
		FogType fogType = camera.getFluidInCamera();
		if (fogType != FogType.WATER) {
			if (shouldIgnore(entity.level, (int) entity.getX(), (int) entity.getEyeY(), (int) entity.getZ())) {
				return false;
			}
			float fog = getFogDensity(entity.level, entity.getX(), entity.getEyeY(), entity.getZ());
			BackgroundInfo.fogDensity = fog;
			float start = viewDistance * 0.75F / fog;
			float end = viewDistance / fog;
			
			if (entity instanceof LivingEntity) {
				LivingEntity livingEntity = (LivingEntity) entity;
				MobEffectInstance effect = livingEntity.getEffect(MobEffects.BLINDNESS);
				if (effect != null) {
					int duration = effect.getDuration();
					if (duration > 20) {
						start = 0;
						end *= 0.03F;
						BackgroundInfo.blindness = 1;
					}
					else {
						float delta = (float) duration / 20F;
						BackgroundInfo.blindness = delta;
						start = Mth.lerp(delta, start, 0);
						end = Mth.lerp(delta, end, end * 0.03F);
					}
				}
				else {
					BackgroundInfo.blindness = 0;
				}
			}
			
			RenderSystem.setShaderFogStart(start);
			RenderSystem.setShaderFogEnd(end);
			return true;
		}
		return false;
	}
	
	private static boolean shouldIgnore(Level level, int x, int y, int z) {
		Biome biome = level.getBiome(MUT_POS.set(x, y, z));
		return BiomeAPI.getRenderBiome(biome) == BiomeAPI.EMPTY_BIOME;
	}
	
	private static float getFogDensityI(Level level, int x, int y, int z) {
		Biome biome = level.getBiome(MUT_POS.set(x, y, z));
		BCLBiome renderBiome = BiomeAPI.getRenderBiome(biome);
		return renderBiome.getFogDensity();
	}
	
	private static float getFogDensity(Level level, double x, double y, double z) {
		int x1 = MHelper.floor(x / GRID_SIZE) * GRID_SIZE;
		int y1 = MHelper.floor(y / GRID_SIZE) * GRID_SIZE;
		int z1 = MHelper.floor(z / GRID_SIZE) * GRID_SIZE;
		float dx = (float) (x - x1) / GRID_SIZE;
		float dy = (float) (y - y1) / GRID_SIZE;
		float dz = (float) (z - z1) / GRID_SIZE;
		
		if (LAST_POS.getX() != x1 || LAST_POS.getY() != y1 || LAST_POS.getZ() != z1) {
			int x2 = x1 + GRID_SIZE;
			int y2 = y1 + GRID_SIZE;
			int z2 = z1 + GRID_SIZE;
			LAST_POS.set(x1, y1, z1);
			FOG_DENSITY[0] = getFogDensityI(level, x1, y1, z1);
			FOG_DENSITY[1] = getFogDensityI(level, x2, y1, z1);
			FOG_DENSITY[2] = getFogDensityI(level, x1, y2, z1);
			FOG_DENSITY[3] = getFogDensityI(level, x2, y2, z1);
			FOG_DENSITY[4] = getFogDensityI(level, x1, y1, z2);
			FOG_DENSITY[5] = getFogDensityI(level, x2, y1, z2);
			FOG_DENSITY[6] = getFogDensityI(level, x1, y2, z2);
			FOG_DENSITY[7] = getFogDensityI(level, x2, y2, z2);
		}
		
		float a = Mth.lerp(dx, FOG_DENSITY[0], FOG_DENSITY[1]);
		float b = Mth.lerp(dx, FOG_DENSITY[2], FOG_DENSITY[3]);
		float c = Mth.lerp(dx, FOG_DENSITY[4], FOG_DENSITY[5]);
		float d = Mth.lerp(dx, FOG_DENSITY[6], FOG_DENSITY[7]);
		
		a = Mth.lerp(dy, a, b);
		b = Mth.lerp(dy, c, d);
		
		return Mth.lerp(dz, a, b);
	}
}