package cookie.villages.core.entity;

import net.minecraft.core.WeightedRandomLootObject;
import net.minecraft.core.entity.EntityLightning;
import net.minecraft.core.entity.MobPathfinder;
import net.minecraft.core.entity.animal.Creature;
import net.minecraft.core.entity.monster.MobZombiePig;
import net.minecraft.core.item.Items;
import net.minecraft.core.util.collection.NamespaceID;
import net.minecraft.core.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class MobPigman extends MobPathfinder implements Creature {
	public List<WeightedRandomLootObject> burningMobDrops = new ArrayList<>();

	public MobPigman(@Nullable World world) {
		super(world);
		scoreValue = 10;
		textureIdentifier = NamespaceID.getPermanent("minecraft", "pigman");
		this.moveSpeed = 0.5F;
		this.scoreValue = 100;

		this.mobDrops.add(new WeightedRandomLootObject(Items.FOOD_PORKCHOP_RAW.getDefaultStack(), 1, 2));
		this.burningMobDrops.add(new WeightedRandomLootObject(Items.FOOD_PORKCHOP_COOKED.getDefaultStack(), 1, 2));
	}

	@Override
	public String getLivingSound() {
		return "mob.pig";
	}

	@Override
	protected String getHurtSound() {
		return "mob.pig";
	}

	@Override
	protected String getDeathSound() {
		return "mob.pigdeath";
	}

	@Override
	public void thunderHit(EntityLightning bolt) {
		if (world == null || world.isClientSide) return;

		MobZombiePig entitypigzombie = new MobZombiePig(this.world);
		entitypigzombie.moveTo(this.x, this.y, this.z, this.yRot, this.xRot);
		this.world.entityJoinedWorld(entitypigzombie);
		this.remove();
	}

	@Override
	protected List<WeightedRandomLootObject> getMobDrops() {
		return this.remainingFireTicks > 0 ? this.burningMobDrops : this.mobDrops;
	}

	@Override
	protected boolean canDespawn() {
		return false;
	}
}
