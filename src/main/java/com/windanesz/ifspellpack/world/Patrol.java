package com.windanesz.ifspellpack.world;

import com.windanesz.ifspellpack.entity.IPatroller;

import java.util.List;

public class Patrol {

	private final int level;
	private List<IPatroller> patrollers;

	public Patrol(int level) {
		this.level = level;
	}

	public int getLevel() {
		return this.level;
	}

	public List<IPatroller> getPatrollers() {
		return this.patrollers;
	}

	public void addPatroller(IPatroller patroller) {
		this.patrollers.add(patroller);
		patroller.setPatrol(this);
	}

	public void addPatrollers(List<IPatroller> patrollers) {
		for (IPatroller patroller : patrollers) {
			this.addPatroller(patroller);
		}
	}

	public void removePatroller(IPatroller patroller) {
		this.patrollers.remove(patroller);
	}

}
