package it.polito.tdp.porto.model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.Graphs;
import org.jgrapht.alg.shortestpath.*;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import it.polito.tdp.porto.db.PortoDAO;
import it.polito.tdp.porto.db.PortoDAO.coAutore;

public class Model {

	private Graph<Author, DefaultEdge> grafo;
	private Map<Integer, Author> authorIdMap;
	private PortoDAO dao;

	public Model() {
		this.grafo = new SimpleGraph<>(DefaultEdge.class);
		dao = new PortoDAO();
		this.authorIdMap = dao.getAutori().parallelStream().collect(Collectors.toMap(Author::getId, a -> a));
		List<coAutore> coAutori = dao.getListaCoautori(authorIdMap);
		Graphs.addAllVertices(this.grafo, authorIdMap.values());
		for (coAutore c : coAutori)
			this.grafo.addEdge(c.getA1(), c.getA2());
		System.out.format("Ci sono %d vertici e %d archi\n", this.grafo.vertexSet().size(), this.grafo.edgeSet().size());
	}

	public List<Author> getAutori() {
		return new ArrayList<Author>(this.authorIdMap.values());
	}

	public List<Author> getCoAutoriOf(Author autore) {
		return this.grafo.outgoingEdgesOf(autore).stream().map(a -> this.grafo.getEdgeTarget(a))
				.collect(Collectors.toList());
	}

	public List<Paper> collegaAutori(Author autore1, Author autore2) {
		System.out.println("Autore 1" + autore1);
		System.out.println("Autore 2" + autore2);
		GraphPath<Author, DefaultEdge> cammino = DijkstraShortestPath.findPathBetween(this.grafo, autore1, autore2);
		List<DefaultEdge> archi = cammino.getEdgeList();
		List<Paper> res = new LinkedList<>();
		for(DefaultEdge e : archi) {
			Author a1 = this.grafo.getEdgeSource(e);
			Author a2 = this.grafo.getEdgeTarget(e);
			Paper p = dao.getArticolo(a1, a2);
			res.add(p);
		}
		return res;
	}

}
