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
package es.uvigo.ei.sing.resttrie.repository;

import java.util.List;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;

import es.uvigo.ei.sing.resttrie.entity.Prefix;

public interface PrefixRepository extends GraphRepository<Prefix> {
	public Prefix getPrefixByPrefix(String prefix);
	
	@Query("MATCH (p:Prefix)-[*]->(w:Prefix) "
		+ "WHERE LOWER(p.prefix) =~ LOWER({0}) AND w.terminal = TRUE "
		+ "RETURN w.prefix")
	public List<String> getWordsWithPrefix(String prefix);
	
	@Query("MATCH (p:Prefix)-[*]->(w:Prefix) "
		+ "WHERE LOWER(p.prefix) =~ LOWER({0}) AND w.terminal = TRUE "
		+ "RETURN count(w)")
	public int countWordsWithPrefix(String prefix);
	
	@Query("MATCH (p:Prefix)-[*]->(w:Prefix) "
		+ "WHERE p.prefix =~ {0} AND w.terminal = TRUE "
		+ "RETURN w.prefix")
	public List<String> getWordsMatching(String text);
	
	@Query("MATCH (p:Prefix)-[*]->(w:Prefix) "
		+ "WHERE p.prefix =~ {0} AND w.terminal = TRUE "
		+ "RETURN count(w)")
	public int countWordsMatching(String prefix);
	
	@Query("MATCH (w:Prefix) "
		+ "WHERE w.terminal = TRUE "
		+ "RETURN count(w)")
	public int countWords(String prefix);
	
	@Query("MATCH (m:Prefix { terminal:false }) "
		+ "WHERE NOT(m-[*]->(:Prefix { terminal: true })) "
		+ "OPTIONAL MATCH ()-[r]->(m:Prefix { terminal:false }) "
		+ "WHERE NOT(m-[*]->(:Prefix { terminal: true })) "
		+ "DELETE r,m "
		+ "RETURN count(m)")
	public int deleteHangingNodes();
}
