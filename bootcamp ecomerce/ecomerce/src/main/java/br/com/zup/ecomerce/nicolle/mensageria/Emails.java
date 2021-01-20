package br.com.zup.ecomerce.nicolle.mensageria;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.zup.ecomerce.nicolle.model.Compra;
import br.com.zup.ecomerce.nicolle.model.Pergunta;

@Service
public class Emails {
	
	@Autowired
	private Mailer mailer;

	public void novaMensagemPergunta(@NotNull @Valid Pergunta pergunta) {
		//new RestTemplate().postForEntity("https://api.mandril.com", mandrilMessage, String.class)
		
		System.out.println("novaMensagemPergunta");
		
		mailer.send("<html>...</html>", "Nova pergunta sobre o produto que você está vendendo", pergunta.getUsuario().getLogin(), "novaPergunta@dominiomercadolivre.com", pergunta.getProduto().getVendedor().getLogin());
		
	}
	
	public void novaCompra(@NotNull @Valid Compra compra) {
		mailer.send("nova compra..." + compra, "Você tem uma nova compra",
				compra.getComprador().getLogin().toString(),
				"compras@nossomercadolivre.com",
				compra.getProduto().getVendedor().getLogin().toString());
	}
	
	public void compraMensagemComNF(@NotNull @Valid Compra compra) {
		mailer.send("Você fez uma compra " + compra, ", do produto ",
				compra.getProduto().getNome().toString(), ", no nosso site mercadolivre.com.br", 
				"do vendedor " + compra.getProduto().getVendedor().getLogin().toString() 
				+ " sua compra foi processada com sucesso" );
	}
}
