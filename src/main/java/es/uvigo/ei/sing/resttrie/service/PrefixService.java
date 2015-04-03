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

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.uvigo.ei.sing.resttrie.repository.PrefixRepository;

@Path("/prefix")
@Service
@Produces(MediaType.TEXT_PLAIN)
@Consumes(MediaType.TEXT_PLAIN)
@Transactional
public class PrefixService {
	@Inject
	private PrefixRepository repository;
	
	@GET
	@Path("/{prefix}")
	public Response get(@PathParam("prefix") String prefix) {
		final List<String> wordsWithPrefix = repository.getWordsWithPrefix(prefix);
		
		return Response.ok(String.join("\n", wordsWithPrefix)).build();
	}
	
	@GET
	@Path("/{prefix}/count")
	public Response count(@PathParam("prefix") String prefix) {
		return Response.ok(repository.countWordsWithPrefix(prefix)).build();
	}
}
