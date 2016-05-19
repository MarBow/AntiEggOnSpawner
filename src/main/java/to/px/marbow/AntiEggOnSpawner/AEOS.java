package to.px.marbow.AntiEggOnSpawner;

import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.plugin.java.JavaPlugin;

public class AEOS extends JavaPlugin implements Listener {
	@Override
	public void onEnable() {
		FileConfiguration fc = getConfig();
		flgAEOSEnabled = fc.getBoolean("AEOSEnable");
		flgDisqualify = fc.getBoolean("Disqualify");
		flgNoBedOnNether = fc.getBoolean("NoBedOnNether");

		getLogger().info("〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");

		getLogger().info(
				"AntiEggOnSpawnerは"
						+ (AEOS.flgAEOSEnabled ? ChatColor.GREEN + "有効"
								: ChatColor.RED + "無効") + ChatColor.RESET
						+ "です。");

		getLogger().info(
				"Egg取り上げは"
						+ (AEOS.flgDisqualify ? ChatColor.GREEN + "有効"
								: ChatColor.RED + "無効") + ChatColor.RESET
						+ "です。");

		getLogger().info(
				"ネザーでのベット使用は"
						+ (AEOS.flgNoBedOnNether ? ChatColor.GREEN + "禁止"
								: ChatColor.RED + "許可") + ChatColor.RESET
						+ "です。");

		getLogger().info("〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");

		//getCommand("aeos").setExecutor(new SwitchCommand());
		getServer().getPluginManager().registerEvents(this, this);
	}

	// ==========================================================

