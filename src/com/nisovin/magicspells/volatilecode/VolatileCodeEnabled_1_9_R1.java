package com.nisovin.magicspells.volatilecode;

import java.io.File;
import java.io.FileWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import net.minecraft.server.v1_9_R1.*;
import net.minecraft.server.v1_9_R1.PacketPlayOutTitle.EnumTitleAction;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.attribute.AttributeModifier.Operation;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_9_R1.CraftServer;
import org.bukkit.craftbukkit.v1_9_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_9_R1.entity.CraftFallingSand;
import org.bukkit.craftbukkit.v1_9_R1.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.v1_9_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_9_R1.entity.CraftTNTPrimed;
import org.bukkit.craftbukkit.v1_9_R1.inventory.CraftItemStack;
import org.bukkit.entity.Creature;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityTargetEvent.TargetReason;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.nisovin.magicspells.MagicSpells;
import com.nisovin.magicspells.util.BoundingBox;
import com.nisovin.magicspells.util.DisguiseManager;
import com.nisovin.magicspells.util.MagicConfig;

public class VolatileCodeEnabled_1_9_R1 implements VolatileCodeHandle {

	VolatileCodeDisabled fallback = new VolatileCodeDisabled();
	
	private static NBTTagCompound getTag(ItemStack item) {
		if (item instanceof CraftItemStack) {
			try {
				Field field = CraftItemStack.class.getDeclaredField("handle");
				field.setAccessible(true);
				return ((net.minecraft.server.v1_9_R1.ItemStack)field.get(item)).getTag();
			} catch (Exception e) {
			}
		}
		return null;
	}
	
	private static ItemStack setTag(ItemStack item, NBTTagCompound tag) {
		CraftItemStack craftItem = null;
		if (item instanceof CraftItemStack) {
			craftItem = (CraftItemStack)item;
		} else {
			craftItem = CraftItemStack.asCraftCopy(item);
		}
		
		net.minecraft.server.v1_9_R1.ItemStack nmsItem = null;
		try {
			Field field = CraftItemStack.class.getDeclaredField("handle");
			field.setAccessible(true);
			nmsItem = ((net.minecraft.server.v1_9_R1.ItemStack)field.get(item));
		} catch (Exception e) {
		}
		if (nmsItem == null) {
			nmsItem = CraftItemStack.asNMSCopy(craftItem);
		}
		
		if (nmsItem != null) {
			nmsItem.setTag(tag);
			try {
				Field field = CraftItemStack.class.getDeclaredField("handle");
				field.setAccessible(true);
				field.set(craftItem, nmsItem);
			} catch (Exception e) {
			}
		}
		
		return craftItem;
	}
	
