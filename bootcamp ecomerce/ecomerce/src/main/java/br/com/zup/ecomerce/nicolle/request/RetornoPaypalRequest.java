package br.com.zup.ecomerce.nicolle.request;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

import br.com.zup.ecomerce.nicolle.enums.StatusTransacao;
import br.com.zup.ecomerce.nicolle.model.Compra;
import br.com.zup.ecomerce.nicolle.model.Transacao;
import br.com.zup.ecomerce.nicolle.service.RetornoGatewayPagamento;

public class RetornoPaypalRequest implements RetornoGatewayPagamento {
	
	@Min(0)
	@Max(1)
	private int status;
	
	@NotBlank
	private String idTransacao;

	public RetornoPaypalRequest(@Min(0) @Max(1) int status, @NotBlank String idTransacao) {
		this.status = status;
		this.idTransacao = idTransacao;
	}
	
	public Transacao toTransacao(Compra compra) {
		//ternario
		StatusTransacao statusCalculado = 
				this.status == 0 
					? StatusTransacao.erro 
						: StatusTransacao.sucesso;
		return new Transacao(statusCalculado, idTransacao, compra);
	}
}
