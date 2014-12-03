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
package me.cybermaxke.elementarrows.spigot.v1710;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import me.cybermaxke.elementarrows.common.PluginBase;
import me.cybermaxke.elementarrows.common.entity.Entities;
import me.cybermaxke.elementarrows.common.entity.EntityFactory;
import me.cybermaxke.elementarrows.common.inventory.ItemFactory;
import me.cybermaxke.elementarrows.common.inventory.ItemStacks;
import me.cybermaxke.elementarrows.common.locale.LocaleRegistry;
import me.cybermaxke.elementarrows.common.locale.Locales;
import me.cybermaxke.elementarrows.common.potion.PotionFactory;
import me.cybermaxke.elementarrows.common.potion.Potions;
import me.cybermaxke.elementarrows.common.recipe.RecipeFactory;
import me.cybermaxke.elementarrows.common.recipe.Recipes;
import me.cybermaxke.elementarrows.common.world.WorldManager;
import me.cybermaxke.elementarrows.common.world.Worlds;
import me.cybermaxke.elementarrows.spigot.v1710.dispenser.DispenseElementArrow;
import me.cybermaxke.elementarrows.spigot.v1710.entity.EntityElementArrow;
import me.cybermaxke.elementarrows.spigot.v1710.entity.EntityElementArrowListener;
import me.cybermaxke.elementarrows.spigot.v1710.entity.EntityRegistry;
import me.cybermaxke.elementarrows.spigot.v1710.entity.FEntityFactory;
import me.cybermaxke.elementarrows.spigot.v1710.entity.FEntityTickHandler;
import me.cybermaxke.elementarrows.spigot.v1710.inventory.FItemFactory;
import me.cybermaxke.elementarrows.spigot.v1710.item.ItemArrow;
import me.cybermaxke.elementarrows.spigot.v1710.item.ItemBow;
import me.cybermaxke.elementarrows.spigot.v1710.item.ItemRegistry;
import me.cybermaxke.elementarrows.spigot.v1710.locale.FLocaleRegistry;
import me.cybermaxke.elementarrows.spigot.v1710.network.MessageInjector;
import me.cybermaxke.elementarrows.spigot.v1710.potion.FPotionFactory;
import me.cybermaxke.elementarrows.spigot.v1710.recipe.FRecipeFactory;
import me.cybermaxke.elementarrows.spigot.v1710.util.Fields;
import me.cybermaxke.elementarrows.spigot.v1710.world.FWorldManager;

import net.minecraft.server.v1_7_R4.BlockDispenser;
import net.minecraft.server.v1_7_R4.Item;
import net.minecraft.server.v1_7_R4.Items;

import org.bukkit.plugin.java.JavaPlugin;

public class FElementArrows extends JavaPlugin {
	public static FLocaleRegistry locales;
	public static FRecipeFactory recipes;
	public static FEntityFactory entities;
	public static FPotionFactory potions;
	public static FWorldManager worlds;
	public static FItemFactory items;

	private PluginBase plugin = new PluginBase();

	@Override
	public void onLoad() {
		this.plugin.onPreInit();
	}

	@Override
	public void onEnable() {
		entities = new FEntityFactory();
		entities.onInit(this);

		locales = new FLocaleRegistry();
		recipes = new FRecipeFactory();
		worlds = new FWorldManager();
		items = new FItemFactory();

		try {
			potions = new FPotionFactory();
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			setFactoryInstance(Locales.class, LocaleRegistry.class, locales);
			setFactoryInstance(Recipes.class, RecipeFactory.class, recipes);
			setFactoryInstance(Entities.class, EntityFactory.class, entities);
			setFactoryInstance(Potions.class, PotionFactory.class, potions);
			setFactoryInstance(Worlds.class, WorldManager.class, worlds);
			setFactoryInstance(ItemStacks.class, ItemFactory.class, items);
		} catch (Exception e) {
			e.printStackTrace();
		}

		/**
		 * Load the locale file.
		 */
		try {
			Locales.read(FElementArrows.class.getResourceAsStream("/assets/elementarrows/lang/en_US.lang"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		/**
		 * Create the new items.
		 */
		Item itemArrow = new ItemArrow();
		Item itemBow = new ItemBow();

		ItemRegistry itemRegistry = new ItemRegistry();
		itemRegistry.register("minecraft:arrow", itemArrow);
		itemRegistry.register("minecraft:bow", itemBow);

		/**
		 * Override the default arrow.
		 */
		EntityRegistry entityRegistry = new EntityRegistry();
		entityRegistry.register(EntityElementArrow.class, "Arrow", 10);

		/**
		 * Initialize the server message injector.
		 */
		MessageInjector injector = new MessageInjector();
		injector.onInit(this);

		EntityElementArrowListener listener = new EntityElementArrowListener();
		listener.onInit(this);

		FEntityTickHandler handler = new FEntityTickHandler();
		handler.onInit(this);

		/**
		 * Register the new dispenser behavior.
		 */
		BlockDispenser.a.a(Items.ARROW, new DispenseElementArrow());

		this.plugin.onInit();
		this.plugin.onPostInit();
	}

	protected static void setFactoryInstance(Class<?> target, Class<?> type, Object instance) throws Exception {
		Field field = Fields.findField(target, type, 0, false);
		field.setAccessible(true);

		int modifiers = field.getModifiers();

		if (Modifier.isFinal(modifiers)) {
			Field field0 = Field.class.getDeclaredField("modifiers");
			field0.setAccessible(true);
			field0.set(field, modifiers & ~Modifier.FINAL);
		}

		field.set(null, instance);
	}

}