	public VolatileCodeEnabled_1_9_R1() {
		try {
			packet63Fields[0] = PacketPlayOutWorldParticles.class.getDeclaredField("a");
			packet63Fields[1] = PacketPlayOutWorldParticles.class.getDeclaredField("b");
			packet63Fields[2] = PacketPlayOutWorldParticles.class.getDeclaredField("c");
			packet63Fields[3] = PacketPlayOutWorldParticles.class.getDeclaredField("d");
			packet63Fields[4] = PacketPlayOutWorldParticles.class.getDeclaredField("e");
			packet63Fields[5] = PacketPlayOutWorldParticles.class.getDeclaredField("f");
			packet63Fields[6] = PacketPlayOutWorldParticles.class.getDeclaredField("g");
			packet63Fields[7] = PacketPlayOutWorldParticles.class.getDeclaredField("h");
			packet63Fields[8] = PacketPlayOutWorldParticles.class.getDeclaredField("i");
			packet63Fields[9] = PacketPlayOutWorldParticles.class.getDeclaredField("j");
			packet63Fields[10] = PacketPlayOutWorldParticles.class.getDeclaredField("k");
			for (int i = 0; i <= 10; i++) {
				packet63Fields[i].setAccessible(true);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		for (EnumParticle particle : EnumParticle.values()) {
			if (particle != null) {
				particleMap.put(particle.b(), particle);
			}
		}
				
	}
	
	@Override
	public void addPotionGraphicalEffect(LivingEntity entity, int color, int duration) {
		/*final EntityLiving el = ((CraftLivingEntity)entity).getHandle();
		final DataWatcher dw = el.getDataWatcher();
		dw.watch(7, Integer.valueOf(color));
		
		if (duration > 0) {
			MagicSpells.scheduleDelayedTask(new Runnable() {
				public void run() {
					int c = 0;
					if (!el.effects.isEmpty()) {
						c = net.minecraft.server.v1_9_R1.PotionBrewer.a(el.effects.values());
					}
					dw.watch(7, Integer.valueOf(c));
				}
			}, duration);
		}*/
	}

	@Override
	public void entityPathTo(LivingEntity creature, LivingEntity target) {
		//EntityCreature entity = ((CraftCreature)creature).getHandle();
		//entity.pathEntity = entity.world.findPath(entity, ((CraftLivingEntity)target).getHandle(), 16.0F, true, false, false, false);
	}

	@Override
	public void sendFakeSlotUpdate(Player player, int slot, ItemStack item) {
		net.minecraft.server.v1_9_R1.ItemStack nmsItem;
		if (item != null) {
			nmsItem = CraftItemStack.asNMSCopy(item);
		} else {
			nmsItem = null;
		}
		PacketPlayOutSetSlot packet = new PacketPlayOutSetSlot(0, (short)slot+36, nmsItem);
		((CraftPlayer)player).getHandle().playerConnection.sendPacket(packet);
	}

	@Override
	public void toggleLeverOrButton(Block block) {
		fallback.toggleLeverOrButton(block);
		//net.minecraft.server.v1_9_R1.Block.getById(block.getType().getId()).interact(((CraftWorld)block.getWorld()).getHandle(), new BlockPosition(block.getX(), block.getY(), block.getZ()), null, 0, 0, 0, 0);
	}

	@Override
	public void pressPressurePlate(Block block) {
		fallback.pressPressurePlate(block);
		//block.setData((byte) (block.getData() ^ 0x1));
		//net.minecraft.server.v1_9_R1.World w = ((CraftWorld)block.getWorld()).getHandle();
		//w.applyPhysics(block.getX(), block.getY(), block.getZ(), net.minecraft.server.v1_9_R1.Block.getById(block.getType().getId()));
		//w.applyPhysics(block.getX(), block.getY()-1, block.getZ(), net.minecraft.server.v1_9_R1.Block.getById(block.getType().getId()));
	}

	@Override
	public boolean simulateTnt(Location target, LivingEntity source, float explosionSize, boolean fire) {
        EntityTNTPrimed e = new EntityTNTPrimed(((CraftWorld)target.getWorld()).getHandle(), target.getX(), target.getY(), target.getZ(), ((CraftLivingEntity)source).getHandle());
        CraftTNTPrimed c = new CraftTNTPrimed((CraftServer)Bukkit.getServer(), e);
        ExplosionPrimeEvent event = new ExplosionPrimeEvent(c, explosionSize, fire);
        Bukkit.getServer().getPluginManager().callEvent(event);
        return event.isCancelled();
	}

	@Override
	public boolean createExplosionByPlayer(Player player, Location location, float size, boolean fire, boolean breakBlocks) {
		return !((CraftWorld)location.getWorld()).getHandle().createExplosion(((CraftPlayer)player).getHandle(), location.getX(), location.getY(), location.getZ(), size, fire, breakBlocks).wasCanceled;
	}

	@Override
	public void playExplosionEffect(Location location, float size) {
		@SuppressWarnings({ "rawtypes", "unchecked" })
		PacketPlayOutExplosion packet = new PacketPlayOutExplosion(location.getX(), location.getY(), location.getZ(), size, new ArrayList(), null);
		for (Player player : location.getWorld().getPlayers()) {
			if (player.getLocation().distanceSquared(location) < 50 * 50) {
				((CraftPlayer)player).getHandle().playerConnection.sendPacket(packet);
			}
		}
	}

	@Override
	public void setExperienceBar(Player player, int level, float percent) {
		PacketPlayOutExperience packet = new PacketPlayOutExperience(percent, player.getTotalExperience(), level);
		((CraftPlayer)player).getHandle().playerConnection.sendPacket(packet);
	}

	@Override
	public Fireball shootSmallFireball(Player player) {
		net.minecraft.server.v1_9_R1.World w = ((CraftWorld)player.getWorld()).getHandle();
		Location playerLoc = player.getLocation();
		Vector loc = player.getEyeLocation().toVector().add(player.getLocation().getDirection().multiply(10));
		
		double d0 = loc.getX() - playerLoc.getX();
        double d1 = loc.getY() - (playerLoc.getY() + 1.5);
        double d2 = loc.getZ() - playerLoc.getZ();
		EntitySmallFireball entitysmallfireball = new EntitySmallFireball(w, ((CraftPlayer)player).getHandle(), d0, d1, d2);

        entitysmallfireball.locY = playerLoc.getY() + 1.5;
        w.addEntity(entitysmallfireball);
        
        return (Fireball)entitysmallfireball.getBukkitEntity();
	}

	@Override
	public void setTarget(LivingEntity entity, LivingEntity target) {
		if (entity instanceof Creature) {
			((Creature)entity).setTarget(target);
		}
		((EntityInsentient)((CraftLivingEntity)entity).getHandle()).setGoalTarget(((CraftLivingEntity)target).getHandle(), TargetReason.CUSTOM, true);
	}

	@Override
	public void playSound(Location location, String sound, float volume, float pitch) {
		for (Player player : location.getWorld().getPlayers()) {
			player.playSound(location, sound, volume, pitch);
		}
	}

	@Override
	public void playSound(Player player, String sound, float volume, float pitch) {
		player.playSound(player.getLocation(), sound, volume, pitch);
	}

	@Override
	public ItemStack addFakeEnchantment(ItemStack item) {
		if (!(item instanceof CraftItemStack)) {
			item = CraftItemStack.asCraftCopy(item);
		}
		NBTTagCompound tag = getTag(item);		
		if (tag == null) {
			tag = new NBTTagCompound();
		}
		if (!tag.hasKey("ench")) {
			tag.set("ench", new NBTTagList());
		}		
		return setTag(item, tag);
	}

	@Override
	public void setFallingBlockHurtEntities(FallingBlock block, float damage, int max) {
		EntityFallingBlock efb = ((CraftFallingSand)block).getHandle();
		try {
			Field field = EntityFallingBlock.class.getDeclaredField("hurtEntities");
			field.setAccessible(true);
			field.setBoolean(efb, true);
			
			field = EntityFallingBlock.class.getDeclaredField("fallHurtAmount");
			field.setAccessible(true);
			field.setFloat(efb, damage);
			
			field = EntityFallingBlock.class.getDeclaredField("fallHurtMax");
			field.setAccessible(true);
			field.setInt(efb, max);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void playEntityAnimation(final Location location, final EntityType entityType, final int animationId, boolean instant) {
		/*final EntityLiving entity;
		if (entityType == EntityType.VILLAGER) {
			entity = new EntityVillager(((CraftWorld)location.getWorld()).getHandle());
		} else if (entityType == EntityType.WITCH) {
			entity = new EntityWitch(((CraftWorld)location.getWorld()).getHandle());
		} else if (entityType == EntityType.OCELOT) {
			entity = new EntityOcelot(((CraftWorld)location.getWorld()).getHandle());
		} else {
			entity = null;
		}
		if (entity == null) return;
		
		entity.setPosition(location.getX(), instant ? location.getY() : -5, location.getZ());
		((CraftWorld)location.getWorld()).getHandle().addEntity(entity);
		entity.addEffect(new MobEffect(14, 40));
		if (instant) {
			((CraftWorld)location.getWorld()).getHandle().broadcastEntityEffect(entity, (byte)animationId);
			entity.getBukkitEntity().remove();
		} else {
			entity.setPosition(location.getX(), location.getY(), location.getZ());
			MagicSpells.scheduleDelayedTask(new Runnable() {
				public void run() {
					((CraftWorld)location.getWorld()).getHandle().broadcastEntityEffect(entity, (byte)animationId);
					entity.getBukkitEntity().remove();
				}
			}, 8);
		}*/
	}

	@Override
	public void createFireworksExplosion(Location location, boolean flicker, boolean trail, int type, int[] colors, int[] fadeColors, int flightDuration) {
		// create item
		net.minecraft.server.v1_9_R1.ItemStack item = new net.minecraft.server.v1_9_R1.ItemStack(Item.getById(401), 1, 0);
		
		// get tag
		NBTTagCompound tag = item.getTag();
		if (tag == null) {
			tag = new NBTTagCompound();
		}
		
		// create explosion tag
		NBTTagCompound explTag = new NBTTagCompound();
		explTag.setByte("Flicker", flicker ? (byte)1 : (byte)0);
		explTag.setByte("Trail", trail ? (byte)1 : (byte)0);
		explTag.setByte("Type", (byte)type);
		explTag.setIntArray("Colors", colors);
		explTag.setIntArray("FadeColors", fadeColors);
		
		// create fireworks tag
		NBTTagCompound fwTag = new NBTTagCompound();
		fwTag.setByte("Flight", (byte)flightDuration);
		NBTTagList explList = new NBTTagList();
		explList.add(explTag);
		fwTag.set("Explosions", explList);
		tag.set("Fireworks", fwTag);
		
		// set tag
		item.setTag(tag);
		
		// create fireworks entity
		EntityFireworks fireworks = new EntityFireworks(((CraftWorld)location.getWorld()).getHandle(), location.getX(), location.getY(), location.getZ(), item);
		((CraftWorld)location.getWorld()).getHandle().addEntity(fireworks);
		
		// cause explosion
		if (flightDuration == 0) {
			((CraftWorld)location.getWorld()).getHandle().broadcastEntityEffect(fireworks, (byte)17);
			fireworks.die();
		}
	}
	
	Field[] packet63Fields = new Field[11];
	Map<String, EnumParticle> particleMap = new HashMap<String, EnumParticle>();
	@Override
	public void playParticleEffect(Location location, String name, float spreadHoriz, float spreadVert, float speed, int count, int radius, float yOffset) {
		playParticleEffect(location, name, spreadHoriz, spreadVert, spreadHoriz, speed, count, radius, yOffset);
	}
	@Override
	public void playParticleEffect(Location location, String name, float spreadX, float spreadY, float spreadZ, float speed, int count, int radius, float yOffset) {
		//location.getWorld().spawnParticle(null, location.getX(), location.getY() + yOffset, location.getZ(), count, spreadX, spreadY, spreadZ, speed);
		PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles();
		EnumParticle particle = particleMap.get(name);
		int[] data = null;
		if (name.contains("_")) {
			String[] split = name.split("_");
			name = split[0] + "_";
			particle = particleMap.get(name);
			if (split.length > 1) {
				String[] split2 = split[1].split(":");
				data = new int[split2.length];
				for (int i = 0; i < data.length; i++) {
					data[i] = Integer.parseInt(split2[i]);
				}
			}
		}
		if (particle == null) {
			MagicSpells.error("Invalid particle: " + name);
			return;
		}
		try {
			packet63Fields[0].set(packet, particle);
			packet63Fields[1].setFloat(packet, (float)location.getX());
			packet63Fields[2].setFloat(packet, (float)location.getY() + yOffset);
			packet63Fields[3].setFloat(packet, (float)location.getZ());
			packet63Fields[4].setFloat(packet, spreadX);
			packet63Fields[5].setFloat(packet, spreadY);
			packet63Fields[6].setFloat(packet, spreadZ);
			packet63Fields[7].setFloat(packet, speed);
			packet63Fields[8].setInt(packet, count);
			packet63Fields[9].setBoolean(packet, radius >= 30);
			if (data != null) {
				packet63Fields[10].set(packet,data);
			}
			int rSq = radius * radius;
			
			for (Player player : location.getWorld().getPlayers()) {
				if (player.getLocation().distanceSquared(location) <= rSq) {
					((CraftPlayer)player).getHandle().playerConnection.sendPacket(packet);
				} else {
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void playDragonDeathEffect(Location location) {
		EntityEnderDragon dragon = new EntityEnderDragon(((CraftWorld)location.getWorld()).getHandle());
		dragon.setPositionRotation(location.getX(), location.getY(), location.getZ(), location.getYaw(), 0F);
		
		PacketPlayOutSpawnEntityLiving packet24 = new PacketPlayOutSpawnEntityLiving(dragon);
		PacketPlayOutEntityStatus packet38 = new PacketPlayOutEntityStatus(dragon, (byte)3);
		final PacketPlayOutEntityDestroy packet29 = new PacketPlayOutEntityDestroy(dragon.getBukkitEntity().getEntityId());
		
		BoundingBox box = new BoundingBox(location, 64);
		final List<Player> players = new ArrayList<Player>();
		for (Player player : location.getWorld().getPlayers()) {
			if (box.contains(player)) {
				players.add(player);
				((CraftPlayer)player).getHandle().playerConnection.sendPacket(packet24);
				((CraftPlayer)player).getHandle().playerConnection.sendPacket(packet38);
			}
		}
		
		MagicSpells.scheduleDelayedTask(new Runnable() {
			public void run() {
				for (Player player : players) {
					if (player.isValid()) {
						((CraftPlayer)player).getHandle().playerConnection.sendPacket(packet29);
					}
				}
			}
		}, 250);
	}
	
	@Override
	public void setKiller(LivingEntity entity, Player killer) {
		((CraftLivingEntity)entity).getHandle().killer = ((CraftPlayer)killer).getHandle();
	}
	
	@Override
	public DisguiseManager getDisguiseManager(MagicConfig config) {
		if (Bukkit.getPluginManager().isPluginEnabled("ProtocolLib")) {
			return new DisguiseManager_1_9_R1(config);
		} else {
			return new DisguiseManagerEmpty(config);
		}
	}

	@Override
	public ItemStack addAttributes(ItemStack item, String[] names, String[] types, double[] amounts, int[] operations) {
		if (!(item instanceof CraftItemStack)) {
			item = CraftItemStack.asCraftCopy(item);
		}
		NBTTagCompound tag = getTag(item);
		
		NBTTagList list = new NBTTagList();
		for (int i = 0; i < names.length; i++) {
			if (names[i] != null) {
				NBTTagCompound attr = new NBTTagCompound();
				attr.setString("Name", names[i]);
				attr.setString("AttributeName", types[i]);
				attr.setDouble("Amount", amounts[i]);
				attr.setInt("Operation", operations[i]);
				UUID uuid = UUID.randomUUID();
				attr.setLong("UUIDLeast", uuid.getLeastSignificantBits());
				attr.setLong("UUIDMost", uuid.getMostSignificantBits());
				list.add(attr);
			}
		}
		
		tag.set("AttributeModifiers", list);
		
		setTag(item, tag);
		return item;
	}
	
	@Override
	public ItemStack hideTooltipCrap(ItemStack item) {
		ItemMeta meta = item.getItemMeta();
		meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
		meta.addItemFlags(ItemFlag.HIDE_DESTROYS);
		meta.addItemFlags(ItemFlag.HIDE_PLACED_ON);
		meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
		item.setItemMeta(meta);
		return item;
	}

	@Override
	public void addEntityAttribute(LivingEntity entity, String attribute, double amount, int operation) {
		Attribute attr = null;
		if (attribute.equals("generic.maxHealth")) {
			attr = Attribute.GENERIC_MAX_HEALTH;
		} else if (attribute.equals("generic.followRange")) {
			attr = Attribute.GENERIC_MAX_HEALTH;
		} else if (attribute.equals("generic.knockbackResistance")) {
			attr = Attribute.GENERIC_KNOCKBACK_RESISTANCE;
		} else if (attribute.equals("generic.movementSpeed")) {
			attr = Attribute.GENERIC_MOVEMENT_SPEED;
		} else if (attribute.equals("generic.attackDamage")) {
			attr = Attribute.GENERIC_ATTACK_DAMAGE;
		} else if (attribute.equals("generic.attackSpeed")) {
			attr = Attribute.GENERIC_ATTACK_SPEED;
		} else if (attribute.equals("generic.armor")) {
			attr = Attribute.GENERIC_ARMOR;
		} else if (attribute.equals("generic.luck")) {
			attr = Attribute.GENERIC_LUCK;
		}
		Operation oper = null;
		if (operation == 0) {
			oper = Operation.ADD_NUMBER;
		} else if (operation == 1) {
			oper = Operation.MULTIPLY_SCALAR_1;
		} else if (operation == 2) {
			oper = Operation.ADD_SCALAR;
		}
		if (attr != null && oper != null) {
			entity.getAttribute(attr).addModifier(new AttributeModifier("MagicSpells " + attribute, amount, oper));
		}
	}

	@Override
	public void resetEntityAttributes(LivingEntity entity) {
		try {
			EntityLiving e = ((CraftLivingEntity)entity).getHandle();
			Field field = EntityLiving.class.getDeclaredField("bp");
			field.setAccessible(true);
			field.set(e, null);
			e.getAttributeMap();
			Method method = null;
			Class<?> clazz = e.getClass();
			while (clazz != null) {
				try {
					method = clazz.getDeclaredMethod("initAttributes");
					break;
				} catch (NoSuchMethodException e1) {
				    clazz = clazz.getSuperclass();
				}
			}
			if (method != null) {
				method.setAccessible(true);
				method.invoke(e);
			} else {
				throw new Exception("No method initAttributes found on " + e.getClass().getName());
			}
		} catch (Exception e) {
			MagicSpells.handleException(e);
		}		
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void removeAI(LivingEntity entity) {
        try {
        	EntityInsentient ev = (EntityInsentient)((CraftLivingEntity)entity).getHandle();
               
            Field goalsField = EntityInsentient.class.getDeclaredField("goalSelector");
            goalsField.setAccessible(true);
            PathfinderGoalSelector goals = (PathfinderGoalSelector) goalsField.get(ev);
           
            Field listField = PathfinderGoalSelector.class.getDeclaredField("b");
            listField.setAccessible(true);
            List list = (List)listField.get(goals);
            list.clear();
            listField = PathfinderGoalSelector.class.getDeclaredField("c");
            listField.setAccessible(true);
            list = (List)listField.get(goals);
            list.clear();

            goals.a(0, new PathfinderGoalFloat(ev));
        } catch (Exception e) {
            e.printStackTrace();
        }
	}

	@Override
	public void addAILookAtPlayer(LivingEntity entity, int range) {
        try {
        	EntityInsentient ev = (EntityInsentient)((CraftLivingEntity)entity).getHandle();
               
            Field goalsField = EntityInsentient.class.getDeclaredField("goalSelector");
            goalsField.setAccessible(true);
            PathfinderGoalSelector goals = (PathfinderGoalSelector) goalsField.get(ev);

            goals.a(1, new PathfinderGoalLookAtPlayer(ev, EntityHuman.class, range, 1.0F));
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
	
	@Override
	public void setBossBar(Player player, String title, double percent) {
		/*updateBossBarEntity(player, title, percent);
		
		PacketPlayOutEntityDestroy packetDestroy = new PacketPlayOutEntityDestroy(bossBarEntity.getId());
		((CraftPlayer)player).getHandle().playerConnection.sendPacket(packetDestroy);
		
		PacketPlayOutSpawnEntityLiving packetSpawn = new PacketPlayOutSpawnEntityLiving(bossBarEntity);
		((CraftPlayer)player).getHandle().playerConnection.sendPacket(packetSpawn);
		
		PacketPlayOutEntityTeleport packetTeleport = new PacketPlayOutEntityTeleport(bossBarEntity);
		((CraftPlayer)player).getHandle().playerConnection.sendPacket(packetTeleport);*/
		
		//PacketPlayOutEntityVelocity packetVelocity = new PacketPlayOutEntityVelocity(bossBarEntity.getId(), 1, 0, 1);		
		//((CraftPlayer)player).getHandle().playerConnection.sendPacket(packetVelocity);
	}
	
	@Override
	public void updateBossBar(Player player, String title, double percent) {
		/*updateBossBarEntity(player, title, percent);
		
		if (title != null) {
			PacketPlayOutEntityMetadata packetData = new PacketPlayOutEntityMetadata(bossBarEntity.getId(), bossBarEntity.getDataWatcher(), true);
			((CraftPlayer)player).getHandle().playerConnection.sendPacket(packetData);
		}
		
		PacketPlayOutEntityTeleport packetTeleport = new PacketPlayOutEntityTeleport(bossBarEntity);
		((CraftPlayer)player).getHandle().playerConnection.sendPacket(packetTeleport);*/
		
		//PacketPlayOutEntityVelocity packetVelocity = new PacketPlayOutEntityVelocity(bossBarEntity.getId(), 1, 0, 1);
		//((CraftPlayer)player).getHandle().playerConnection.sendPacket(packetVelocity);
	}
	
	/*private void updateBossBarEntity(Player player, String title, double percent) {
		if (title != null) {
			if (percent <= 0.01) percent = 0.01D;
			bossBarEntity.setCustomName(ChatColor.translateAlternateColorCodes('&', title));
			bossBarEntity.getDataWatcher().watch(6, (float)(percent * 300f));
		}
		
		Location l = player.getLocation();
		l.setPitch(l.getPitch() + 10);
		Vector v = l.getDirection().multiply(20);
		Util.rotateVector(v, 15);
		l.add(v);
		bossBarEntity.setLocation(l.getX(), l.getY(), l.getZ(), 0, 0);
	}*/
	
	@Override
	public void removeBossBar(Player player) {
		//PacketPlayOutEntityDestroy packetDestroy = new PacketPlayOutEntityDestroy(bossBarEntity.getId());
		//((CraftPlayer)player).getHandle().playerConnection.sendPacket(packetDestroy);
	}
	
	@Override
	public void saveSkinData(Player player, String name) {
		GameProfile profile = ((CraftPlayer)player).getHandle().getProfile();
		Collection<Property> props = profile.getProperties().get("textures");
		for (Property prop : props) {
			String skin = prop.getValue();
			String sig = prop.getSignature();
			
			File folder = new File(MagicSpells.getInstance().getDataFolder(), "disguiseskins");
			if (!folder.exists()) {
				folder.mkdir();
			}
			File skinFile = new File(folder, name + ".skin.txt");
			File sigFile = new File(folder, name + ".sig.txt");
			try {
				FileWriter writer = new FileWriter(skinFile);
				writer.write(skin);
				writer.flush();
				writer.close();
				writer = new FileWriter(sigFile);
				writer.write(sig);
				writer.flush();
				writer.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			break;
		}
	}

	@Override
	public ItemStack setUnbreakable(ItemStack item) {
		ItemMeta meta = item.getItemMeta();
		meta.spigot().setUnbreakable(true);
		item.setItemMeta(meta);
		return item;
	}
		
	@Override
	public void setArrowsStuck(LivingEntity entity, int count) {
		//((CraftLivingEntity)entity).getHandle().set
	}

	@Override
	public void sendTitleToPlayer(Player player, String title, String subtitle, int fadeIn, int stay, int fadeOut) {
		PlayerConnection conn = ((CraftPlayer)player).getHandle().playerConnection;
		PacketPlayOutTitle packet = new PacketPlayOutTitle(EnumTitleAction.TIMES, null, fadeIn, stay, fadeOut);
		conn.sendPacket(packet);
		if (title != null) {
			packet = new PacketPlayOutTitle(EnumTitleAction.TITLE, new ChatComponentText(title));
			conn.sendPacket(packet);
		}
		if (subtitle != null) {
			packet = new PacketPlayOutTitle(EnumTitleAction.SUBTITLE, new ChatComponentText(subtitle));
			conn.sendPacket(packet);
		}
	}

	@Override
	public void sendActionBarMessage(Player player, String message) {
		PlayerConnection conn = ((CraftPlayer)player).getHandle().playerConnection;
		PacketPlayOutChat packet = new PacketPlayOutChat(new ChatComponentText(message), (byte)2);
		conn.sendPacket(packet);
	}

	@Override
	public void setTabMenuHeaderFooter(Player player, String header, String footer) {
		PlayerConnection conn = ((CraftPlayer)player).getHandle().playerConnection;
		PacketPlayOutPlayerListHeaderFooter packet = new PacketPlayOutPlayerListHeaderFooter();
		try {
			Field field1 = PacketPlayOutPlayerListHeaderFooter.class.getDeclaredField("a");
			Field field2 = PacketPlayOutPlayerListHeaderFooter.class.getDeclaredField("b");
			field1.setAccessible(true);
			field1.set(packet, new ChatComponentText(header));
			field2.setAccessible(true);
			field2.set(packet, new ChatComponentText(footer));
			conn.sendPacket(packet);
		} catch (Exception e) {
			MagicSpells.handleException(e);
		}
	}

	@Override
	public void setNoAIFlag(LivingEntity entity) {
	}

	@Override
	public void setClientVelocity(Player player, Vector velocity) {
		((CraftPlayer)player).getHandle().playerConnection.sendPacket(new PacketPlayOutEntityVelocity(player.getEntityId(), velocity.getX(), velocity.getY(), velocity.getZ()));
	}

	@Override
	public double getAbsorptionHearts(LivingEntity entity) {
		return ((CraftLivingEntity)entity).getHandle().getAbsorptionHearts();
	}

	@Override
	public void setOffhand(Player player, ItemStack item) {
		player.getInventory().setItemInOffHand(item);
	}

}