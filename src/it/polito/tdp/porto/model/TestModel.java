package it.polito.tdp.porto.model;

import java.util.List;

public class TestModel {

	public static void main(String[] args) {

		Model model = new Model();
//		System.out.println(model.getCoAutoriOf(model.getAutori().get(0)));
//		System.out.println(model.getCoAutoriOf(model.getAutori().get(21)));
//		System.out.println(model.getCoAutoriOf(model.getAutori().get(482)));
//		System.out.println(model.getCoAutoriOf(model.getAutori().get(12)));

		Author autore1 = model.getAutori().get(12);
		List<Author> tmp = model.getAutori();
		tmp.removeAll(model.getCoAutoriOf(autore1));
		Author autore2 = tmp.get(4);
		List<Paper> paperi = model.collegaAutori(autore1, autore2);
		paperi.forEach(a->System.out.println(a));
	}

}
