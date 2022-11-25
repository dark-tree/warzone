package net.darktree.warzone.network;

import net.darktree.warzone.network.packet.GroupSyncPacket;
import net.darktree.warzone.util.Util;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
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
		users.remove(uid);
	}

	private void sync() {
		GroupSyncPacket packet = new GroupSyncPacket(this);

		for (int uid : users) {
			if (uid != host) {
				packet.sendToUser(uid);
			}
		}
	}

	private void close() {
		relay.close();
		instance = null;
	}

	public static void make(String hostname, Consumer<UserGroup> groupCallback, Consumer<String> errorCallback) {
		Relay.open(Side.HOST, hostname, relay -> {
			Timer timer = Util.runAsyncAfter(() -> {
				relay.close();
				errorCallback.accept("Timeout! Failed to create a group after " + Relay.TIMEOUT + " seconds!");
			}, Relay.TIMEOUT * 1000);

			relay.onGroupCreated(gid -> {
				UserGroup group = new UserGroup(relay, relay.getUid(), gid);

				timer.cancel();

				relay.onGroupJoined(uid -> {
					group.join(uid);
					group.sync();
				});

				relay.onGroupLeft(uid -> {
					group.left(uid);
					group.sync();
				});

				groupCallback.accept(group);
				UserGroup.instance = group;
			});

			relay.createGroup();
		}, errorCallback);
	}

	public static void join(String hostname, int gid, Consumer<UserGroup> groupCallback, Consumer<String> errorCallback) {
		Relay.open(Side.CLIENT, hostname, relay -> {
			Timer timer = Util.runAsyncAfter(() -> {
				relay.close();
				errorCallback.accept("Timeout! Failed to join a group after " + Relay.TIMEOUT + " seconds!");
			}, Relay.TIMEOUT * 1000);

			relay.setPacketListener(Packets.GROUP_SYNC, group -> {
				timer.cancel();
				groupCallback.accept((UserGroup) group);
				UserGroup.instance = (UserGroup) group;
			});
			relay.joinGroup(gid);
		}, errorCallback);
	}

	public static void closeAll() {
		if (instance != null) {
			instance.close();
		}
	}

	public static <T> void broadcast(Packet<T> packet, int except) {
		UserGroup group = UserGroup.instance;

		if (group != null) {
			group.relay.broadcastMessage(except, packet.getBuffer());
		} else {
			packet.apply();
		}
	}

	public static <T> void sendToUser(Packet<T> packet, int uid) {
		UserGroup group = UserGroup.instance;

		if (group != null) {
			group.relay.sendMessage(uid, packet.getBuffer());
		} else {
			packet.apply();
		}
	}

	public static <T> void sendToHost(Packet<T> packet) {
		UserGroup group = UserGroup.instance;

		if (group != null) {
			group.relay.sendMessage(group.host, packet.getBuffer());
		} else {
			packet.apply();
		}
	}

}
