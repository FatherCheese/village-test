package cookie.villages.client.render.model;

import cookie.villages.core.entity.MobPigman;
import net.minecraft.client.render.entity.MobRenderer;
import net.minecraft.client.render.model.ModelBiped;
import org.lwjgl.opengl.GL11;

public class MobRendererPigman extends MobRenderer<MobPigman> {
	public MobRendererPigman() {
		super(new ModelBiped(), 0.5f);
	}

	@Override
	protected void setupScale(MobPigman entity, float partialTick) {
		float scale = 0.9375F;
		GL11.glScalef(scale, scale, scale);
	}
}
