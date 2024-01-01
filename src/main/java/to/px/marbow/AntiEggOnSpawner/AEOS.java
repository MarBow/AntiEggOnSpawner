package to.px.marbow.AntiEggOnSpawner;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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

		Config_Load();

		getLogger().info("〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");

		getLogger().info(
				"AntiEggOnSpawnerは"
						+ (AEOS.flgAEOSEnabled ? ChatColor.GREEN + "有効"
								: ChatColor.RED + "無効")
						+ ChatColor.RESET
						+ "です。");

		getLogger().info(
				"Egg取り上げは"
						+ (AEOS.flgDisqualify ? ChatColor.GREEN + "有効"
								: ChatColor.RED + "無効")
						+ ChatColor.RESET
						+ "です。");

		getLogger().info(
				"ネザーでのベット使用は"
						+ (AEOS.flgNoBedOnNether ? ChatColor.GREEN + "禁止"
								: ChatColor.RED + "許可")
						+ ChatColor.RESET
						+ "です。");

		getLogger().info(
				"エンドでのベット使用は"
						+ (AEOS.flgNoBedOnNether ? ChatColor.GREEN + "禁止"
								: ChatColor.RED + "許可")
						+ ChatColor.RESET
						+ "です。");

		getLogger().info("〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");

		// getCommand("aeos").setExecutor(new SwitchCommand());
		getServer().getPluginManager().registerEvents(this, this);

		//OutputMaterials();//マテリアルの一覧表などを作るための作業ルーチン
	}

	private void OutputMaterials() {

		try {
			File fileMaterial = new File("material.txt");
			if (fileMaterial.length() > 0) {
				fileMaterial.delete();
			}
			FileWriter fwMaterial = new FileWriter(fileMaterial);

			File fileDoor = new File("door.txt");
			if (fileDoor.length() > 0) {
				fileDoor.delete();
			}
			FileWriter fwDoor = new FileWriter(fileDoor);

			fwMaterial.write(String.valueOf(Material.values().length) + "\r\n");

			for (Material mat : Material.values()) {
				String strMaterialName = mat.name();
				fwMaterial.write(strMaterialName + "\r\n");

				if (strMaterialName.toLowerCase().indexOf("_door") >= 0) {
					fwDoor.write(strMaterialName + "\r\n");
				}
			}

			fwDoor.flush();
			fwDoor.close();

			fwMaterial.flush();
			fwMaterial.close();

		} catch (IOException e) {
			System.out.println(e);
		}
	}

	// ==========================================================

	@Override
	public void onDisable() {

		Config_Save();

		getLogger().info("〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");

		getLogger().info(
				"AntiEggOnSpawnerは"
						+ (AEOS.flgAEOSEnabled ? ChatColor.GREEN + "有効"
								: ChatColor.RED + "無効")
						+ ChatColor.RESET
						+ "でした。");

		getLogger().info(
				"Egg取り上げは"
						+ (AEOS.flgDisqualify ? ChatColor.GREEN + "有効"
								: ChatColor.RED + "無効")
						+ ChatColor.RESET
						+ "でした。");

		getLogger().info(
				"ネザーでのベット使用は"
						+ (AEOS.flgNoBedOnNether ? ChatColor.GREEN + "禁止"
								: ChatColor.RED + "許可")
						+ ChatColor.RESET
						+ "でした。");

		getLogger().info(
				"エンドでのベット使用は"
						+ (AEOS.flgNoBedOnEnd ? ChatColor.GREEN + "禁止"
								: ChatColor.RED + "許可")
						+ ChatColor.RESET
						+ "でした。");

		getLogger().info("〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
	}

	// ==========================================================

	static public boolean flgAEOSEnabled = true;
	static public boolean flgDisqualify = true;
	static public boolean flgNoBedOnNether = true;
	static public boolean flgNoBedOnEnd = true;

	// ==========================================================

	void Config_Load() {
		FileConfiguration fc = getConfig();
		flgAEOSEnabled = fc.getBoolean("AEOSEnable");
		flgDisqualify = fc.getBoolean("Disqualify");
		flgNoBedOnNether = fc.getBoolean("NoBedOnNether");
		flgNoBedOnEnd = fc.getBoolean("NoBedOnEnd");
	}

	void Config_Save() {
		FileConfiguration fc = getConfig();

		fc.set("AEOSEnable", flgAEOSEnabled);
		fc.set("Disqualify", flgDisqualify);
		fc.set("NoBedOnNether", flgNoBedOnNether);
		fc.set("NoBedOnEnd", flgNoBedOnEnd);

		saveConfig();
	}

	// 〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓

	void Mes(CommandSender sender, String strMes) {
		if ((sender instanceof Player)) {
			Player player = (Player) sender;
			player.sendMessage(strMes);
		} else {
			getLogger().info(strMes);
		}
	}

	// 〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓

	public boolean onCommand(CommandSender sender, Command cmd,
			String commandLabel, String[] args) {
		boolean flgRef = true;

		if (cmd.getName().equalsIgnoreCase("aeos")) {
			if (args.length > 0) {

				switch (args[0].toLowerCase()) {

				case "on":
					AEOS.flgAEOSEnabled = true;
					Mes(sender, "AntiEggOnSpawnerは" + ChatColor.GREEN + "有効" + ChatColor.RESET + "になりました。");
					Config_Save();
					break;

				case "off":
					AEOS.flgAEOSEnabled = false;
					Mes(sender, "AntiEggOnSpawnerは" + ChatColor.RED + "無効" + ChatColor.RESET + "になりました。");
					Config_Save();
					break;

				case "disqualify":// スポナーにエッグを使った時の取り上げ
					if (args.length >= 2) {
						switch (args[1].toLowerCase()) {
						case "on":
							AEOS.flgDisqualify = true;
							Mes(sender, "Egg取り上げは" + ChatColor.GREEN + "有効" + ChatColor.RESET + "になりました。");
							Config_Save();
							break;
						case "off":
							AEOS.flgDisqualify = false;
							Mes(sender, "Egg取り上げは" + ChatColor.RED + "無効" + ChatColor.RESET + "になりました。");
							Config_Save();
							break;
						default:
							Mes(sender, "/aeos disqualify [on|off] 使おうとしたエッグ取り上げの有効・無効を切り替えます。");
							break;
						}
					} else {
						Mes(sender, "/aeos disqualify [on|off] 使おうとしたエッグ取り上げの有効・無効を切り替えます。");
					}
					break;

				case "nbon":// ネザーでのベット使用禁止
					if (args.length >= 2) {
						switch (args[1].toLowerCase()) {
						case "on":
							AEOS.flgNoBedOnNether = true;
							Mes(sender, "ネザーでのベッド使用は" + ChatColor.GREEN + "禁止" + ChatColor.RESET + "になりました。");
							Config_Save();
							break;
						case "off":
							AEOS.flgNoBedOnNether = false;
							Mes(sender, "ネザーでのベッド使用は" + ChatColor.RED + "許可" + ChatColor.RESET + "になりました。");
							Config_Save();
							break;
						default:
							Mes(sender, "/aeos nbon [on|off] ネザーでのベッド使用の禁止・許可を切り替えます。");
							break;
						}
					} else {
						Mes(sender, "/aeos nbon [on|off] ネザーでのベッド使用の禁止・許可を切り替えます。");
					}
					break;

				case "nboe":// エンドでのベット使用禁止
					if (args.length >= 2) {
						switch (args[1].toLowerCase()) {
						case "on":
							AEOS.flgNoBedOnEnd = true;
							Mes(sender, "エンドでのベッド使用は" + ChatColor.GREEN + "禁止" + ChatColor.RESET + "になりました。");
							Config_Save();
							break;
						case "off":
							AEOS.flgNoBedOnEnd = false;
							Mes(sender, "エンドでのベッド使用は" + ChatColor.RED + "許可" + ChatColor.RESET + "になりました。");
							Config_Save();
							break;
						default:
							Mes(sender, "/aeos nboe [on|off] エンドでのベッド使用の禁止・許可を切り替えます。");
							break;
						}
					} else {
						Mes(sender, "/aeos nboe [on|off] エンドでのベッド使用の禁止・許可を切り替えます。");
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
						Mes(sender, "パーミッション数は");
						Mes(sender, String.valueOf(pais.length));
						for (int i = 0; i < pais.length; i++) {
							Mes(sender, pais[i].getPermission());
						}
					} else {
						flgRef = false;
					}

					break;
				}

			} else {

				Mes(sender, "〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
				Mes(sender, "AntiEggOnSpawnerは" + (AEOS.flgAEOSEnabled ? ChatColor.GREEN + "有効" : ChatColor.RED + "無効")
						+ ChatColor.RESET + "です。");

				Mes(sender, "Egg取り上げは" + (AEOS.flgDisqualify ? ChatColor.GREEN + "有効" : ChatColor.RED + "無効")
						+ ChatColor.RESET + "です。");

				Mes(sender, "ネザーでのベット使用は" + (AEOS.flgNoBedOnNether ? ChatColor.GREEN + "禁止" : ChatColor.RED + "許可")
						+ ChatColor.RESET + "です。");

				Mes(sender, "エンドでのベット使用は" + (AEOS.flgNoBedOnEnd ? ChatColor.GREEN + "禁止" : ChatColor.RED + "許可")
						+ ChatColor.RESET + "です。");

				Mes(sender, "〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");

				Mes(sender, "/aeos 使用法および現在の状態を表示します。");
				Mes(sender, "/aeos [on|off] プラグインの有効・無効を切り替えます。");
				Mes(sender, "/aeos disqualify [on|off] 使おうとしたエッグ取り上げの有効・無効を切り替えます。");
				Mes(sender, "/aeos nbon [on|off] ネザーでのベッド使用の禁止・許可を切り替えます。");
				Mes(sender, "/aeos nboe [on|off] エンドでのベッド使用の禁止・許可を切り替えます。");
				// Mes(sender, "/aeos [Player] プレイヤーに一時的な使用許可を与えます");
			}
			
		} else {
			flgRef = false;
		}

		return flgRef;
	}

	// 〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓

	public Player getPlayer(String name) {
		for (Player player : Bukkit.getOnlinePlayers()) {
			if (player.getName().equals(name)) {
				return player;
			}
		}
		return null;
	}

	// 〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓

	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerInteractBlock(PlayerInteractEvent event) {

		Player player = event.getPlayer();
		Block target = event.getClickedBlock();

		if (AEOS.flgAEOSEnabled)// プラグインが有効の場合
		{
			if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
				// スポナーに使おうとしたか検出
				switch (target.getType()) {
				//case MOB_SPAWNER:
				case SPAWNER:
					// エッグを使おうとしたか検出
					String strItemName = player.getInventory().getItemInMainHand().getType().name().toUpperCase();
					if (strItemName.endsWith("EGG")) {
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
							inventory.removeItem(player.getInventory()
									.getItemInMainHand());
						}
					}
					break;
				default:
					break;
				}
			}
		}

		if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
			Biome bm = player.getWorld().getBiome(0, 0);
			boolean flgVerboteneWelt = false;
			String strWorldName = "";

			if (AEOS.flgNoBedOnNether) {
				switch (bm) {
				//case HELL://1.12.2まで
				//case NETHER:
				case NETHER_WASTES:
					flgVerboteneWelt = true;
					strWorldName = "ネザーワールド";
					break;

				default:
					break;
				}
			}

			if (AEOS.flgNoBedOnEnd) {
				switch (bm) {
				//case SKY://1.12.2まで
				case END_BARRENS:
				case END_HIGHLANDS:
				case END_MIDLANDS:
				case SMALL_END_ISLANDS:
				case THE_END:
					flgVerboteneWelt = true;
					strWorldName = "エンドワールド";
					break;

				default:
					break;
				}
			}

			if (flgVerboteneWelt) {

				String strBedChk = target.getType().name().toUpperCase();

				if (strBedChk.endsWith("BED")) {
					player.sendMessage(ChatColor.RED
							+ strWorldName + "でベッド使うと爆発するよ！！" + ChatColor.RESET);
					event.setCancelled(true);
					getServer().getLogger().info(
							player.getName() + "が" + strWorldName + "でベッドを使用しようとしました。");
				}
			}
		}
	}
}
