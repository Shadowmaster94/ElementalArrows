/**
 * 
 * This software is part of the ElementalArrows
 * 
 * ElementalArrows is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or 
 * any later version.
 * 
 * ElementalArrows is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with ElementalArrows. If not, see <http://www.gnu.org/licenses/>.
 * 
 */
package me.cybermaxke.elementalarrows.plugin.entity.nms;

import java.lang.reflect.Field;

import net.minecraft.server.v1_5_R3.EntityArrow;
import net.minecraft.server.v1_5_R3.EntityHuman;
import net.minecraft.server.v1_5_R3.EntityItem;
import net.minecraft.server.v1_5_R3.EntityLiving;
import net.minecraft.server.v1_5_R3.ItemStack;
import net.minecraft.server.v1_5_R3.NBTTagCompound;
import net.minecraft.server.v1_5_R3.World;

import org.bukkit.craftbukkit.v1_5_R3.entity.CraftItem;
import org.bukkit.craftbukkit.v1_5_R3.inventory.CraftItemStack;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerPickupItemEvent;

import org.getspout.spoutapi.material.CustomItem;
import org.getspout.spoutapi.material.MaterialData;

import me.cybermaxke.elementalarrows.api.material.ArrowMaterial;
import me.cybermaxke.elementalarrows.plugin.entity.CraftElementalArrow;
import me.cybermaxke.elementalarrows.plugin.entity.CraftElementalTurretArrow;

public class EntityElementalArrow extends EntityArrow {
	public EntityElementalTurret turret;
	public ArrowMaterial arrow;
	public float speed = 0.0F;

	public EntityElementalArrow(World world) {
		super(world);
	}

	public EntityElementalArrow(World world, double x, double y, double z) {
		super(world, x, y, z);
	}

	public EntityElementalArrow(World world, EntityLiving entityliving, float f) {
		super(world, entityliving, f);
		this.speed = f;
	}

	public EntityElementalArrow(World world, EntityLiving entityliving, EntityLiving entityliving1, float f, float f1) {
		super(world, entityliving, entityliving1, f, f1);
		this.speed = f;
	}

	@Override
	public CraftElementalArrow getBukkitEntity() {
		if (this.turret != null && (this.bukkitEntity == null || !(this.bukkitEntity instanceof CraftElementalTurretArrow))) {
			this.bukkitEntity = new CraftElementalTurretArrow(this);
		} else if (this.bukkitEntity == null || !(this.bukkitEntity instanceof CraftElementalArrow)) {
			this.bukkitEntity = new CraftElementalArrow(this);
		}
			
		return (CraftElementalArrow) this.bukkitEntity;
	}

	@Override
	public void b(NBTTagCompound tag) {
		super.b(tag);
		if (this.arrow != null && this.arrow instanceof CustomItem) {
			tag.setInt("ElementalArrowId", ((CustomItem) this.arrow).getCustomId());
		}
	}

	@Override
	public void a(NBTTagCompound tag) {
		super.a(tag);
		if (tag.hasKey("ElementalArrowId")) {
			CustomItem item = MaterialData.getCustomItem(tag.getInt("ElementalArrowId"));
			this.arrow = (ArrowMaterial) (item == null ? null : item instanceof ArrowMaterial ? item : null);
		}
	}

	@Override
	public void l_() {
		if (this.arrow != null) {
			LivingEntity shooter = (LivingEntity) (this.shooter == null ? null : this.shooter instanceof EntityLiving ? this.shooter.getBukkitEntity() : null);
			this.arrow.onTick(shooter, this.getBukkitEntity());
		}
		super.l_();
	}

	public boolean isInGround() {
		try {
			Field f = EntityArrow.class.getDeclaredField("inGround");
			f.setAccessible(true);
			return f.getBoolean(this);
		} catch (Exception e) {
			return false;
		}
	}

	public int getKnockbackStrength() {
		try {
			Field f = EntityArrow.class.getDeclaredField("aw");
			f.setAccessible(true);
			return f.getInt(this);
		} catch (Exception e) {
			return 0;
		}
	}

	@Override
	public void b_(EntityHuman entityhuman) {
		if (this.arrow == null) {
			super.b_(entityhuman);
			return;
		}

		if (!this.world.isStatic && this.isInGround() && this.shake <= 0) {
			ItemStack is = this.arrow.getDrop() == null ? null : CraftItemStack.asNMSCopy(this.arrow.getDrop());

			if (is != null && this.fromPlayer == 1 && entityhuman.inventory.canHold(is) > 0) {
				EntityItem i = new EntityItem(this.world, this.locX, this.locY, this.locZ, is);

				PlayerPickupItemEvent e = new PlayerPickupItemEvent((Player) entityhuman.getBukkitEntity(), new CraftItem(this.world.getServer(), this, i), 0);
				this.world.getServer().getPluginManager().callEvent(e);

				if (e.isCancelled()) {
					return;
				}
			}

			boolean flag = this.fromPlayer == 1 || (this.fromPlayer == 2 && entityhuman.abilities.canInstantlyBuild);
			if (is == null || (this.fromPlayer == 1 && !entityhuman.inventory.pickup(is))) {
				flag = false;
			}

			if (flag) {
				this.world.makeSound(this, "random.pop", 0.2F, ((this.random.nextFloat() - this.random.nextFloat()) * 0.7F + 1.0F) * 2.0F);
				entityhuman.receive(this, 1);
				this.die();
			}
		}
	}
}