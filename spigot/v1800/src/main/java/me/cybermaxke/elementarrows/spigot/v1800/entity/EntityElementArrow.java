/**
 * This file is part of ElementalArrows.
 * 
 * Copyright (c) 2014, Cybermaxke
 * 
 * ElementalArrows is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * ElementalArrows is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with ElementalArrows. If not, see <http://www.gnu.org/licenses/>.
 */
package me.cybermaxke.elementarrows.spigot.v1800.entity;

import org.bukkit.craftbukkit.v1_8_R1.entity.CraftItem;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerPickupItemEvent;

import net.minecraft.server.v1_8_R1.EntityArrow;
import net.minecraft.server.v1_8_R1.EntityHuman;
import net.minecraft.server.v1_8_R1.EntityItem;
import net.minecraft.server.v1_8_R1.EntityLiving;
import net.minecraft.server.v1_8_R1.ItemStack;
import net.minecraft.server.v1_8_R1.Items;
import net.minecraft.server.v1_8_R1.NBTTagCompound;
import net.minecraft.server.v1_8_R1.World;

import me.cybermaxke.elementarrows.common.arrow.Arrows;
import me.cybermaxke.elementarrows.common.arrow.ElementArrow;
import me.cybermaxke.elementarrows.common.arrow.event.EventEntityHitGround;
import me.cybermaxke.elementarrows.common.arrow.event.EventEntityTick;
import me.cybermaxke.elementarrows.common.source.Source;
import me.cybermaxke.elementarrows.common.source.SourceUnknown;

public class EntityElementArrow extends EntityArrow {
	/**
	 * Field to track when the state changes.
	 */
	private boolean lastInGround;

	/**
	 * The source.
	 */
	public Source source = new SourceUnknown();

	public EntityElementArrow(World world) {
		super(world);
	}

	public EntityElementArrow(World world, double x, double y, double z) {
		super(world, x, y, z);
	}

	public EntityElementArrow(World world, EntityLiving shooter, float power) {
		super(world, shooter, power);
	}

	@Override
	protected void h() {
		super.h();

		/**
		 * Add the elemental arrow data.
		 */
		this.datawatcher.a(20, new Short((short) 0));
	}

	@Override
	public void b(NBTTagCompound nbt) {
		super.b(nbt);

		/**
		 * Save the elemental arrow type.
		 */
		nbt.setShort("elementarrow", this.getElementData());
	}

	@Override
	public void a(NBTTagCompound nbt) {
		/**
		 * Try to load the elemental arrow type.
		 */
		if (nbt.hasKey("elementarrow")) {
			this.setElementData(nbt.getShort("elementarrow"));
		}

		/**
		 * Fix the in ground field and I forgot to save/load default data.
		 */
		if (nbt.hasKey("inGround")) {
			super.a(nbt);

			this.inGround = nbt.getBoolean("inGround");
			this.lastInGround = this.inGround;
		}
	}

	@Override
	public void d(EntityHuman player) {
		if (this.world.isStatic || !this.inGround || this.shake > 0) {
			return;
		}

		ItemStack itemstack = new ItemStack(Items.ARROW, 1, this.getElementData());
		if (this.fromPlayer == 1 && player.inventory.canHold(itemstack) > 0) {
			EntityItem item = new EntityItem(this.world, this.locX, this.locY, this.locZ, itemstack);
			PlayerPickupItemEvent event = new PlayerPickupItemEvent((Player) player.getBukkitEntity(), new CraftItem(this.world.getServer(), this, item), 0);
	        
			this.world.getServer().getPluginManager().callEvent(event);

			if (event.isCancelled()) {
				return;
			}
		}

		int i = this.fromPlayer == 1 || (this.fromPlayer == 2 && (player.abilities.canInstantlyBuild)) ? 1 : 0;
		if (this.fromPlayer == 1 && !player.inventory.pickup(new ItemStack(Items.ARROW, 1, this.getElementData()))) {
			i = 0;
		}

		if (i != 0) {
			this.makeSound("random.pop", 0.2f, ((this.random.nextFloat() - this.random.nextFloat()) * 0.7f + 1f) * 2f);
			player.receive(this, 1);
			this.die();
		}
	}

	@Override
	public void s_() {
		super.s_();

		ElementArrow arrow = Arrows.find(this.getElementData());
		FEntityArrow wrapper = FEntity.of(this);

		/**
		 * Check hit ground stuff.
		 */
		if (this.inGround && this.inGround != this.lastInGround && arrow != null) {
			EventEntityHitGround event = new EventEntityHitGround(wrapper, this.source);
			arrow.handle(event);
		}

		if (arrow != null) {
			arrow.handle(new EventEntityTick(wrapper));
		}

		this.lastInGround = this.inGround;
	}

	/**
	 * Gets the data value of the elemental arrow.
	 * 
	 * @return the data value
	 */
	public short getElementData() {
		return this.datawatcher.getShort(20);
	}

	/**
	 * Sets the data value of the elemental arrow.
	 * 
	 * @param data the data value
	 */
	public void setElementData(int data) {
		this.datawatcher.watch(20, new Short((short) (data & 0xffff)));
	}

}