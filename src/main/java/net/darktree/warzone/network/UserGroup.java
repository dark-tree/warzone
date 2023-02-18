package net.darktree.warzone.network;

import net.darktree.warzone.Main;
import net.darktree.warzone.network.packet.GroupSyncPacket;
import net.darktree.warzone.network.packet.WorldSyncPacket;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class UserGroup {

	public static UserGroup instance;

	public final Relay relay;
	public final List<Integer> users = new ArrayList<>();
	public final int host;
	public final int id;

	public UserGroup(Relay relay, int host, int group) {
		this.relay = relay;
		this.host = host;
		this.id = group;
		this.users.add(host);
	}

	public void join(int uid) {
		users.add(uid);
	}

	public void left(int uid) {
		users.remove(Integer.valueOf(uid));
	}

	private void sync() {
		GroupSyncPacket packet = new GroupSyncPacket(this);

		for (int uid : users) {
			if (uid != host) {
				packet.sendToUser(uid);
			}
		}
	}

	public void close() {
		relay.close("Connection closed.");
	}

	/**
	 * Create a new user group, will return it with the onOpen callback, if the
	 * connection is closed the onClose callback will be called. onOpen is not
	 * guarantied to be called.
	 */
	public static void make(String hostname, Consumer<UserGroup> onOpen, Consumer<String> onClose) {
		Relay relay = Relay.open(hostname, Relay.PORT, new Role.Host(), onClose);

		// onClose already called
		if (relay == null) {
			return;
		}

		relay.onOpen(self -> {
			UserGroup group = new UserGroup(self, self.getUid(), self.getGid());

			self.onGroupJoined(uid -> {
				group.join(uid);
				group.sync();
				new WorldSyncPacket(Main.game.getWorld().orElseThrow()).sendToUser(uid);
			});

			self.onGroupLeft(uid -> {
				group.left(uid);
				group.sync();
			});

			onOpen.accept(group);
			instance = group;
		});

		relay.start();
	}

	/**
	 * Join a group with the given id, will return it with the onOpen callback, if the
	 * connection is closed the onClose callback will be called. onOpen is not
	 * guarantied to be called.
	 */
	public static void join(String hostname, int gid, Consumer<UserGroup> onOpen, Consumer<String> onClose) {
		Relay relay = Relay.open(hostname, Relay.PORT, new Role.Client(gid), onClose);

		// onClose already called
		if (relay == null) {
			return;
		}

		relay.onOpen(self -> {
			self.setPacketListener(Packets.GROUP_SYNC, group -> {
				onOpen.accept((UserGroup) group);
				instance = (UserGroup) group;
			});
		});

		relay.start();
	}

	public static <T> void broadcast(Packet<T> packet, int except) {
		UserGroup group = UserGroup.instance;

		if (group != null) {
			group.relay.broadcastMessage(except, packet.getBuffer().buffer());
		} else {
			packet.apply(PacketContext.LOCAL);
		}
	}

	public static <T> void sendToUser(Packet<T> packet, int uid) {
		UserGroup group = UserGroup.instance;

		if (group != null) {
			group.relay.sendMessage(uid, packet.getBuffer().buffer());
		} else {
			packet.apply(PacketContext.LOCAL);
		}
	}

	public static <T> void sendToHost(Packet<T> packet) {
		UserGroup group = UserGroup.instance;

		if (group != null) {
			group.relay.sendMessage(group.host, packet.getBuffer().buffer());
		} else {
			packet.apply(PacketContext.LOCAL);
		}
	}

}
