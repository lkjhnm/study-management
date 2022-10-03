package com.grasstudy.study.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.domain.Persistable;

import java.util.Objects;
import java.util.UUID;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
abstract class PersistableEntity implements Persistable<String> {

	@Id
	protected String id;

	@Override
	public boolean isNew() {
		boolean isNew = Objects.isNull(this.id);
		this.id = isNew ? UUID.randomUUID().toString() : this.id;
		return isNew;
	}
}
