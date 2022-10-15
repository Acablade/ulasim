package me.acablade.pluginjamulasim.listener;

import lombok.AllArgsConstructor;
import me.acablade.pluginjamulasim.PluginJamUlasim;
import me.acablade.pluginjamulasim.objects.TeleportPortal;
import me.acablade.pluginjamulasim.utils.Colorize;
import me.acablade.pluginjamulasim.utils.PortalHelper;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.Optional;

@AllArgsConstructor
public class PortalListener implements Listener {

	private PluginJamUlasim plugin;

	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	private void onInteract(PlayerInteractEvent event){

		if(event.getHand() != EquipmentSlot.HAND) return;
		if(event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
		Block block = event.getClickedBlock();
		if(block == null) return;
		if(event.getMaterial() != Material.BLAZE_ROD) return;

		TeleportPortal portal = PortalHelper.getPortal(block);
		if(portal!=null){

			if(!event.getPlayer().isSneaking()){
				new AnvilGUI.Builder()
						.itemLeft(new ItemStack(Material.PAPER))
						.title(portal.getName() + "> Işınlan!")
						.text("İsim gir!")
						.plugin(plugin)
						.onComplete((player, s) -> {
							plugin.getPortalManager().findFromName(s).ifPresent(portal1 -> {
								if(portal1.equals(portal)){
									player.sendMessage(Colorize.format("&7[&6BP&7] &cOlduğun portala ışınlanamazsın!"));
									player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO,0.5f,1.0f);
									return;
								}
								if(portal1.teleport(player)){
									player.sendMessage(Colorize.format("&7[&6BP&7] &aBaşarıyla &e"+s+" &aisimli portala ışınlandın"));
									player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP,0.5f,1.0f);
								}else{
									player.sendMessage(Colorize.format("&7[&6BP&7] &cYeterli xp'ye sahip değilsin!"));
									player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO,0.5f,1.0f);
								}
							});
							return AnvilGUI.Response.close();
						})
						.open(event.getPlayer());

			}else{
				new AnvilGUI.Builder()
						.itemLeft(new ItemStack(Material.PAPER))
						.title(portal.getName()+"> İsim Değiştir!")
						.text("İsim gir!")
						.plugin(plugin)
						.onComplete((player, s) -> {
							Optional<TeleportPortal> optional = plugin.getPortalManager().findFromName(s);
							if(optional.isPresent()){
								player.sendMessage(Colorize.format("&7[&6BP&7] &cBu adla bir portal bulunuyor!"));
								player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO,0.5f,1.0f);
								return AnvilGUI.Response.close();
							}
							player.sendMessage(Colorize.format("&7[&6BP&7] &aBaşarıyla portalın ismini &e"+s+" &ayaptın"));
							player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP,0.5f,1.0f);
							portal.setName(s);
							return AnvilGUI.Response.close();
						})
						.open(event.getPlayer());

			}


			return;

		}
		if(!PortalHelper.isShape(block)) return;

		new AnvilGUI.Builder()
				.itemLeft(new ItemStack(Material.PAPER))
				.title("Oluştur!")
				.text("İsim gir!")
				.plugin(plugin)
				.onComplete((player, s) -> {
					PortalHelper.createPortal(block, player, s);
					player.sendMessage(Colorize.format("&7[&6BP&7] &aBaşarıyla portal oluşturdun!"));
					player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP,0.5f,1.0f);
					return AnvilGUI.Response.close();
				})
				.open(event.getPlayer());



	}

	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	private void onBreak(BlockBreakEvent event){
		Block block = event.getBlock();
		Player player = event.getPlayer();
		if(block.getType() != Material.IRON_BLOCK && block.getType() != Material.END_ROD) return;

		TeleportPortal portal = PortalHelper.getPortal(block);

		if(portal==null) return;

		if(block.getType() == Material.IRON_BLOCK){
			event.setCancelled(true);
			return;
		}

		if(!portal.getOwnerUUID().equals(player.getUniqueId())){
			player.sendMessage(Colorize.format("&7[&6BP&7] &cBaşka birinin portalını kıramazsın!"));
			player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO,0.5f,1.0f);
			return;
		}

		block.getRelative(BlockFace.UP).breakNaturally(player.getEquipment().getItemInMainHand());
		block.getRelative(BlockFace.DOWN).breakNaturally(player.getEquipment().getItemInMainHand());

		player.sendMessage(Colorize.format("&7[&6BP&7] &aBaşarıyla &e"+portal.getName()+" &aportalını kırdın!"));
		player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP,0.5f,1.0f);

		plugin.getPortalManager().remove(portal);




	}

}
