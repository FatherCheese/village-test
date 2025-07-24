package cookie.villages;

import cookie.villages.core.entity.MobPigman;
import net.fabricmc.api.ModInitializer;
import net.minecraft.core.util.collection.NamespaceID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import turniplabs.halplibe.helper.EntityHelper;


public class Villages implements ModInitializer {
    public static final String MOD_ID = "villages";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    @Override
    public void onInitialize() {
		EntityHelper.createEntity(MobPigman.class, NamespaceID.getPermanent(MOD_ID, "pigman"), "Pigman");
        LOGGER.info("Villages initialized.");
    }
}
