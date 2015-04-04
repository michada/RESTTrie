/*
 * #%L
 * REST Trie
 * %%
 * Copyright (C) 2015 Miguel Reboiro-Jato
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Lesser Public License for more details.
 * 
 * You should have received a copy of the GNU General Lesser Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/lgpl-3.0.html>.
 * #L%
 */
package es.uvigo.ei.sing.resttrie.entity;

import java.io.Serializable;

import org.springframework.data.neo4j.annotation.EndNode;
import org.springframework.data.neo4j.annotation.Fetch;
import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.RelationshipEntity;
import org.springframework.data.neo4j.annotation.StartNode;

@RelationshipEntity(type = "FOLLOWED_BY")
public class FollowedBy implements Serializable {
	private static final long serialVersionUID = 1L;

	@GraphId private Long nodeId;
	
	@StartNode @Fetch
	private Prefix from;
	
	@EndNode @Fetch
	private Prefix to;
	
	FollowedBy() {}

	public FollowedBy(Prefix from, Prefix to) {
		this.from = from;
		this.to = to;
	}

	public Prefix getFrom() {
		return from;
	}

	public void setFrom(Prefix from) {
		this.from = from;
	}

	public Prefix getTo() {
		return to;
	}

	public void setTo(Prefix to) {
		this.to = to;
	}
}
