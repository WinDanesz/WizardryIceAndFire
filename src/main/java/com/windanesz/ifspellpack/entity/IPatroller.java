package com.windanesz.ifspellpack.entity;

import com.windanesz.ifspellpack.world.Patrol;

import java.util.List;
import java.util.UUID;

public interface IPatroller {

	Patrol getPatrol();

	void setPatrol(Patrol patrol);

	default boolean isInPatrol() {
		return this.getPatrol() != null;
	}
}
