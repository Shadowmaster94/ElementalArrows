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
package me.cybermaxke.elementarrows.forge.v1800.enchant;

import com.google.common.base.Preconditions;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.item.ItemStack;
import me.cybermaxke.elementarrows.common.enchant.Enchant;
import me.cybermaxke.elementarrows.common.item.type.ItemType;
import me.cybermaxke.elementarrows.forge.v1800.inventory.FItemType;

public class FEnchant implements Enchant {
	public final Enchantment enchantment;
	public final Target target;
	public final String id;

	public FEnchant(String id, Enchantment enchantment) {
		this.enchantment = enchantment;
		this.target = convert(enchantment.type);
		this.id = id;
	}

	@Override
	public String getId() {
		return this.id;
	}

	@Override
	public int getInternalId() {
		return this.enchantment.effectId;
	}

	@Override
	public Target getTarget() {
		return this.target;
	}

	@Override
	public boolean worksWith(Enchant enchant) {
		Preconditions.checkNotNull(enchant);
		return this.enchantment.canApplyTogether(((FEnchant) enchant).enchantment);
	}

	@Override
	public boolean worksFor(ItemType type) {
		Preconditions.checkNotNull(type);
		return this.enchantment.canApply(new ItemStack(((FItemType) type).item));
	}

	@Override
	public int getMaxLevel() {
		return this.enchantment.getMaxLevel();
	}

	@Override
	public int getMinLevel() {
		return this.enchantment.getMinLevel();
	}

	static Target convert(EnumEnchantmentType type) {
		switch (type) {
			case ALL:
				return Target.All;
			case ARMOR:
				return Target.Armor;
			case ARMOR_FEET:
				return Target.Boots;
			case ARMOR_HEAD:
				return Target.Helmet;
			case ARMOR_LEGS:
				return Target.Leggings;
			case ARMOR_TORSO:
				return Target.Chestplate;
			case BOW:
				return Target.Bow;
			case BREAKABLE:
				return Target.Breakable;
			case DIGGER:
				return Target.Digger;
			case FISHING_ROD:
				return Target.FishingRod;
			case WEAPON:
				return Target.Weapon;
			default:
				return Target.All;
		}
	}

}