	@Override
	public void onDisable() {

		FileConfiguration fc = getConfig();

		fc.set("AEOSEnable", flgAEOSEnabled);
		fc.set("Disqualify", flgDisqualify);
		fc.set("NoBedOnNether", flgNoBedOnNether);

		saveConfig();

		getLogger().info("〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");

		getLogger().info(
				"AntiEggOnSpawnerは"
						+ (AEOS.flgAEOSEnabled ? ChatColor.GREEN + "有効"
								: ChatColor.RED + "無効") + ChatColor.RESET
						+ "でした。");

		getLogger().info(
				"Egg取り上げは"
						+ (AEOS.flgDisqualify ? ChatColor.GREEN + "有効"
								: ChatColor.RED + "無効") + ChatColor.RESET
						+ "でした。");

		getLogger().info(
				"ネザーでのベット使用は"
						+ (AEOS.flgNoBedOnNether ? ChatColor.GREEN + "禁止"
								: ChatColor.RED + "許可") + ChatColor.RESET
						+ "でした。");

		getLogger().info("〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
	}

	// ==========================================================

	static public boolean flgAEOSEnabled = true;
	static public boolean flgDisqualify = true;
	static public boolean flgNoBedOnNether = true;

	// ==========================================================

	public boolean onCommand(CommandSender sender, Command cmd,
			String commandLabel, String[] args) {
		boolean flgRef = true;

		// if ((sender instanceof Player)) {
		// Player playerCmd = (Player) sender;

		if (cmd.getName().equalsIgnoreCase("aeos")) {

			if (args.length > 0) {
				Player other = (Player) sender;
				//Player other = getPlayer(args[0]);

				if (other != null) {

					//sender.sendMessage("r u "+other.getName()+" ?");

					switch (args[0].toLowerCase()) {

					case "on":
						AEOS.flgAEOSEnabled = true;
						sender.sendMessage("AntiEggOnSpawnerは"
								+ ChatColor.GREEN + "有効" + ChatColor.RESET
								+ "になりました。");
						break;

					case "off":
						AEOS.flgAEOSEnabled = false;
						sender.sendMessage("AntiEggOnSpawnerは" + ChatColor.RED
								+ "無効" + ChatColor.RESET + "になりました。");
						break;

					case "disqualify":// スポナーにエッグを使った時の取り上げ
						if (args.length >= 2) {
							switch (args[1].toLowerCase()) {
							case "on":
								AEOS.flgDisqualify = true;
								sender.sendMessage("Egg取り上げは" + ChatColor.GREEN
										+ "有効" + ChatColor.RESET + "になりました。");
								break;
							case "off":
								AEOS.flgDisqualify = false;
								sender.sendMessage("Egg取り上げは" + ChatColor.RED
										+ "無効" + ChatColor.RESET + "になりました。");
								break;
							default:
								sender.sendMessage("/aeos disqualify [on|off] 使おうとしたエッグ取り上げの有効・無効を切り替えます。");
								break;
							}
						} else {
							sender.sendMessage("/aeos disqualify [on|off] 使おうとしたエッグ取り上げの有効・無効を切り替えます。");
						}
						break;

					case "nbon":// ネザーでのベット使用禁止
						if (args.length >= 2) {
							switch (args[1].toLowerCase()) {
							case "on":
								AEOS.flgNoBedOnNether = true;
								sender.sendMessage("ネザーでのベッド使用は"
										+ ChatColor.GREEN + "禁止"
										+ ChatColor.RESET + "になりました。");
								break;
							case "off":
								AEOS.flgNoBedOnNether = false;
								sender.sendMessage("ネザーでのベッド使用は"
										+ ChatColor.RED + "許可"
										+ ChatColor.RESET + "になりました。");
								break;
							default:
								sender.sendMessage("/aeos nbon [on|off] ネザーでのベッド使用の禁止・許可を切り替えます。");
								break;
							}
						} else {
							sender.sendMessage("/aeos nbon [on|off] ネザーでのベッド使用の禁止・許可を切り替えます。");
						}
						break;

					case "pmsn":
						if ((sender instanceof Player)) {
							Player playerCmd = (Player) sender;
							Set<PermissionAttachmentInfo> setPai = playerCmd
									.getEffectivePermissions();
							PermissionAttachmentInfo[] pais = setPai
									.toArray(new PermissionAttachmentInfo[setPai
											.size()]);
							sender.sendMessage("パーミッション数は");
							sender.sendMessage(String.valueOf(pais.length));
							for (int i = 0; i < pais.length; i++) {
								sender.sendMessage(pais[i].getPermission());
							}
						}
						else{
							flgRef = false;
						}

						break;
					}

				}
				else
				{
					flgRef = false;
				}
			} else {

				sender.sendMessage("〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
				sender.sendMessage("AntiEggOnSpawnerは"
						+ (AEOS.flgAEOSEnabled ? ChatColor.GREEN + "有効"
								: ChatColor.RED + "無効") + ChatColor.RESET
						+ "です。");

				sender.sendMessage("Egg取り上げは"
						+ (AEOS.flgDisqualify ? ChatColor.GREEN + "有効"
								: ChatColor.RED + "無効") + ChatColor.RESET
						+ "です。");

				sender.sendMessage("ネザーでのベット使用は"
						+ (AEOS.flgNoBedOnNether ? ChatColor.GREEN + "禁止"
								: ChatColor.RED + "許可") + ChatColor.RESET
						+ "です。");
				sender.sendMessage("〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");

				sender.sendMessage("/aeos 使用法および現在の状態を表示します。");
				sender.sendMessage("/aeos [on|off] プラグインの有効・無効を切り替えます。");
				sender.sendMessage("/aeos disqualify [on|off] 使おうとしたエッグ取り上げの有効・無効を切り替えます。");
				sender.sendMessage("/aeos nbon [on|off] ネザーでのベッド使用の禁止・許可を切り替えます。");
				// sender.sendMessage("/aeos [Player] プレイヤーに一時的な使用許可を与えます");
			}
		}
		else
		{
			flgRef = false;
		}

		return flgRef;
	}

	// ==========================================================

	public Player getPlayer(String name) {
		for (Player player : Bukkit.getOnlinePlayers()) {
			if (player.getName().equals(name)) {
				return player;
			}
		}
		return null;
	}

	// ==========================================================

	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerInteractBlock(PlayerInteractEvent event) {

		Player player = event.getPlayer();
		Block target = event.getClickedBlock();

		if (AEOS.flgAEOSEnabled)// プラグインが有効の場合
		{
			if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
				if (player.getItemInHand().getType() == Material.MONSTER_EGG
						|| player.getItemInHand().getType() == Material.MONSTER_EGGS) {
					// モンスターエッグを使おうとしたか検出

					if (target != null) {

						if (target.getType() == Material.MOB_SPAWNER) {
							// スポナーに使おうとしたか検出
							player.sendMessage("モンスターエッグをスポナーに使うことは許可されていません。");
							event.setCancelled(true);
							getLogger().info(
									player.getName() + "が"
											+ event.getItem().getType() + "を"
											+ event.getClickedBlock().getType()
											+ "に使おうとしました。");

							if (flgDisqualify) {
								// 使おうとしたエッグを取り上げる
								PlayerInventory inventory = player
										.getInventory();
								inventory.removeItem(player.getItemInHand());
							}
						}
					}
				}
			}
		}

		if (AEOS.flgNoBedOnNether) {
			if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
				Biome bm = player.getWorld().getBiome(0, 0);
				if (bm == Biome.HELL) {
					if (target.getType() == Material.BED_BLOCK) {
						player.sendMessage(ChatColor.RED
								+ "ネザーワールドでベッド使うと爆発するよ！！" + ChatColor.RESET);
						event.setCancelled(true);
						getServer().getLogger().info(
								player.getName() + "がネザーワールドでベッドを使用しようとしました。");
					}
				}
			}
		}
	}
}
