package com.bigchickenstudios.divinature;

import com.bigchickenstudios.divinature.block.ModBlocks;
import com.bigchickenstudios.divinature.client.Setup;
import com.bigchickenstudios.divinature.item.ModItems;
import com.bigchickenstudios.divinature.item.crafting.ModRecipeSerializers;
import com.bigchickenstudios.divinature.tileentity.ModTileEntityTypes;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerAboutToStartEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(Constants.MODID)
public class Divinature  {

    public static final Logger LOGGER = LogManager.getLogger();

    public Divinature() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        bus.addListener(this::clientSetup);

        ModBlocks.init(bus);
        ModItems.init(bus);

        ModRecipeSerializers.init(bus);
        ModTileEntityTypes.init(bus);

        DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> {
            bus.addListener(Setup::preStitch);
            bus.addListener(Setup::blockColours);
            bus.addListener(Setup::itemColours);
        });

        MinecraftForge.EVENT_BUS.addListener(this::onServerAboutToStart);
    }

    private void clientSetup(FMLClientSetupEvent event) {
        Setup.go();
    }

    private void onServerAboutToStart(FMLServerAboutToStartEvent event) {
    }


}
