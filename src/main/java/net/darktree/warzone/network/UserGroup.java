package net.darktree.warzone.network;

import net.darktree.warzone.util.Util;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.function.Consumer;

public class UserGroup {

	private final Relay relay;
	public final List<Integer> users = new ArrayList<>();
	public final int host;
	public final int group;

	public UserGroup(Relay relay, int host, int group) {
		this.relay = relay;
		this.host = host;
		this.group = group;
		this.users.add(host);
	}

	public void join(int uid) {
		users.add(uid);
	}

	public void left(int uid) {
		users.remove(uid);
	}

	private void sync() {
		Packets.GROUP_SYNC.send(relay, this);
	}

	public static void make(String hostname, Consumer<UserGroup> groupCallback, Consumer<String> errorCallback) {
		Relay.open(hostname, relay -> {
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
			});

			relay.createGroup();
		}, errorCallback);
	}

	public static void join(String hostname, int gid, Consumer<UserGroup> groupCallback, Consumer<String> errorCallback) {
		Relay.open(hostname, relay -> {
			Timer timer = Util.runAsyncAfter(() -> {
				relay.close();
				errorCallback.accept("Timeout! Failed to join a group after " + Relay.TIMEOUT + " seconds!");
			}, Relay.TIMEOUT * 1000);

			relay.setPacketListener(Packets.GROUP_SYNC, group -> {
				timer.cancel();
				groupCallback.accept(group);
			});
			relay.joinGroup(gid);
		}, errorCallback);
	}

}
