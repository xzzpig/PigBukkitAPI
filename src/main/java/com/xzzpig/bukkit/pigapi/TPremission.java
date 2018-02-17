package com.xzzpig.bukkit.pigapi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TPremission {
	private static List<TPremission> checked = new ArrayList<TPremission>();
	private static List<TPremission> prelist = new ArrayList<TPremission>();

	public static TPremission[] getAllPremissions() {
		return prelist.toArray(new TPremission[0]);
	}

	public static TPremission valueOf(String name) {
		for (TPremission pre : prelist) {
			if (pre.getName().equalsIgnoreCase(name))
				return pre;
		}
		return null;
	}

	private List<TPremission> children = new ArrayList<TPremission>();

	private String name;

	public TPremission(String name, TPremission[] children) {
		if (!prelist.contains(this))
			prelist.add(this);
		this.name = name;
		if (children != null)
			this.children = new ArrayList<TPremission>(Arrays.asList(children));
	}

	private List<TPremission> _getAllChildren() {
		checked.add(this);
		if (this.children == null)
			return null;
		List<TPremission> childlist = new ArrayList<TPremission>();
		for (TPremission pre : this.children) {
			childlist.add(pre);
			List<TPremission> child = pre._getAllChildren();
			if (child != null) {
				for (TPremission pre2 : child) {
					if (!childlist.contains(pre2))
						childlist.add(pre2);
				}
			}
		}
		return childlist;
	}

	public TPremission addChild(TPremission child) {
		if (this.children.contains(child))
			return this;
		children.add(child);
		return this;
	}

	public TPremission delChild(TPremission child) {
		if (this.children.contains(child))
			return this;
		children.remove(child);
		return this;
	}

	public List<TPremission> getAllChildren() {
		if (this.children == null)
			return null;
		List<TPremission> childlist = new ArrayList<TPremission>();
		for (TPremission pre : this.children) {
			childlist.add(pre);
			List<TPremission> child = pre._getAllChildren();
			if (child != null) {
				for (TPremission pre2 : child) {
					if (!childlist.contains(pre2))
						childlist.add(pre2);
				}
			}
		}
		checked.clear();
		return childlist;
	}

	public List<TPremission> getAllParents() {
		List<TPremission> parlist = new ArrayList<TPremission>();
		for (TPremission pre : prelist) {
			if (pre.hasChild(this))
				parlist.add(pre);
		}
		if (parlist.size() == 0) {
			return null;
		}
		return parlist;
	}

	public TPremission[] getChildren() {
		return children.toArray(new TPremission[0]);
	}

	public String getName() {
		return name;
	}

	public boolean hasChild(TPremission premission) {
		if (premission == this)
			return true;
		List<TPremission> childlist = this.getAllChildren();
		if (childlist == null)
			return false;
		else if (childlist.contains(premission))
			return true;
		else
			return false;
	}
}
