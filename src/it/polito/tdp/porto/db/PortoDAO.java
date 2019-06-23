package it.polito.tdp.porto.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingDeque;

import it.polito.tdp.porto.model.Author;
import it.polito.tdp.porto.model.Paper;

public class PortoDAO {

	/*
	 * Dato l'id ottengo l'autore.
	 */
	public Author getAutore(int id) {

		final String sql = "SELECT * FROM author where id=?";

		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, id);

			ResultSet rs = st.executeQuery();

			if (rs.next()) {

				Author autore = new Author(rs.getInt("id"), rs.getString("lastname"), rs.getString("firstname"));
				return autore;
			}

			return null;

		} catch (SQLException e) {
			// e.printStackTrace();
			throw new RuntimeException("Errore Db");
		}
	}

	/*
	 * Dato l'id ottengo l'articolo.
	 */
	public Paper getArticolo(int eprintid) {

		final String sql = "SELECT * FROM paper where eprintid=?";

		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, eprintid);

			ResultSet rs = st.executeQuery();

			if (rs.next()) {
				Paper paper = new Paper(rs.getInt("eprintid"), rs.getString("title"), rs.getString("issn"),
						rs.getString("publication"), rs.getString("type"), rs.getString("types"));
				return paper;
			}

			return null;

		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Errore Db");
		}
	}

	public List<Author> getAutori() {

		final String sql = "SELECT id, lastname, firstname FROM author";
		List<Author> autori = new LinkedList<>();
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);

			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				Author autore = new Author(rs.getInt("id"), rs.getString("lastname"), rs.getString("firstname"));
				autori.add(autore);
			}

			return autori;

		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Errore Db");
		}
	}

	public List<coAutore> getListaCoautori(Map<Integer, Author> authorIdMap) {
		final String sql = "SELECT DISTINCT c1.authorid AS a1, c2.authorid AS a2 FROM creator c1, creator c2 "
				+ "WHERE c1.eprintid = c2.eprintid AND c1.authorid<c2.authorid";
		List<coAutore> coAutori = new LinkedList<>();
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);

			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				Author autore1 = authorIdMap.get(rs.getInt("a1"));
				Author autore2 = authorIdMap.get(rs.getInt("a2"));
				coAutori.add(new coAutore(autore1, autore2));
			}

			return coAutori;

		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Errore Db");
		}
	}

	public class coAutore {
		private Author a1;
		private Author a2;

		public coAutore(Author a1, Author a2) {
			this.a1 = a1;
			this.a2 = a2;
		}

		public Author getA1() {
			return a1;
		}

		public Author getA2() {
			return a2;
		}

	}

	public Paper getArticolo(Author a1, Author a2) {
		final String sql = "SELECT p.eprintid, c1.authorid, c2.authorid, p.eprintid, p.title,"
				+ "p.issn, p.publication, p.TYPE, p.types  FROM paper p, creator c1, creator c2  "
				+ "WHERE c1.eprintid = p.eprintid  AND c2.eprintid = p.eprintid  "
				+ "AND c1.authorid != c2.authorid  AND c1.authorid = ?  AND c2.authorid = ?";

		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, a1.getId());
			st.setInt(2, a2.getId());

			ResultSet rs = st.executeQuery();

			if (rs.next()) {
				Paper paper = new Paper(rs.getInt("p.eprintid"), rs.getString("p.title"), rs.getString("p.issn"),
						rs.getString("p.publication"), rs.getString("p.type"), rs.getString("p.types"));
				return paper;
			}

			return null;

		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Errore Db");
		}
	}

}