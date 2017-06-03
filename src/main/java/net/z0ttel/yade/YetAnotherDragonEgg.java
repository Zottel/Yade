package net.z0ttel.yade;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Level;

import net.minecraft.launchwrapper.Launch;
import net.minecraft.util.ResourceLocation;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import java.io.File;
import java.util.Iterator;
import java.util.Set;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;
import java.lang.reflect.Field;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.world.ExplosionEvent;

import net.minecraftforge.common.config.Configuration;

import net.minecraftforge.fml.relauncher.ReflectionHelper;

import net.minecraft.util.math.BlockPos;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.ai.EntityAITasks;

import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.end.DragonFightManager;
import net.minecraft.world.WorldProviderEnd;
import net.minecraft.entity.boss.EntityDragon;

import net.minecraft.entity.ai.EntityAITasks;

import net.minecraftforge.event.world.WorldEvent;
// WorldEvent.Load
// WorldEvent.Unload


@Mod(modid = YetAnotherDragonEgg.MODID, name = YetAnotherDragonEgg.NAME, version = YetAnotherDragonEgg.VERSION, acceptableRemoteVersions = "*")
public class YetAnotherDragonEgg
{
	public static final String MODID = "yade";
	public static final String NAME = "Yat Another Dragon Egg";
	public static final String VERSION = "1.10.2-0.1";
	
	public static Logger logger;
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		logger = event.getModLog();
		logger.info("[yade]: starting.");
		
		MinecraftForge.EVENT_BUS.register(this);
	}
	
	@SubscribeEvent
	public void onLoadWorld(WorldEvent.Load event) {
		World worldIn = event.getWorld();
		if(!worldIn.isRemote && worldIn.provider instanceof WorldProviderEnd) {
			logger.info("[yade]: an END has been loaded - overwriting DragonFightManager.");
			WorldProviderEnd worldProv = (WorldProviderEnd) worldIn.provider;
			DragonFightManager oldFightManager = worldProv.getDragonFightManager();
			BetterDragonFightManager fightManager = new BetterDragonFightManager((WorldServer)worldIn, oldFightManager.getCompound());
			ReflectionHelper.setPrivateValue(WorldProviderEnd.class, worldProv, fightManager, "dragonFightManager", "field_186064_g");
		}
	}
	
	public class BetterDragonFightManager extends DragonFightManager {
		public BetterDragonFightManager(WorldServer worldIn, NBTTagCompound compound) {
			super(worldIn, compound);
		}
		
		public void processDragonDeath(EntityDragon dragon) {
			//ReflectionHelper.setPrivateValue(DragonFightManager.class, this, false, "scanForLegacyFight", "field_186120_n");
			ReflectionHelper.setPrivateValue(DragonFightManager.class, this, false, "previouslyKilled", "field_186118_l");
			
			super.processDragonDeath(dragon);
		}
	}
}

