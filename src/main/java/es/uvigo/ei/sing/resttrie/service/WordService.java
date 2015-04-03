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
package es.uvigo.ei.sing.resttrie.service;

import java.util.Deque;
import java.util.LinkedList;
import java.util.Optional;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.uvigo.ei.sing.resttrie.entity.Prefix;
import es.uvigo.ei.sing.resttrie.repository.PrefixRepository;

@Path("/word")
@Service
@Produces(MediaType.TEXT_PLAIN)
@Consumes(MediaType.TEXT_PLAIN)
@Transactional
public class WordService {
	@Inject
	private PrefixRepository repository;
	
	@POST
	@Path("/{word}")
	public Response add(@PathParam("word") String word) {
		final Deque<Prefix> prefixes = new LinkedList<>();
		Prefix current = null;

		String prefix = "";
		for (int i = 0; i < word.length(); i++) {
			final char letter = word.charAt(i);
			prefix += letter;
			
			final Prefix newPrefix = Optional.ofNullable(repository.getPrefixByPrefix(prefix))
				.orElse(new Prefix(prefix));
			
			if (i == word.length() - 1) {
				newPrefix.setTerminal(true);
			}
			
			repository.save(newPrefix);
			
			if (current != null) {
				current.addFollowigPrefix(newPrefix, letter);
			}
			
			current = newPrefix;
			
			prefixes.push(current); // Reverse order needed
		}
		
		repository.save(prefixes);
		
		return Response.ok().build();
	}
	
	@DELETE
	@Path("/{word}")
	public Response delete(@PathParam("word") String word) {
		final Prefix prefix = repository.getPrefixByPrefix(word);
		
		if (prefix == null || !prefix.isTerminal()) {
			return Response.status(Status.BAD_REQUEST).build();
		} else {
			repository.delete(prefix);
			repository.deleteHangingNodes();
			
			return Response.ok().build();
		}
	}
	
	@GET
	@Path("/count")
	public Response count() {
		return Response.ok(repository.count()).build();
	}
}
