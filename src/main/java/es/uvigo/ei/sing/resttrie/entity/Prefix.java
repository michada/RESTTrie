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

import static java.util.Objects.requireNonNull;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.neo4j.graphdb.Direction;
import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.Indexed;
import org.springframework.data.neo4j.annotation.NodeEntity;
import org.springframework.data.neo4j.annotation.RelatedToVia;

@NodeEntity
public class Prefix implements Comparable<Prefix>, Serializable {
	private static final long serialVersionUID = 1L;
	
	@GraphId private Long nodeId;
	@Indexed(unique = true) private String prefix;
	
	private boolean terminal;
	
	@RelatedToVia(type = "FOLLOWED_BY", elementClass = FollowedBy.class, direction = Direction.OUTGOING)
	private Set<FollowedBy> children;
	
	Prefix() {}
	
	public Prefix(String prefix) {
		this(prefix, false);
	}
	
	public Prefix(String prefix, boolean terminal) {
		this.prefix = prefix;
		this.terminal = terminal;
		this.children = new HashSet<>();
	}
	
	public Long getNodeId() {
		return nodeId;
	}
	
	public String getPrefix() {
		return prefix;
	}
	
	public void setTerminal(boolean terminal) {
		this.terminal = terminal;
	}
	
	public boolean isTerminal() {
		return terminal;
	}
	
	public SortedSet<Prefix> getChildren() {
		return new TreeSet<>(
			this.children.stream()
				.map(FollowedBy::getTo)
			.collect(Collectors.toSet())
		);
	}
	
	public FollowedBy addFollowigPrefix(Prefix follower) {
		requireNonNull(follower);
		
		final String followerPrefix = follower.getPrefix()
			.substring(0, follower.getPrefix().length() - 1);
		
		if (!(this.getPrefix()).equals(followerPrefix)) {
			throw new IllegalArgumentException("Invalid follower");
		}
		
		final FollowedBy relation = new FollowedBy(this, follower);
		this.children.add(relation);
		
		return relation;
	}

	@Override
	public int compareTo(Prefix o) {
		return this.getPrefix().compareTo(o.getPrefix());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((prefix == null) ? 0 : prefix.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Prefix other = (Prefix) obj;
		if (prefix == null) {
			if (other.prefix != null)
				return false;
		} else if (!prefix.equals(other.prefix))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return this.prefix + (this.terminal ? " [Terminal]" : "");
	}
}
