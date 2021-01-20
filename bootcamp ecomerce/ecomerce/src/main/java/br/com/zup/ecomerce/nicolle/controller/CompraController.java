package br.com.zup.ecomerce.nicolle.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.zup.ecomerce.nicolle.enums.GatewayPagamento;
import br.com.zup.ecomerce.nicolle.mensageria.Emails;
import br.com.zup.ecomerce.nicolle.model.Compra;
import br.com.zup.ecomerce.nicolle.model.Produto;
import br.com.zup.ecomerce.nicolle.model.Usuario;
import br.com.zup.ecomerce.nicolle.repository.ComprasRepository;
import br.com.zup.ecomerce.nicolle.repository.ProdutosRepository;
import br.com.zup.ecomerce.nicolle.repository.UsuariosRepository;
import br.com.zup.ecomerce.nicolle.request.CompraRequest;

@RestController
@RequestMapping(value = "ecomerce/compras")
public class CompraController {
	
	
	@Autowired
	private ProdutosRepository produtosRepository;
	
	@Autowired
	private UsuariosRepository usuariosRepository;
	
	@Autowired
	private ComprasRepository comprasRepository;
	
	@Autowired
	private Emails emails;
	
	@PostMapping  
	public ResponseEntity<?> comprar(@RequestBody @Valid CompraRequest request, UriComponentsBuilder builder) throws BindException{
		
		Produto produtoParaCompra = produtosRepository.findById(request.getIdProduto()).get();
		
		int quantidade = request.getQuantidade();
		boolean deduziu = produtoParaCompra.tiraEstoque(request.getQuantidade());
		if(deduziu) {
			Usuario comprador = usuariosRepository.findByLogin("nicolle@teste.com.br").get();
			GatewayPagamento gateway = request.getGateway();
			Compra compra = new Compra(produtoParaCompra, quantidade, comprador, gateway);
			comprasRepository.save(compra);
			emails.novaCompra(compra);
			
			//String redireciona = compra.urlRedirecionamento(builder);
			
			return ResponseEntity.ok(compra.urlRedirecionamento(builder));
			
			//if(gateway.equals(GatewayPagamento.pagseguro)) {
//			URI uriPagseguro = UriComponentsBuilder.fromPath("/ecomerce/retorno-pagseguro/{id}").buildAndExpand(compra.getId()).toUri();
//			return ResponseEntity.ok("pagseguro.com?returnId=" + compra.getId() + "&redirectUrl="+ uriPagseguro);
		//	
//			//Do Alberto
////			String urlRetornoPagSeguro = UriComponentsBuilder.fromPath("/ecomerce/retorno-pagseguro/{id}")
////					.buildAndExpand(compra.getId().toString());
//			//do guia
//			//pagseguro.com?returnId={idGeradoDaCompra}&redirectUrl={urlRetornoAppPosPagamento}
			
		//} else {
//			URI uriPaypal = UriComponentsBuilder.fromPath("/ecomerce/retorno-paypal/{id}").buildAndExpand(compra.getId()).toUri();
//			return ResponseEntity.ok("paypal.com/" + compra.getId() + "?redirectUrl="+ uriPaypal);
//			//do guia
//			//paypal.com/{idGeradoDaCompra}?redirectUrl={urlRetornoAppPosPagamento}

		}
		
		BindException problemaComEstoque = new BindException(request, "compraRequest");
		problemaComEstoque.rejectValue(null, "Não é possível realizar a compra, quantidade indisponível no estoque");
		throw problemaComEstoque;
		
		//System.out.println("deu ruim");
		
		
	}

}
