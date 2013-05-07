package com.yunfeng.game.world.manager;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.springframework.stereotype.Service;

import com.yunfeng.game.module.user.User;
import com.yunfeng.game.world.IRoom;
import com.yunfeng.game.world.IZone;

@Service
public class UserManager {
	// private final ConcurrentMap<ISession, User> usersBySession;
	private final ConcurrentMap<String, User> usersByName;
	private final ConcurrentMap<Integer, User> usersById;
	private IRoom ownerRoom;
	private IZone ownerZone;

	public UserManager() {
		// this.usersBySession = new ConcurrentHashMap<>();
		this.usersByName = new ConcurrentHashMap<>();
		this.usersById = new ConcurrentHashMap<>();
	}

	public void addUser(User user) {
		if (containsId(user.getId())) {
			return;
		}
		this.usersById.put(Integer.valueOf(user.getId()), user);
		this.usersByName.put(user.getUsername(), user);
		// this.usersBySession.put(user.getSession(), user);
	}

	public User getUserById(int id) {
		return this.usersById.get(Integer.valueOf(id));
	}

	public User getUserByName(String name) {
		return this.usersByName.get(name);
	}

	// public User getUserBySession(ISession session) {
	// return (User) this.usersBySession.get(session);
	// }

	public void removeUser(int userId) {
		User user = this.usersById.get(Integer.valueOf(userId));

		if (user == null) {
			System.out.println("User is Null");
			return;
		} else {
			removeUser(user);
		}
	}

	public void removeUser(String name) {
		User user = this.usersByName.get(name);

		if (user == null) {
			System.out.println("User is Null");
			return;
		} else {
			removeUser(user);
		}
	}

	// public void removeUser(ISession session) {
	// User user = (User) this.usersBySession.get(session);
	//
	// if (user == null) {
	// throw new SFSRuntimeException("Can't remove user with session: "
	// + session + ". User was not found.");
	// }
	// removeUser(user);
	// }

	public boolean containsId(int userId) {
		return this.usersById.containsKey(Integer.valueOf(userId));
	}

	public boolean containsName(String name) {
		return this.usersByName.containsKey(name);
	}

	// public boolean containsSessions(ISession session) {
	// return this.usersBySession.containsKey(session);
	// }

	public boolean containsUser(User user) {
		return this.usersById.containsValue(user);
	}

	public IRoom getOwnerRoom() {
		return this.ownerRoom;
	}

	public void setOwnerRoom(IRoom ownerRoom) {
		this.ownerRoom = ownerRoom;
	}

	public IZone getOwnerZone() {
		return this.ownerZone;
	}

	public void setOwnerZone(IZone ownerZone) {
		this.ownerZone = ownerZone;
	}

	public List<User> getAllUsers() {
		return new ArrayList<User>(this.usersById.values());
	}

	// public List<ISession> getAllSessions() {
	// return new ArrayList<ISession>(this.usersBySession.keySet());
	// }

	public int getUserCount() {
		return this.usersById.values().size();
	}

	// public int getNPCCount() {
	// int npcCount = 0;
	// for (User user : this.usersById.values()) {
	// if (user.isNpc()) {
	// npcCount++;
	// }
	// }
	// return npcCount;
	// }

	public void disconnectUser(int userId) {
		User user = this.usersById.get(Integer.valueOf(userId));

		if (user == null) {
			System.out.println("User is Null");
			return;
		} else {
			disconnectUser(user);
		}
	}

	// public void disconnectUser(ISession session) {
	// User user = (User) this.usersBySession.get(session);
	//
	// if (user == null)
	// this.logger.warn("Can't disconnect user with session: " + session
	// + ". User was not found.");
	// else
	// disconnectUser(user);
	// }

	public void disconnectUser(String name) {
		User user = this.usersByName.get(name);

		if (user == null) {
			System.out.println("User is Null");
			return;
		} else {
			disconnectUser(user);
		}
	}

	public void disconnectUser(User user) {
		removeUser(user);
	}

	public void removeUser(User user) {
		this.usersById.remove(Integer.valueOf(user.getId()));
		this.usersByName.remove(user.getUsername());
		// this.usersBySession.remove(user.getSession());
	}

	public void executeCommand() {
		// TODO Auto-generated method stub

	}

}