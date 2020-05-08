package com.jorislodewijks.hardcorerevival.ritual;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.EntityEffect;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.jorislodewijks.hardcorerevival.HardcoreRevival.ResurrectionType;
import com.jorislodewijks.hardcorerevival.RevivalHandler;
import com.jorislodewijks.hardcorerevival.Players.PlayerHandler;
import com.jorislodewijks.hardcorerevival.altar.Altar;
import com.jorislodewijks.hardcorerevival.books.CultBookHandler;
import com.jorislodewijks.hardcorerevival.books.ReligiousBookHandler;
import com.jorislodewijks.hardcorerevival.ritual.Ritual.RitualType;

public class RitualHandler implements Listener {

	private static List<Ritual> CultRituals = new ArrayList<Ritual>(
			Arrays.asList(
					new Ritual("Ritual Instruction Spawning", ResurrectionType.CULT, RitualType.INSTRUCTION_BOOK,
							new ArrayList<ItemStack>(Arrays.asList(new ItemStack(Material.BOOK, 1),
									new ItemStack(Material.FEATHER, 1), new ItemStack(Material.INK_SAC, 1))),
							null, 10, 0, -10, -1),
					new Ritual("Bodily Sacrificial Revival", ResurrectionType.CULT, RitualType.REVIVAL,
							new ArrayList<ItemStack>(Arrays.asList(new ItemStack(Material.TOTEM_OF_UNDYING, 1),
									new ItemStack(Material.BONE, 8), new ItemStack(Material.REDSTONE, 16),
									new ItemStack(Material.GUNPOWDER, 2))),
							null, 60, -100, -50, -1)));

	private static List<Ritual> ReligiousRituals = new ArrayList<Ritual>(Arrays.asList(
			new Ritual("Heavenly Revival", ResurrectionType.RELIGIOUS, RitualType.REVIVAL,
					new ArrayList<ItemStack>(Arrays.asList(new ItemStack(Material.TOTEM_OF_UNDYING, 1),
							new ItemStack(Material.GOLDEN_APPLE, 1), new ItemStack(Material.EMERALD, 16),
							new ItemStack(Material.POPPY, 1))),
					null, 60, 100, 50, -1),
			new Ritual("The Lords instructions", ResurrectionType.RELIGIOUS, RitualType.INSTRUCTION_BOOK,
					new ArrayList<ItemStack>(Arrays.asList(new ItemStack(Material.BOOK, 1),
							new ItemStack(Material.FEATHER, 1), new ItemStack(Material.INK_SAC, 1))),
					null, 10, 0, 10, -1),
			new Ritual("Pray", ResurrectionType.RELIGIOUS, RitualType.PRAY,
					new ArrayList<ItemStack>(Arrays.asList(new ItemStack(Material.GOLD_INGOT, 1))), null, 10, 0, 10,
					-1)));

	@EventHandler
	public void OnRitualCompleteEvent(OnRitualCompletionEvent event) {
		removeIngredients(event.getAltar(), event.getRitual().getIngredients());
		spawnResults(event.getAltar(), event.getRitual().getResults());

		// Specials on ritual completion?
		switch (event.getRitual().getResurrectionType()) {
		case CULT:
			event.getAltar().getImportantBlock().getWorld().playSound(
					event.getAltar().getImportantBlock().getLocation(), Sound.ENTITY_PARROT_IMITATE_GHAST, 1.0f, 0.1f);

			switch (event.getRitual().getRitualType()) {
			case REVIVAL:
				new RevivalHandler().reviveNearestDeadPlayer(
						event.getAltar().getImportantBlock().getLocation().add(0.5, 0.5, 0.5),
						event.getAltar().getImportantBlock().getLocation().add(0.5, 0.5, 0.5),
						event.getRitual().getResurrectionType());
				break;
			case INSTRUCTION_BOOK:
				event.getAltar().getImportantBlock().getWorld().dropItemNaturally(
						event.getAltar().getImportantBlock().getLocation().add(0.5, 0.5, 0.5),
						new CultBookHandler().getInstructionBook());
				break;
			case PRAY:
				new PlayerHandler().giveEffectToNearestPlayer(event.getAltar().getImportantBlock().getLocation(),
						new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 20 * 60 * 5, 1));
				break;
			}
			break;
		case RELIGIOUS:
			event.getAltar().getImportantBlock().getWorld().spawnParticle(Particle.FIREWORKS_SPARK,
					event.getAltar().getImportantBlock().getX() + 0.5,
					event.getAltar().getImportantBlock().getY() + 0.5,
					event.getAltar().getImportantBlock().getZ() + 0.5, 255, 0.25, 0.25, 0.25, 1);

			event.getAltar().getImportantBlock().getWorld().playSound(
					event.getAltar().getImportantBlock().getLocation().add(0.5, 0.5, 0.5), Sound.BLOCK_BEACON_ACTIVATE,
					2.0f, 2.0f);

			switch (event.getRitual().getRitualType()) {
			case REVIVAL:
				new RevivalHandler().reviveNearestDeadPlayer(
						event.getAltar().getImportantBlock().getLocation().add(0.5, 1.5, 0.5),
						event.getAltar().getImportantBlock().getLocation().add(0.5, 1.5, 0.5),
						ResurrectionType.RELIGIOUS);
				break;
			case INSTRUCTION_BOOK:
				event.getAltar().getImportantBlock().getWorld().dropItemNaturally(
						event.getAltar().getImportantBlock().getLocation().add(0.5, 0.5, 0.5),
						new ReligiousBookHandler().getInstructionBook());
				break;
			case PRAY:
				new PlayerHandler().giveEffectToNearestPlayer(event.getAltar().getImportantBlock().getLocation(),
						new PotionEffect(PotionEffectType.REGENERATION, 20 * 60 * 5, 1));
				break;
			}
			break;
		}
	}

	@EventHandler
	public void OnRitualCanceledEvent(OnRitualCanceledEvent event) {
		switch (event.getRitual().getResurrectionType()) {
		case CULT:
			event.getAltar().getImportantBlock().getWorld().playSound(
					event.getAltar().getImportantBlock().getLocation(), Sound.ENTITY_GHAST_DEATH, 1.0f, 0.05f);

			Entity zombie = event.getAltar().getImportantBlock().getWorld().spawnEntity(
					event.getAltar().getImportantBlock().getLocation().add(0.5, 0.5, 0.5), EntityType.ZOMBIE);
			zombie.setCustomName("Forsaken Experiment");
			zombie.playEffect(EntityEffect.ENTITY_POOF);
			break;
		case RELIGIOUS:
			event.getAltar().getImportantBlock().getWorld().playSound(
					event.getAltar().getImportantBlock().getLocation(), Sound.BLOCK_BEACON_POWER_SELECT, 1.0f, 1.0f);
			break;
		}
	}

	private void removeIngredients(Altar altar, List<ItemStack> items) {
		for (Entity e : altar.getAlterItemEntities()) {
			if (e instanceof Item) {
				// if(((Item)e).getItemStack())

				// Remove all for now. We may do additive removal..
				e.remove();
			}
		}
	}

	private void spawnResults(Altar altar, List<ItemStack> items) {
		if (items == null)
			return;

		for (ItemStack itemStack : items) {
			altar.getImportantBlock().getWorld()
					.dropItemNaturally(altar.getImportantBlock().getLocation().add(0.5, 0.5, 0.5), itemStack);
		}
	}

	public static List<Ritual> getRituals(ResurrectionType resurrectionType) {
		switch (resurrectionType) {
		case CULT:
			return CultRituals;
		case RELIGIOUS:
			return ReligiousRituals;
		}

		return null;
	}

}